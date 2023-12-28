package per.pslilysm.sdk_library

import android.app.Application
import per.pslilysm.sdk_library.util.reflection.ReflectionUtil

/**
 * Application instance's holder
 *
 * @author pslilysm
 * @since 1.0.0
 */

@Volatile
private var applicationObj: Application? = null

private val lock = Any()

fun setApplication(application: Application?) {
    synchronized(lock) {
        applicationObj = application
    }
}

val application: Application by lazy {
    if (applicationObj == null) {
        synchronized(lock) {
            if (applicationObj == null) {
                try {
                    val activityThread = ReflectionUtil.getStaticFieldValue<Any>(
                        "android.app.ActivityThread",
                        "sCurrentActivityThread"
                    )
                    applicationObj = ReflectionUtil.getFieldValue(activityThread, "mInitialApplication")
                } catch (e: ReflectiveOperationException) {
                    throw RuntimeException(
                        "Unable to get application by reflection, " +
                                "maybe the mInitialApplication or sCurrentActivityThread field in ActivityThread is denied to access by android system, " +
                                "you can check your phone log for more detail and call setApplication() manually",
                        e
                    )
                }
            }
        }
    }
    applicationObj!!
}