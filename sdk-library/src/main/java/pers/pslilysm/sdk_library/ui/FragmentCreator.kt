package pers.pslilysm.sdk_library.ui

import androidx.fragment.app.Fragment

/**
 * A Creator to create fragment by `fmtClazz`
 *
 * @param T Fragment type
 * @author pslilysm
 * @since 1.0.5
 */
interface FragmentCreator<T : Fragment?> {
    fun create(fmtClazz: Class<T>): T
}