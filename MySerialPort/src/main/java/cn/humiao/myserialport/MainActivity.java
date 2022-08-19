package cn.humiao.myserialport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private EditText editText;
    private TextView tv;
    private SerialPortUtil serialPortUtil;
    private SharedPreferences preferences;

    /*
        FE 00 01 00 00 EA 60 4B
        FE   00 01     00 00 EA 60      4B
        头部  协议头     ACC延时时间 ms    FE后六位之和
    */
    public static String parseSendData(int value) {
        String result = "";
        try {
            String hex = String.format("%08x", value).toUpperCase();
            int checkDec = 0;
            checkDec += 1;
            final char[] charArray = hex.toCharArray();
            for (int i = 0; i < charArray.length; i += 2) {
                String temp = String.valueOf(charArray[i]) + charArray[i + 1];
                checkDec += Integer.parseInt(temp, 16);
            }
            String checkHex = String.format("%02x", checkDec).toUpperCase();
            if (checkHex.length() > 2) {
                checkHex = checkHex.substring(checkHex.length() - 2);
            }
            result = "FE0001" + hex + checkHex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String reverse(String hex) {
        final char[] charArray = hex.toCharArray();
        final int length = charArray.length;
        final int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            final int c2i = c1i + 1;
            final char c1 = charArray[c1i];
            final char c2 = charArray[c2i];
            final int c3i = length - c1i - 2;
            final int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray).toUpperCase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        button = (Button) findViewById(R.id.btn);
        editText = findViewById(R.id.editTextNumber);
        tv = (TextView) findViewById(R.id.tv);
        int accDelayTime = preferences.getInt("acc_delay_time", 10);
        editText.setText(String.valueOf(accDelayTime));
        serialPortUtil = new SerialPortUtil();
        serialPortUtil.openSerialPort();
        sendData();
        button.setOnClickListener(v -> sendData());
    }

    private void sendData() {
        try {
            int accDelayTime = Integer.parseInt(editText.getText().toString());
            String data = parseSendData(accDelayTime * 1000);
            Log.i(TAG, data);
            if (!TextUtils.isEmpty(data)) {
                serialPortUtil.sendSerialPort(data);
            }
            preferences.edit().putInt("acc_delay_time", accDelayTime).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (serialPortUtil != null) {
            serialPortUtil.closeSerialPort();
        }
        super.onDestroy();
    }

    /**
     * 用EventBus进行线程间通信，也可以使用Handler
     *
     * @param string
     */
    //@Subscribe(threadMode = ThreadMode.MAIN)
    //public void onEventMainThread(String string) {
    //    Log.d(TAG, "获取到了从传感器发送到Android主板的串口数据");
    //    tv.setText(string);
    //}

}
