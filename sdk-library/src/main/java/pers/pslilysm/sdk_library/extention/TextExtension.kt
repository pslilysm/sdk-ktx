package pers.pslilysm.sdk_library.extention

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Extension for text
 *
 * @author pslilysm
 * Created on 2023/06/29 15:20
 * @since 2.2.0
 */

/**
 * @return A text without space
 */
fun String.cleanSpace(): String {
    return replace("\\s+".toRegex(), "")
}

/**
 * @return A text without wrap
 */
fun String.cleanWrap(): String {
    return replace("[\\s*\t\n\r]".toRegex(), "")
}

/**
 * @return Null if the text is empty
 */
fun String?.nullIfEmpty(): String? {
    return if (this?.isEmpty() == true) null else this
}

/**
 * Copy the text to clipboard
 */
fun String.copyToClipboard(context: Context) {
    val cm = context.getSystemService(ClipboardManager::class.java)
    val mClipData = ClipData.newPlainText("Label", this)
    cm.setPrimaryClip(mClipData)
}

/**
 * Clean the content after the dot(inclusive)
 */
fun String.cleanContentAfterDot(): String {
    val separatorIndex = this.lastIndexOf('/')
    if (separatorIndex != -1) {
        val index = this.indexOf('.', separatorIndex)
        if (index != -1) {
            return this.substring(0, index)
        }
    }
    return this
}