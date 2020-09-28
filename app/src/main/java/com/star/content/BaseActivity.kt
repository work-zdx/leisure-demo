package com.star.content

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.star.content.fragment.LoadingFragment

open class BaseActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val loadingFragment = LoadingFragment()

    var showingLoading = false

    fun showLoading() {
        if (!showingLoading) {
            showingLoading = true
            loadingFragment.isCancelable = false
            loadingFragment.show(supportFragmentManager, "loading")
        }
    }

    fun hideLoading() {
        if (showingLoading) {
            handler.postDelayed({
                try {
                    if (loadingFragment.isVisible) {
                        loadingFragment.dismissAllowingStateLoss()
                        showingLoading = false
                    } else if (loadingFragment.isAdded && !loadingFragment.isHidden) {
                        hideLoading()
                    }
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }, 500)
        }
    }
}