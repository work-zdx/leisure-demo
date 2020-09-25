package com.star.content

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bun.miitmdid.core.JLibrary
import com.bun.miitmdid.core.MdidSdkHelper
import com.bytedance.sdk.openadsdk.*
import com.starmedia.adsdk.AdRequest
import com.starmedia.adsdk.IRewardedVideo
import com.starmedia.adsdk.InnerRet
import com.starmedia.tinysdk.StarMedia

class ContentApplication: MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    var currentActivity: Activity? = null;

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


        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }
            override fun onActivityStarted(p0: Activity) {
            }
            override fun onActivityResumed(p0: Activity) {
                currentActivity = p0
            }
            override fun onActivityPaused(p0: Activity) {
                if(p0 == currentActivity){
                    currentActivity = null
                }
            }
            override fun onActivityStopped(p0: Activity) {
            }
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }
            override fun onActivityDestroyed(p0: Activity) {
            }
        })


        initAd()

        StarMedia.setRewardedVideoListener {
            loadRewardVideo()
        }

        StarMedia.setInterstitialListener {
            loadInterstitial()
        }

        StarMedia.init(this, "2")
    }

    lateinit var adManager: TTAdManager

    fun initAd(){
        adManager = TTAdSdk.init(
            this,
            TTAdConfig.Builder()
                .appId("5042771")
                .useTextureView(false)
                .appName(
                    applicationInfo.loadLabel(packageManager)
                        .toString()
                )
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true)
                .allowShowPageWhenScreenLock(true)
                .debug(true)
                .directDownloadNetworkType(
                    TTAdConstant.NETWORK_STATE_WIFI,
                    TTAdConstant.NETWORK_STATE_3G,
                    TTAdConstant.NETWORK_STATE_4G
                )
                .supportMultiProcess(true)
                .build()
        )
    }

    fun loadInterstitial() {
        if(currentActivity != null) {
            val adNative = TTAdSdk.getAdManager().createAdNative(currentActivity)

            val adSlot = AdSlot.Builder()
                .setCodeId("945392270")
                .setSupportDeepLink(true)
                .setAdCount(1)
                .setExpressViewAcceptedSize(
                    resources.displayMetrics.widthPixels.toFloat(),
                    0F
                )
                .setImageAcceptedSize(640, 320)
                .build()

            adNative.loadInteractionExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
                override fun onError(code: Int, message: String) {
                    Toast.makeText(currentActivity, message, Toast.LENGTH_LONG).show()
                }

                override fun onNativeExpressAdLoad(nativeExpressAds: List<TTNativeExpressAd>) {

                    var nativeExpressAd = nativeExpressAds[0]

                    nativeExpressAd.setExpressInteractionListener(object :
                        TTNativeExpressAd.AdInteractionListener {
                        /**
                         * 插屏广告消失用户回调
                         */
                        override fun onAdDismiss() {
                            Log.e("TTInterstitial", "头条插屏广告隐藏事件监听!")
                        }

                        /**
                         *广告的点击回调
                         * @param view 广告
                         * @param type 广告的交互类型
                         */
                        override fun onAdClicked(view: View, type: Int) {
                            Log.e("TTInterstitial", "头条插屏广告点击事件监听!")
                        }

                        /**
                         * 广告的展示回调 每个广告仅回调一次
                         * @param view 广告view
                         * @param type 广告的交互类型
                         */
                        override fun onAdShow(view: View, type: Int) {
                            Log.e("TTInterstitial", "头条插屏广告展示事件监听!")
                        }

                        /**
                         * 个性化模板渲染失败
                         * @param view
                         */
                        override fun onRenderFail(view: View, msg: String, code: Int) {
                            Log.e("TTInterstitial", "头条插屏广告模板渲染失败: $code : $msg")
                            nativeExpressAd.destroy()
                        }

                        /**
                         * 个性化模板渲染成功
                         * @param view
                         * @param width  返回view的宽 单位 dp
                         * @param height 返回view的高 单位 dp
                         */
                        override fun onRenderSuccess(view: View, width: Float, height: Float) {
                            Log.e("TTInterstitial", "头条插屏广告模板渲染成功!")
                            if(currentActivity != null) {
                                nativeExpressAd.showInteractionExpressAd(currentActivity)
                            }
                        }
                    })

                    nativeExpressAd.render()
                }
            })
        }
    }

    fun loadRewardVideo() {
        if(currentActivity != null) {
            val adNative = TTAdSdk.getAdManager().createAdNative(currentActivity)

            val adSlot = AdSlot.Builder()
                .setCodeId("942650839")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setMediaExtra("xxxx")
                .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build()

            adNative.loadRewardVideoAd(adSlot, object : TTAdNative.RewardVideoAdListener {
                override fun onError(code: Int, message: String) {
                    Log.e("TTPlatform", "Request rewarded video ad error: $code : $message")

                }

                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                override fun onRewardVideoCached() {
                    Log.e("TTPlatform", "Request rewarded video ad cached!")

                }

                //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
                override fun onRewardVideoAdLoad(rewardVideoAd: TTRewardVideoAd) {

                    rewardVideoAd.setRewardAdInteractionListener(object :
                        TTRewardVideoAd.RewardAdInteractionListener {
                        //视频广告展示回调
                        override fun onAdShow() {
                            Log.e("TTRewardedVideo", "头条激励视频广告展示事件监听")
                        }

                        //广告的下载bar点击回调
                        override fun onAdVideoBarClick() {
                            Log.e("TTRewardedVideo", "头条激励视频广告点击事件监听")
                        }

                        //视频广告关闭回调
                        override fun onAdClose() {
                            Log.e("TTRewardedVideo", "头条激励视频广告关闭事件监听")
                        }

                        //视频广告播放完毕回调
                        override fun onVideoComplete() {
                            Log.e("TTRewardedVideo", "头条激励视频广告播放完毕事件监听")
                        }

                        //视频广告播放错误
                        override fun onVideoError() {
                            Log.e("TTRewardedVideo", "头条激励视频广告播放错误事件监听")
                        }

                        //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                        override fun onRewardVerify(
                            rewardVerify: Boolean,
                            rewardAmount: Int,
                            rewardName: String?
                        ) {
                            Log.e(
                                "TTRewardedVideo",
                                "头条激励视频广告奖励验证回调 verify:$rewardVerify amount:$rewardAmount name:$rewardName"
                            )
                        }

                        //视频广告跳过视频播放
                        override fun onSkippedVideo() {
                            Log.e("TTRewardedVideo", "头条激励视频广告跳过播放事件监听")
                        }
                    })

                    if (currentActivity != null) {
                        rewardVideoAd.showRewardVideoAd(currentActivity)
                    }
                }
            })
        }
    }
}