package com.example.testapp.activity

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.virogu.library.common.extention.toast
import com.virogu.library.common.util.requestPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

data class GSensorData(val x: Float, val y: Float, val z: Float) {
    override fun toString(): String {
        return "[$x, $y, $z]"
    }
}

class SensorActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private val sensorList: HashMap<Int, Int> = HashMap()
    private val gSensorDataXYZ: MutableSharedFlow<GSensorData> = MutableSharedFlow(0, 1)

    private var job: Job? = null

    private lateinit var tvX: TextView
    private lateinit var tvY: TextView
    private lateinit var tvZ: TextView
    private lateinit var btStart: Button
    private lateinit var btEnd: Button

    private lateinit var outputDirectory: File

    private var max = 0f
        set(value) {
            if (field != value) {
                Timber.d("Max: $value")
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
        tvX = findViewById(R.id.tv_x)
        tvY = findViewById(R.id.tv_y)
        tvZ = findViewById(R.id.tv_z)
        btStart = findViewById(R.id.bt_start)
        btEnd = findViewById(R.id.bt_end)
        outputDirectory = getOutputDirectory()
        requestPermission(
            listOfNotNull(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Manifest.permission.HIGH_SAMPLING_RATE_SENSORS
                } else {
                    null
                }
            ), "需要以下权限才能使用该功能"
        ).request { allGranted, _, _ ->
            if (allGranted) {
                init()
            } else {
                toast("您拒绝了授予程序权限")
                return@request
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "${resources.getString(R.string.app_name)}/GSensor").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            File(filesDir, "GSensor").apply {
                mkdirs()
            }
        }
    }

    private fun init() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensorList.apply {
            // P290C
            // SensorManager.SENSOR_DELAY_UI       65ms  15/s
            // SensorManager.SENSOR_DELAY_NORMAL   200ms 5/s
            // SensorManager.SENSOR_DELAY_GAME     20ms  50/s
            // SensorManager.SENSOR_DELAY_FASTEST  2ms   500/s
            put(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_FASTEST)
            //put(Sensor.TYPE_PROXIMITY, SensorManager.SENSOR_DELAY_NORMAL)
        }.forEach {
            Timber.i("register sensor: $it")
            sensorManager?.registerListener(
                sensorListener,
                sensorManager?.getDefaultSensor(it.key),
                it.value
            )
        }
        lifecycleScope.launchWhenResumed {
            gSensorDataXYZ.collect {
                tvX.text = it.x.toString()
                tvY.text = it.y.toString()
                tvZ.text = it.z.toString()
            }
        }
        btStart.setOnClickListener {
            start()
        }
        btEnd.setOnClickListener {
            stop()
        }
        btStart.isEnabled = true
        btEnd.isEnabled = false
    }

    private var count = 0
    private var time = SystemClock.elapsedRealtime()
    private var sumCount = 1000
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) {
                return
            }
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    count++
                    if (count >= sumCount) {
                        count = 0
                        val spend = SystemClock.elapsedRealtime() - time
                        Timber.d("G sensor get $sumCount times, spend ${spend}ms[${spend / sumCount}ms/each]")
                        time = SystemClock.elapsedRealtime()
                    }
                    val value = event.values
                    val x = value[0]
                    val y = value[1]
                    val z = value[2]
                    max = event.sensor.maximumRange
                    gSensorDataXYZ.tryEmit(GSensorData(x, y, z))
                    Timber.v(value.toList().toString())
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Timber.i("onAccuracyChanged, sensor: ${sensor?.name}, accuracy: $accuracy")
        }

    }

    private val format = "yyyyMMdd_HH.mm.ss.SSS"

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun start() {
        btStart.isEnabled = false
        btEnd.isEnabled = true
        val time = SimpleDateFormat(format, Locale.getDefault()).format(System.currentTimeMillis())

        val fileX = File(outputDirectory, "$time/X_$time.wav")
        val fileY = File(outputDirectory, "$time/Y_$time.wav")
        val fileZ = File(outputDirectory, "$time/Z_$time.wav")
        val fileXt = File(outputDirectory, "$time/X_$time.txt")
        val fileYt = File(outputDirectory, "$time/Y_$time.txt")
        val fileZt = File(outputDirectory, "$time/Z_$time.txt")
        val fileXYZt = File(outputDirectory, "$time/XYZ_$time.txt")

        try {
            fileX.parentFile?.mkdirs()
            fileX.createNewFile()
            fileY.parentFile?.mkdirs()
            fileY.createNewFile()
            fileZ.parentFile?.mkdirs()
            fileZ.createNewFile()
        } catch (e: Throwable) {
            e.printStackTrace()
            Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show()
            stop()
            return
        }
        try {
            val xS = DataOutputStream(FileOutputStream(fileX, true))
            val yS = DataOutputStream(FileOutputStream(fileY, true))
            val zS = DataOutputStream(FileOutputStream(fileZ, true))
            val xtS = DataOutputStream(FileOutputStream(fileXt, true))
            val ytS = DataOutputStream(FileOutputStream(fileYt, true))
            val ztS = DataOutputStream(FileOutputStream(fileZt, true))
            val xyzTS = DataOutputStream(FileOutputStream(fileXYZt, true))
            job = lifecycleScope.launch(Dispatchers.IO) {
                xtS.writeBytes("Max: $max\n")
                ytS.writeBytes("Max: $max\n")
                ztS.writeBytes("Max: $max\n")
                xyzTS.writeBytes("[x, y, z]\n")
                launch {
                    gSensorDataXYZ.buffer().collect {
                        // Write X
                        xS.writeShort(it.x.toInt())
                        xtS.writeBytes("${it.x}, ")
                        // Write Y
                        yS.writeShort(it.y.toInt())
                        ytS.writeBytes("${it.y}, ")
                        // Write Z
                        zS.writeShort(it.z.toInt())
                        ztS.writeBytes("${it.z}, ")
                        // Write XYZ
                        xyzTS.writeBytes("$it\n")
                    }
                    xS.use { it.close() }
                    yS.use { it.close() }
                    zS.use { it.close() }
                    xtS.use { it.close() }
                    ytS.use { it.close() }
                    ztS.use { it.close() }
                    xyzTS.use { it.close() }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Toast.makeText(this, "记录文件失败", Toast.LENGTH_SHORT).show()
            stop()
            return
        }
    }

    private fun stop() {
        job?.cancel()
        job = null
        btStart.isEnabled = true
        btEnd.isEnabled = false
    }

    override fun onDestroy() {
        sensorManager?.unregisterListener(sensorListener)
        stop()
        super.onDestroy()
    }


}