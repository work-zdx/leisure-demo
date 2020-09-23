package com.star.content

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bun.miitmdid.core.JLibrary
import com.bun.miitmdid.core.MdidSdkHelper
import com.starmedia.tinysdk.StarMedia

class ContentApplication: MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        JLibrary.InitEntry(this)

        val activityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val runningAppProcesses =
            activityManager.runningAppProcesses

        val pid = Process.myPid()

        MdidSdkHelper.InitSdk(this, true) { support, supplier ->
            Log.e(
                "ContentApplication",
                "OAID: $support : ${supplier?.oaid} : ${supplier?.aaid} : ${supplier?.vaid}"
            )
        }

        for (processInfo in runningAppProcesses) {
            if (processInfo.pid == pid && packageName == processInfo.processName) {

                val backgroundIntent = Intent(this, BackgroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(backgroundIntent)
                } else {
                    startService(backgroundIntent)
                }
            }
        }

        StarMedia.init(this, "2")
    }
}