package com.ll.adhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.orhanobut.logger.Logger

/**
 *
 * xiaofang
 * 19-8-22
 *
 **/
class ADAccessibilityService : AccessibilityService() {
    override fun onInterrupt() {
        Logger.d("not implemented")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Logger.d("service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val nodeList = event.source?.findAccessibilityNodeInfosByText("跳过")
            if (nodeList != null && nodeList.size != 0) {
                Logger.d("nodeList.size:${nodeList.size}")
                for (node in nodeList) {
                    Logger.d("node packageName:${node.packageName} ,className:${node.className} ,text:${node.text} event.className:${event.className}")
                    Logger.d("performAction ACTION_CLICK on node clickable ${node.isClickable}")
                    if (node.isClickable) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    } else {
                        Logger.d("performAction ACTION_CLICK on parent clickable ${node.isClickable} parent.clickable ${node.parent.isClickable} parentClass ${node.parent.className}")
                        node.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }

                }
            }
        }
    }
}