package per.pslilysm.sdk_library.ui.dialog

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

    protected val binding: VB
    protected val builder: AlertDialog.Builder
    protected val dialog: AlertDialog

    init {
        binding = inflateViewBinding(LayoutInflater.from(context))
        builder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(true)
        setUp()
        dialog = builder.create()
    }

    abstract fun inflateViewBinding(layoutInflater: LayoutInflater): VB

    abstract fun setUp()

    abstract fun initWindowLp(lp: WindowManager.LayoutParams)

    override fun show() {
        dialog.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        initWindowLp(lp)
        dialog.window!!.attributes = lp
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun dismiss() {
        dialog.dismiss()
    }

}