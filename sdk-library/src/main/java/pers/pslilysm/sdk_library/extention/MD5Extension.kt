package pers.pslilysm.sdk_library.extention

import android.content.Context
import android.net.Uri
import org.apache.commons.codec.digest.DigestUtils
import pers.pslilysm.sdk_library.util.ThreadUtil
import java.io.File
import java.io.FileInputStream

/**
 * Extension for MD5
 *
 * @author cxd
 * Created on 2023/06/29 15:07
 * @since 2.2.0
 */

/**
 * @return MD5Hex string by the uri
 */
fun Uri.readMD5Str(context: Context): String {
    ThreadUtil.throwIfMainThread()
    return context.openUriInputStreamSafety(this)?.use {
        DigestUtils.md5(it).encodeHexString()
    } ?: ""
}

/**
 * @return MD5Hex string by the file
 */
fun File.readMD5Str(): String {
    ThreadUtil.throwIfMainThread()
    return FileInputStream(this).use {
        DigestUtils.md5(it).encodeHexString()
    }
}