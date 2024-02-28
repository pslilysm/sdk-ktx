package per.pslilysm.sdk_library

import android.app.Application
import per.pslilysm.sdk_library.util.reflection.ReflectionUtil
import per.pslilysm.sdk_library.util.reflection.ReflectionUtil.getFieldValue

/**
 * Application instance's holder
 *
 * @author pslilysm
 * @since 1.0.0
 */

@Volatile
private var application: Application? = null

private val lock = Any()

fun setApplication(app: Application?) {
    synchronized(lock) {
        application = app
    }
}

@Suppress("PrivateApi")
val app: Application by lazy {
    if (application == null) {
        synchronized(lock) {
            if (application == null) {
                try {
                    val activityThread = ReflectionUtil.getStaticFieldValue<Any>(
                        Class.forName("android.app.ActivityThread"), "sCurrentActivityThread"
                    )!!
                    application = activityThread.getFieldValue("mInitialApplication")
                } catch (e: ReflectiveOperationException) {
                    throw RuntimeException(
                        "Unable to get application by reflection, " +
                                "maybe the mInitialApplication or sCurrentActivityThread field in ActivityThread is denied to access by android system, " +
                                "you can check your phone log for more detail and call setApplication() manually", e
                    )
                }
            }
        }
    }
    application!!
}