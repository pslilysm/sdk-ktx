package per.pslilysm.sdk_library.extention

import android.content.Context
import android.net.Uri
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Extension for MD5
 *
 * @author pslilysm
 * Created on 2023/06/29 15:07
 * @since 2.2.0
 */

/**
 * @return MD5Hex string by the uri
 */
@Throws(IOException::class)
fun Uri.readMD5Str(context: Context): String {
    throwIfMainThread()
    return context.openInputStreamNotNull(this).use {
        DigestUtils.md5(it).encodeHexString()
    }
}

/**
 * @return MD5Hex string by the file
 */
@Throws(IOException::class)
fun File.readMD5Str(): String {
    throwIfMainThread()
    return FileInputStream(this).use {
        DigestUtils.md5(it).encodeHexString()
    }
}