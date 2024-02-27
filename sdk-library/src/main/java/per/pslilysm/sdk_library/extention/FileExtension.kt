package per.pslilysm.sdk_library.extention

/**
 * Extension for file
 *
 * @author cxd
 * Created on 2023/07/28 10:33
 * @since 2.2.5
 */

/**
 * @return true if the chat is valid for android file system
 */
fun Char.isFileNameCharValid(): Boolean {
    return if (this.code in 0x00..0x1f || this.code == 0x7F) {
        false
    } else when (this) {
        '"', '*', '/', ':', '<', '>', '?', '\\' -> false
        else -> true
    }
}

/**
 * @return true if the file name is valid for android file system
 */
fun String.isFileNameValid(): Boolean {
    toCharArray().forEach {
        if (!it.isFileNameCharValid()) {
            return false
        }
    }
    return true
}

/**
 * @return a new string which deleted all invalid file name char
 */
fun String.deleteInvalidFileNameChar(): String {
    val chatList = toCharArray().toMutableList()
    chatList.removeIf {
        !it.isFileNameCharValid()
    }
    return String(chatList.toCharArray())
}
