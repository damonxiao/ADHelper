package com.ll.adhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.orhanobut.logger.Logger

class MainActivity : AppCompatActivity() {
    private var mHandler = Handler();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHandler.postDelayed(Runnable { kotlin.run {
            AccessibilityUtils.checkAccessibilitySettings(this)
            finish()
        } }, 1000)
    }
}
