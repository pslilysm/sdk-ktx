package pers.pslilysm.sdk_library.extention

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import pers.pslilysm.sdk_library.AppHolder

/**
 * Extension for screen
 *
 * @author pslilysm
 * Created on 2023/06/29 16:39
 * @since 2.2.0
 */

val darkMode: Boolean
    get() = (AppHolder.get().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            == Configuration.UI_MODE_NIGHT_YES)

val statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() {
        var result = 0
        val resourceId =
            AppHolder.get().resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = AppHolder.get().resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

val screenWidth: Int
    get() {
        val wm = AppHolder.get().getSystemService(WindowManager::class.java)
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            wm.currentWindowMetrics.bounds.width()
//        } else {
//            val dm = DisplayMetrics()
//            wm.defaultDisplay.getMetrics(dm)
//            dm.widthPixels
//        }
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

val screenHeight: Int
    get() {
        val wm = AppHolder.get().getSystemService(WindowManager::class.java)
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            wm.currentWindowMetrics.bounds.height()
//        } else {
//            val dm = DisplayMetrics()
//            wm.defaultDisplay.getMetrics(dm)
//            dm.heightPixels
//        }
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

val screenRealWidth: Int
    get() {
        val wm = AppHolder.get().getSystemService(WindowManager::class.java)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wm.maximumWindowMetrics.bounds.width()
        } else {
            val dm = DisplayMetrics()
            wm.defaultDisplay.getRealMetrics(dm)
            dm.widthPixels
        }
    }

val screenRealHeight: Int
    get() {
        val wm = AppHolder.get().getSystemService(WindowManager::class.java)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wm.maximumWindowMetrics.bounds.height()
        } else {
            val dm = DisplayMetrics()
            wm.defaultDisplay.getRealMetrics(dm)
            dm.heightPixels
        }
    }

fun Float.dip2px(): Int {
    val scale = AppHolder.get().resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.pxToDip(): Float {
    val scale = AppHolder.get().resources.displayMetrics.density
    return (this / scale + 0.5f)
}

fun Float.spToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        AppHolder.get().resources.displayMetrics
    ).toInt()
}

val navBarHeight: Int
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() {
        val resources = AppHolder.get().resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

val screenDensityDpi: Int
    get() = AppHolder.get().resources.displayMetrics.densityDpi

fun Window.setSystemUiFlagAndTransparentStatusBar(
    layoutFullScreen: Boolean,
    fullScreen: Boolean,
    lightMode: Boolean,
    hideNav: Boolean
) {
    var flag = 0
    if (layoutFullScreen) {
        flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (fullScreen) {
        flag = flag or (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    if (hideNav){
        flag = flag or (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    if (lightMode) {
        flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    decorView.systemUiVisibility = flag
    statusBarColor = Color.TRANSPARENT
}


fun Window.keepScreenOn() {
    addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}