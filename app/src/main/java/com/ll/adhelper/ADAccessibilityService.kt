package com.ll.adhelper

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.orhanobut.logger.Logger
import java.lang.StringBuilder
import kotlin.math.absoluteValue

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
            if (nodeList != null && nodeList.size > 0) {
                for (node in nodeList) {
                    val nodeParentClickable = node.parent?.isClickable
                    Logger.d("node packageName:${node.packageName} ,className:${node.className} ,text:${node.text} , clickable:${node.isClickable} parent clickable $nodeParentClickable")
                    val nodeOutRect = Rect()
                    node.getBoundsInScreen(nodeOutRect)
                    val parent = node.parent
                    val parentOutRect = Rect()
                    parent.getBoundsInScreen(parentOutRect)
                    val nodeNearbyParent = rectNearby(nodeOutRect, parentOutRect)
                    Logger.d("nodeOutRect $nodeOutRect ,parentOutRect $parentOutRect, nodeNearbyParent $nodeNearbyParent")
                    if (node.isClickable) {
                        Logger.d("click node self to skip AD")
                        clickNode(node)
                        break;
                    } else if (nodeNearbyParent && nodeParentClickable == true) {
                        Logger.d("click parent to skip AD")
                        clickNode(parent)
                        break;
                    } else {
                        val childCount = parent.childCount
                        for (childIndex in 0 until childCount) {
                            val child = parent.getChild(childIndex)
                            val childOutRect = Rect()
                            child.getBoundsInScreen(childOutRect)
                            Logger.d("childOutRect $childOutRect")
                            Logger.d("child packageName:${child.packageName} ,className:${child.className} ,text:${child.text}")
                            if (rectNearby(nodeOutRect, childOutRect) && child.isClickable) {
                                Logger.d("click child to skip AD")
                                clickNode(child)
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clickNode(node : AccessibilityNodeInfo) {
        Logger.d("performAction ACTION_CLICK node packageName:${node.packageName} ,className:${node.className} ,text:${node.text} , clickable:${node.isClickable}")
        toast2User(applicationContext, node)
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    private fun rectNearby(rect1 : Rect, rect2 : Rect) :Boolean {
        return ((rect1.left - rect2.left).absoluteValue < 100) && ((rect1.top - rect2.top).absoluteValue < 100);
    }

    private fun toast2User(context : Context, node : AccessibilityNodeInfo) {
        val builder = StringBuilder()
        builder.append("跳过广告\n")
            .append(node.packageName)
            .append("/")
            .append(node.className)
            .append(",text:")
            .append(node.text)
        Toast.makeText(context, builder.toString() , Toast.LENGTH_LONG).show()
    }
}