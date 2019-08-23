package com.ll.adhelper

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import com.orhanobut.logger.Logger

/**
 *
 * xiaofang
 * 19-8-23
 *
 **/
class AccessibilityUtils {
    companion object {
        fun checkAccessibilitySettings (context: Context) {
            if (!isAccessibilitySettingsOn(context)) {
                Logger.d("start to ACTION_ACCESSIBILITY_SETTINGS")
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, R.string.tips_access_service_ready, Toast.LENGTH_LONG).show()
            }
        }

        private fun isAccessibilitySettingsOn(context: Context): Boolean {
            var accessibilityEnabled = 0
            // TestService为对应的服务
            val service = context.packageName + "/" + ADAccessibilityService::class.qualifiedName
            Logger.d("service name $service")
            try {
                accessibilityEnabled = Settings.Secure.getInt(
                    context.applicationContext.contentResolver,
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
                )
                Logger.v("accessibilityEnabled = $accessibilityEnabled")
            } catch (e: Settings.SettingNotFoundException) {
                Logger.e("Error finding setting, default accessibility to not found: ${e.message}")
            }

            val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

            if (accessibilityEnabled == 1) {
                Logger.v("***ACCESSIBILITY IS ENABLED*** -----------------")
                val settingValue = Settings.Secure.getString(
                    context.applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
                )
                if (settingValue != null) {
                    mStringColonSplitter.setString(settingValue)
                    while (mStringColonSplitter.hasNext()) {
                        val accessibilityService = mStringColonSplitter.next()

                        Logger.v(
                            "-------------- > accessibilityService :: $accessibilityService $service"
                        )
                        if (accessibilityService.equals(service, ignoreCase = true)) {
                            Logger.v(
                                "We've found the correct setting - accessibility is switched on!"
                            )
                            return true
                        }
                    }
                }
            } else {
                Logger.v("***ACCESSIBILITY IS DISABLED***")
            }
            return false
        }
    }


}