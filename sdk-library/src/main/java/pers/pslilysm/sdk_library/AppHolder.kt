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
    private var sApplication: Application? = null
    fun set(sApplication: Application?) {
        AppHolder.sApplication = sApplication
    }

    @kotlin.jvm.JvmStatic
    fun get(): Application {
        if (sApplication == null) {
            synchronized(AppHolder::class.java) {
                if (sApplication == null) {
                    try {
                        val activityThread = ReflectionUtil.getStaticFieldValue<Any>(
                            "android.app.ActivityThread",
                            "sCurrentActivityThread"
                        )
                        sApplication =
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
        return sApplication!!
    }
}