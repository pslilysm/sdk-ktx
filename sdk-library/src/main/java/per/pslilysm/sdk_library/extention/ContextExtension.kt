package per.pslilysm.sdk_library.extention

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import java.io.IOException
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

@Throws(IOException::class)
fun Context.openInputStreamNotNull(uri: Uri): InputStream {
    return contentResolver.openInputStream(uri) ?: throw IOException("ContentProvider crashed")
}

@RequiresPermission(android.Manifest.permission.VIBRATE)
fun Context.vibrateWithDefaultEffective(milliseconds: Long = 2000L) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(VibratorManager::class.java)
        vibratorManager.vibrate(CombinedVibration.createParallel(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)))
    } else {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(milliseconds)
        }
    }
}