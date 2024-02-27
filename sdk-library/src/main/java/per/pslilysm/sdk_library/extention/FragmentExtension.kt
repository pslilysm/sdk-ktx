package per.pslilysm.sdk_library.extention

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import per.pslilysm.sdk_library.ui.FragmentCreator
import per.pslilysm.sdk_library.ui.FragmentFinder
import per.pslilysm.sdk_library.util.reflection.ReflectionUtil

/**
 * Extension for fragment
 *
 * @author pslilysm
 * Created on 2023/06/29 17:48
 * @since 2.2.0
 */

val viewPager2Finder: FragmentFinder by lazy {
    object : FragmentFinder {
        override fun <T : Fragment?> findFragment(
            fmtClass: Class<out Fragment?>,
            vararg args: Any?
        ): T? {
            val manager = args[0] as FragmentManager
            val position = args[1] as Int
            return manager.findFragmentByTag("f$position") as T?
        }
    }
}

fun <T : Fragment?> FragmentManager.findOrCreateFmtInViewPager2(
    fmtClass: Class<T>,
    position: Int,
): T {
    return findOrCreateFmt(fmtClass, viewPager2Finder, object : FragmentCreator<T> {
        override fun create(fmtClazz: Class<T>): T {
            return try {
                ReflectionUtil.newInstance(fmtClazz)
            } catch (e: ReflectiveOperationException) {
                throw e.toRuntime()
            }
        }
    }, this, position)
}


fun <T : Fragment?> findOrCreateFmt(
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