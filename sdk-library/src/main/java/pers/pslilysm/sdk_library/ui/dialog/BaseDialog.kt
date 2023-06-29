package pers.pslilysm.sdk_library.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding

/**
 * A Base Dialog implements DialogInterface
 * Easy to design various dialogs
 *
 * @author pslilysm
 * @since 1.1.0
 */
abstract class BaseDialog<VB : ViewBinding>(protected val context: Context) : DialogInterface {

    protected val mBinding: VB
    protected val mBuilder: AlertDialog.Builder
    protected val mDialog: AlertDialog

    init {
        mBinding = inflateViewBinding(LayoutInflater.from(context))
        mBuilder = AlertDialog.Builder(context)
            .setView(mBinding.root)
            .setCancelable(true)
        setUp()
        mDialog = mBuilder.create()
    }

    abstract fun inflateViewBinding(layoutInflater: LayoutInflater): VB

    abstract fun setUp()

    abstract fun initWindowLp(lp: WindowManager.LayoutParams)

    override fun show() {
        mDialog.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(mDialog.window!!.attributes)
        initWindowLp(lp)
        mDialog.window!!.attributes = lp
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun dismiss() {
        mDialog.dismiss()
    }

}