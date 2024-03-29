package per.pslilysm.sdk_library.extention

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Extension for view
 *
 * @author pslilysm
 * Created on 2023/06/29 15:27
 * @since 2.2.0
 */

private val finishListener =
    View.OnClickListener { v: View -> (v.context as Activity).finish() }

/**
 * @param visible if true the view will be visible else gone
 */
fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * @param visible if true the view will be visible else invisible
 */
fun View.visibleOrInvisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/**
 * Show keyboard by the EditText
 */
fun EditText.showInput() {
    this.isFocusableInTouchMode = true
    this.requestFocus()
    val imm = this.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    this.setSelection(this.text.length)
}

/**
 * Hide keyboard from the EditText
 */
fun EditText.hideInput() {
    this.clearFocus()
    val imm = this.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * Set the view's top margin to the same height as the status bar
 */
fun View.setStatusBarMargin() {
    val lp = this.layoutParams as ViewGroup.MarginLayoutParams
    lp.topMargin += statusBarHeight
    this.layoutParams = lp
}

/**
 * Set the view's top padding to the same height as the status bar
 */
fun View.setStatusBarPadding() {
    this.setPadding(
        this.paddingLeft,
        this.paddingTop + statusBarHeight,
        this.paddingRight,
        this.paddingBottom
    )
}

/**
 * Finish activity when view clicked
 */
fun View.finishActivityWhenClick() {
    this.setOnClickListener(finishListener)
}

/**
 * @return the height of ime
 */
fun View.getImeHeight(): Int {
    return ViewCompat.getRootWindowInsets(this)?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
}

/**
 * @return true if ime is visible
 */
fun View.isImeVisible(): Boolean {
    return ViewCompat.getRootWindowInsets(this)?.isVisible(WindowInsetsCompat.Type.ime()) == true
}