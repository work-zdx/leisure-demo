package com.star.content.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.star.content.FlowLockActivity

class LockBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val action = intent.action

            if (action != null && action == Intent.ACTION_SCREEN_ON) {
                try {
                    val startIntent = Intent(context, FlowLockActivity::class.java)
                    startIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    context?.startActivity(startIntent)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            } else if (action != null && action == Intent.ACTION_SCREEN_OFF){
                // 屏幕关闭
            }
        }
    }
}