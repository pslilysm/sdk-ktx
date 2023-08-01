package pers.pslilysm.sdk_library

import android.app.Application
import pers.pslilysm.sdk_library.util.reflection.ReflectionUtil

/**
 * Application instance's holder
 *
 * @author pslilysm
 * @since 1.0.0
 */
object AppHolder {
    @Volatile
    private var application: Application? = null
    fun set(application: Application?) {
        AppHolder.application = application
    }

    fun get(): Application {
        if (application == null) {
            synchronized(AppHolder::class.java) {
                if (application == null) {
                    try {
                        val activityThread = ReflectionUtil.getStaticFieldValue<Any>(
                            "android.app.ActivityThread",
                            "sCurrentActivityThread"
                        )
                        application =
                            ReflectionUtil.getFieldValue(activityThread, "mInitialApplication")
                    } catch (e: ReflectiveOperationException) {
                        throw RuntimeException(
                            "Unable to get application by reflection, " +
                                    "maybe the mInitialApplication or sCurrentActivityThread field in ActivityThread is denied to access by android system, " +
                                    "you can check your phone log for more detail and call set() manually",
                            e
                        )
                    }
                }
            }
        }
        return application!!
    }
}