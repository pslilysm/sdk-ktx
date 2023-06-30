package pers.pslilysm.sdk_library.extention

import android.content.Context
import android.net.Uri
import android.os.Build
import java.io.InputStream

/**
 * Extension for Context
 *
 * @author pslilysm
 * Created on 2023/06/29 14:51
 * @since 2.2.0
 */

/**
 * @return Our app's version code
 */
fun Context.longVersionCode(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this.packageManager.getPackageInfo(this.packageName, 0).longVersionCode
    } else {
        this.packageManager.getPackageInfo(this.packageName, 0).versionCode.toLong()
    }
}

/**
 * @return Our app's version name
 */
fun Context.versionName(): String {
    return this.packageManager.getPackageInfo(this.packageName, 0).versionName
}

/**
 * @return A InputStream of the uri or null if exception occurs
 */
fun Context.openUriInputStreamSafety(uri: Uri): InputStream? {
    return try {
        contentResolver.openInputStream(uri)
    } catch (e: Exception) {
        null
    }
}