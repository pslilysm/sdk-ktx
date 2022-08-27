package pers.pslilysm.sdk_library.base

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import pers.pslilysm.sdk_library.AppHolder
import pers.pslilysm.sdk_library.simple.SimpleLifecycle
import pers.pslilysm.sdk_library.util.ExceptionUtil
import pers.pslilysm.sdk_library.util.reflection.ReflectionUtil


/**
 * Provide some default UiComponent interface implementation
 *
 * @author pslilysm
 * @since 1.0.0
 */
object UIComponentPlugins {

    @kotlin.jvm.JvmStatic
    val sSimpleFinder: FragmentFinder = object : FragmentFinder {
        override fun <T : Fragment?> findFragment(
            fmtClass: Class<out Fragment?>,
            vararg args: Any?
        ): T? {
            val manager = args[0] as FragmentManager
            return manager.findFragmentByTag(fmtClass.name) as T?
        }
    }
    @kotlin.jvm.JvmStatic
    val sViewPager2Finder: FragmentFinder = object : FragmentFinder {
        override fun <T : Fragment?> findFragment(
            fmtClass: Class<out Fragment?>,
            vararg args: Any?
        ): T? {
            val manager = args[0] as FragmentManager
            val position = args[1] as Int
            return manager.findFragmentByTag("f$position") as T?
        }
    }

    @kotlin.jvm.JvmStatic
    fun <T : Fragment?> findOrCreateFmt(
        fmtClass: Class<T>,
        finder: FragmentFinder,
        vararg args: Any?
    ): T {
        return findOrCreateFmt1(fmtClass, finder, object : FragmentCreator<T> {
            override fun create(fmtClazz: Class<T>): T {
                return try {
                    ReflectionUtil.newInstance(fmtClazz)
                } catch (e: ReflectiveOperationException) {
                    throw ExceptionUtil.rethrow(e)
                }
            }
        }, *args)
    }

    @kotlin.jvm.JvmStatic
    fun <T : Fragment?> findOrCreateFmt1(
        fmtClass: Class<T>,
        finder: FragmentFinder,
        creator: FragmentCreator<T>,
        vararg args: Any?
    ): T {
        var fmt = finder.findFragment<T>(fmtClass, *args)
        if (fmt == null) {
            fmt = creator.create(fmtClass)
        }
        return fmt!!
    }

    @kotlin.jvm.JvmStatic
    fun initUIComponent(uiComponent: UIComponent<*>) {
        when (uiComponent) {
            is Activity -> {
                AppHolder.get().registerActivityLifecycleCallbacks(object : SimpleLifecycle {
                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                        if (activity == uiComponent) {
                            if (uiComponent.mViewBinding != null) {
                                activity.setContentView(uiComponent.mViewBinding!!.root)
                            }
                            uiComponent.setup(savedInstanceState)
                        }
                    }

                    override fun onActivityDestroyed(activity: Activity) {
                        if (activity == uiComponent) {
                            uiComponent.performOnDestroy()
                            AppHolder.get().unregisterActivityLifecycleCallbacks(this)
                        }
                    }
                })
            }
            is Fragment -> {
                uiComponent.lifecycle.addObserver(object : LifecycleEventObserver {
                    @Volatile
                    private var mLoaded = false
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_RESUME) {
                            if (!mLoaded) {
                                mLoaded = true
                                try {
                                    uiComponent.setup(ReflectionUtil.getFieldValue(uiComponent, "mSavedFragmentState"))
                                } catch (e: ReflectiveOperationException) {
                                    throw ExceptionUtil.rethrow(e)
                                }
                            }
                        } else if (event == Lifecycle.Event.ON_DESTROY) {
                            uiComponent.lifecycle.removeObserver(this)
                            uiComponent.performOnDestroy()
                        }
                    }
                })
            }
            else -> {
                throw IllegalArgumentException("unrecognized UIComponent -> $uiComponent")
            }
        }
    }

}