package pers.pslilysm.sdk_library.extention

import android.os.Looper
import android.widget.Toast
import pers.pslilysm.sdk_library.AppHolder
import pers.pslilysm.sdk_library.util.concurrent.GlobalExecutors

/**
 * Extension for toast
 *
 * @author pslilysm
 * Created on 2023/06/29 16:35
 * @since 2.2.0
 */

fun CharSequence.showShortToast() {
    ToastProp.newProp()
        .lengthShort(this)
        .showToast()
}

fun CharSequence.showLongToast() {
    ToastProp.newProp()
        .lengthLong(this)
        .showToast()
}

fun ToastProp.showToast() {
    val runnable = ShowToastRunnable(this)
    if (Looper.myLooper() == null) {
        GlobalExecutors.main.execute(runnable)
    } else {
        runnable.run()
    }
}


class ToastProp {

    companion object {

        fun newProp(): ToastProp {
            return ToastProp()
        }

    }

    var text: CharSequence? = null
    var duration: Int = Toast.LENGTH_SHORT
    var gravity: Int = 0
    var xOffset: Int = 0
    var yOffset: Int = 0
    var horizontalMargin: Float = 0f
    var verticalMargin: Float = 0f

    fun duration(duration: Int): ToastProp {
        this.duration = duration
        return this
    }

    @Deprecated(
        "Starting from Android [Build.VERSION_CODES.R], apps\n" +
                "     targeting API level [Build.VERSION_CODES#R] or higher, this method is a no-op when\n" +
                "     called on text toasts."
    )
    fun gravity(gravity: Int): ToastProp {
        this.gravity = gravity
        return this
    }

    @Deprecated(
        "Starting from Android [Build.VERSION_CODES.R], apps\n" +
                "     targeting API level [Build.VERSION_CODES#R] or higher, this method is a no-op when\n" +
                "     called on text toasts."
    )
    fun xOffset(xOffset: Int): ToastProp {
        this.xOffset = xOffset
        return this
    }

    @Deprecated(
        "Starting from Android [Build.VERSION_CODES.R], apps\n" +
                "     targeting API level [Build.VERSION_CODES#R] or higher, this method is a no-op when\n" +
                "     called on text toasts."
    )
    fun yOffset(yOffset: Int): ToastProp {
        this.yOffset = yOffset
        return this
    }

    @Deprecated(
        "Starting from Android [Build.VERSION_CODES.R], apps\n" +
                "     targeting API level [Build.VERSION_CODES#R] or higher, this method is a no-op when\n" +
                "     called on text toasts."
    )
    fun horizontalMargin(horizontalMargin: Float): ToastProp {
        this.horizontalMargin = horizontalMargin
        return this
    }

    @Deprecated(
        "Starting from Android [Build.VERSION_CODES.R], apps\n" +
                "     targeting API level [Build.VERSION_CODES#R] or higher, this method is a no-op when\n" +
                "     called on text toasts."
    )
    fun verticalMargin(verticalMargin: Float): ToastProp {
        this.verticalMargin = verticalMargin
        return this
    }

    fun text(text: CharSequence?): ToastProp {
        this.text = text
        return this
    }

    fun lengthShort(text: CharSequence?): ToastProp {
        return text(text)
            .duration(Toast.LENGTH_SHORT)
    }

    fun lengthLong(text: CharSequence?): ToastProp {
        return text(text)
            .duration(Toast.LENGTH_LONG)
    }

}

private class ShowToastRunnable(private val toastProp: ToastProp) :
    Runnable {

    companion object {
        val sToastTLS = ThreadLocal<Toast>()
    }

    override fun run() {
        var toast = sToastTLS.get()
        toast?.cancel()
        toast = Toast.makeText(AppHolder.get(), toastProp.text, toastProp.duration)
        toast!!.setGravity(toastProp.gravity, toastProp.xOffset, toastProp.yOffset)
        toast.setMargin(toastProp.horizontalMargin, toast.verticalMargin)
        toast.show()
        sToastTLS.set(toast)
    }
}