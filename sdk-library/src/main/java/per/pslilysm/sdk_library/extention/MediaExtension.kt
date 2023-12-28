package per.pslilysm.sdk_library.extention

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import per.pslilysm.sdk_library.AppHolder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Date
import java.util.concurrent.CountDownLatch

/**
 * Extension for media
 *
 * @author pslilysm
 * Created on 2023/06/29 15:09
 * @since 2.2.0
 */

fun File.save2MediaStoreAsImage(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
    alternativeDisplayName: String? = null
): Uri? {
    return try {
        FileInputStream(this).save2MediaStoreAsImage(context, relativePath, displayName, alternativeDisplayName)
    } catch (e: IOException) {
        null
    }
}

/**
 * Save the picture to gallery via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStoreAsImage(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
    alternativeDisplayName: String? = null
): Uri? {
    return this.save2MediaStore(context, displayName, alternativeDisplayName, relativePath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the audio to MediaStore via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStoreAsAudio(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
    alternativeDisplayName: String? = null
): Uri? {
    return this.save2MediaStore(context, displayName, alternativeDisplayName, relativePath, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the audio to MediaStore via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStoreAsVideo(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
    alternativeDisplayName: String? = null
): Uri? {
    return this.save2MediaStore(context, displayName, alternativeDisplayName, relativePath, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the media to MediaStore via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStore(
    context: Context,
    displayName: String,
    alternativeDisplayName: String?,
    relativePath: String,
    mediaExternalUri: Uri
): Uri? {
    throwIfMainThread()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        contentValues.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
        contentValues.put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        val uri = contentResolver.insert(mediaExternalUri, contentValues)
        uri?.let {
            try {
                this.use { `is` ->
                    contentResolver.openOutputStream(uri).use { os ->
                        IOUtils.copy(`is`, os)
                    }
                }
            } catch (_: Exception) {
            }
        }
        return if (uri == null && !alternativeDisplayName.isNullOrEmpty()) {
            save2MediaStore(context, alternativeDisplayName, null, relativePath, mediaExternalUri)
        } else {
            uri
        }
    } else {
        var result: Uri? = null
        val countDownLatch = CountDownLatch(1)
        val originalOutFile = File(Environment.getExternalStorageDirectory(), relativePath + File.separator + displayName)
        FileUtils.forceMkdirParent(originalOutFile)
        val outFile = originalOutFile.addTimesIfExit(alternativeDisplayName = alternativeDisplayName) ?: return null
        try {
            FileUtils.copyInputStreamToFile(this@save2MediaStore, outFile)
            MediaScannerConnection.scanFile(AppHolder.get(), arrayOf(outFile.absolutePath), null) { _, uri ->
                result = uri
                countDownLatch.countDown()
            }
        } catch (_: Exception) {
            countDownLatch.countDown()
        }
        countDownLatch.await()
        return result
    }
}

/**
 * @return A file that are not duplicated on the disk
 */
fun File.addTimesIfExit(times: Int = 1, alternativeDisplayName: String?): File? {
    try {
        return if (times >= 100) {
            if (alternativeDisplayName.isNullOrEmpty()) {
                null
            } else {
                File(parentFile, alternativeDisplayName)
            }
        } else if (!createNewFile()) {
            val file = File(parentFile, "$nameWithoutExtension($times).$extension")
            if (!file.createNewFile()) {
                addTimesIfExit(times + 1, alternativeDisplayName)
            } else {
                file
            }
        } else {
            this
        }
    } catch (e: IOException) {
        return if (alternativeDisplayName.isNullOrEmpty()) {
            null
        } else {
            File(parentFile, alternativeDisplayName)
        }
    }
}

/**
 * @return A new cache file copied by the uri
 */
@Throws(IOException::class)
fun Uri.copyToNewCacheFile(): File {
    throwIfMainThread()
    AppHolder.get().openUriInputStreamSafety(this).use { `is` ->
        if (`is` == null) {
            throw IOException("InputStream is null, the uri is $this")
        }
        val newCacheFile = File(AppHolder.get().cacheDir, SystemClock.elapsedRealtimeNanos().toString())
        FileUtils.copyInputStreamToFile(`is`, newCacheFile)
        return newCacheFile
    }
}

/**
 * Convert bitmap to [ByteArrayInputStream]
 */
fun Bitmap.toInputStream(): InputStream {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, bos)
    return ByteArrayInputStream(bos.toByteArray())
}