package pers.pslilysm.sdk_library.ui.dialog

/**
 * Interface definition for a callback to be invoked when a dialog event trigger
 *
 * @author cxd
 * Created on 2023/06/29 14:43
 * @since 2.2.0
 */
interface DialogListener {

    /**
     * Close btn clicked.
     * by default we'll dismiss the dialog.
     *
     * @param dialogInterface perform your own action with this
     */
    fun onCloseClicked(dialogInterface: DialogInterface) {
        dialogInterface.dismiss()
    }

    /**
     * Close btn clicked.
     * by default we'll dismiss the dialog.
     *
     * @param dialogInterface perform your own action with this
     */
    fun onCancelClicked(dialogInterface: DialogInterface) {
        dialogInterface.dismiss()
    }

    /**
     * Confirm btn clicked
     *
     * @param dialogInterface perform your own action with this
     */
    fun onConfirmClicked(dialogInterface: DialogInterface) {

    }

    /**
     * Dialog dismissed
     */
    fun onDialogDismissed() {

    }

}