package com.example.testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.testapp.timber.LogTree
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        Timber.plant(LogTree(this))
    }

}
