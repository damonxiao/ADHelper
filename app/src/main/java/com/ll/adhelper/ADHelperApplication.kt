package com.ll.adhelper

import android.app.Application
import android.content.Intent
import android.provider.Settings
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


/**
 *
 * xiaofang
 * 19-8-22
 *
 **/
class ADHelperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val formatStrategy =
            PrettyFormatStrategy.newBuilder()
                .tag("AD_Helper")
                .methodCount(0)
                .methodOffset(0)
                .showThreadInfo(false)
                .singleLineMethodInfo(true)
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Logger.d("application inited")
    }
}