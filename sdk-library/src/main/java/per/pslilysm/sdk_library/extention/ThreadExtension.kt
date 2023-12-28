package per.pslilysm.sdk_library.extention

import android.os.Looper

/**
 * Extension for thread
 *
 * @author pslilysm
 * Created on 2023/06/30 17:42
 * @since 2.2.2
 */

/**
 * check current thread is main thread
 *
 * @throws IllegalStateException if current thread is not main thread
 */
fun checkIsMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) { "cur thread is " + Thread.currentThread().name + ", not main thread" }
}

/**
 * check current thread not main thread
 *
 * @throws IllegalStateException if current thread is main thread
 */
fun throwIfMainThread() {
    check(Looper.getMainLooper() != Looper.myLooper()) { "cur thread is main thread" }
}