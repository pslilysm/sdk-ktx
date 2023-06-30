package pers.pslilysm.sdk_library.extention

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import pers.pslilysm.sdk_library.AppHolder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.concurrent.CountDownLatch

/**
 * Extension for media
 *
 * @author pslilysm
 * Created on 2023/06/29 15:09
 * @since 2.2.0
 */

/**
 * Save the picture to gallery via Bitmap
 *
 * @return The media's uri if success or null
 */
fun Bitmap.save2MediaStoreAsImage(context: Context, relativePath: String = Environment.DIRECTORY_DCIM, displayName: String): Uri? {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, bos)
    val bin = ByteArrayInputStream(bos.toByteArray())
    return bin.save2MediaStoreAsImage(context, relativePath, displayName)
}

/**
 * Save the picture to gallery via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStoreAsImage(context: Context, relativePath: String = Environment.DIRECTORY_DCIM, displayName: String): Uri? {
    return this.save2MediaStore(context, displayName, relativePath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the audio to MediaStore via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStoreAsAudio(context: Context, relativePath: String = Environment.DIRECTORY_DCIM, displayName: String): Uri? {
    return this.save2MediaStore(context, displayName, relativePath, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the audio to MediaStore via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStoreAsVideo(context: Context, relativePath: String = Environment.DIRECTORY_DCIM, displayName: String): Uri? {
    return this.save2MediaStore(context, displayName, relativePath, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the media to MediaStore via InputStream
 *
 * @return The media's uri if success or null
 */
fun InputStream.save2MediaStore(context: Context, displayName: String, relativePath: String, mediaExternalUri: Uri): Uri? {
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
            } catch (e: Exception) {
                return null
            }
        }
        return uri
    } else {
        var result: Uri? = null
        val countDownLatch = CountDownLatch(1)
        val outFile = File(Environment.getExternalStorageDirectory(), relativePath + File.separator + displayName).addTimesIfExit()
        FileUtils.forceMkdirParent(outFile)
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
fun File.addTimesIfExit(times: Int = 1): File {
    return if (exists()) {
        val file = File(parentFile, "$nameWithoutExtension($times).$extension")
        if (file.exists()) {
            addTimesIfExit(times + 1)
        } else {
            file
        }
    } else {
        this
    }
}