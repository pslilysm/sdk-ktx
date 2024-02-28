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
import per.pslilysm.sdk_library.app
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.CountDownLatch

/**
 * Extension for media
 *
 * @author pslilysm
 * Created on 2023/06/29 15:09
 * @since 2.2.0
 */

interface MediaSaveErrorPolicy {

    /**
     * Run policy
     *
     * @param e the exception when save, may null
     * @return a uri after the policy proceed
     */
    fun runPolicy(e: Throwable?): Uri?

}

class IncreaseFileNameTimesPolicy(
    private val inputStream: InputStream,
    private val context: Context,
    private val displayName: String,
    private val relativePath: String,
    private val mediaExternalUri: Uri,
    private val maxSaveTimes: Int = 10
) : MediaSaveErrorPolicy {

    private var saveTimes = 0

    override fun runPolicy(e: Throwable?): Uri? {
        saveTimes++
        if (saveTimes > maxSaveTimes) {
            return null
        }
        val newName = "${displayName.substringBeforeLast(".")}($saveTimes)" +
                displayName.substringAfterLast('.', "")
        return inputStream.save2MediaStore(
            context, newName,
            relativePath, mediaExternalUri, this
        )
    }
}

fun File.save2MediaStoreAsImage(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
): Uri? {
    return try {
        FileInputStream(this).save2MediaStoreAsImage(context, relativePath, displayName)
    } catch (e: IOException) {
        null
    }
}

/**
 * Save the picture to gallery via InputStream
 *
 * @return the media's uri if success or null
 */
fun InputStream.save2MediaStoreAsImage(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
): Uri? {
    return this.save2MediaStore(context, displayName, relativePath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the audio to MediaStore via InputStream
 *
 * @return the media's uri if success or null
 */
fun InputStream.save2MediaStoreAsAudio(
    context: Context,
    relativePath: String = Environment.DIRECTORY_MUSIC,
    displayName: String,
): Uri? {
    return this.save2MediaStore(context, displayName, relativePath, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the audio to MediaStore via InputStream
 *
 * @return the media's uri if success or null
 */
fun InputStream.save2MediaStoreAsVideo(
    context: Context,
    relativePath: String = Environment.DIRECTORY_DCIM,
    displayName: String,
): Uri? {
    return this.save2MediaStore(context, displayName, relativePath, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
}

/**
 * Save the media to MediaStore via InputStream
 *
 * @return the media's uri if success or null
 */
fun InputStream.save2MediaStore(
    context: Context,
    displayName: String,
    relativePath: String,
    mediaExternalUri: Uri,
    mediaSaveErrorPolicy: MediaSaveErrorPolicy? = IncreaseFileNameTimesPolicy(
        this, context, displayName, relativePath, mediaExternalUri
    )
): Uri? {
    throwIfMainThread()
    val contentResolver = context.contentResolver
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val uri = contentResolver.insert(mediaExternalUri, ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
            put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        })
        uri?.let {
            try {
                contentResolver.openOutputStream(uri)?.use { os ->
                    this.copyTo(os)
                }
            } catch (ex: Exception) {
                return mediaSaveErrorPolicy?.runPolicy(ex)
            }
        }
        return uri ?: mediaSaveErrorPolicy?.runPolicy(null)
    } else {
        var result: Uri? = null
        val countDownLatch = CountDownLatch(1)
        val outFile = File(Environment.getExternalStorageDirectory(), relativePath + File.separator + displayName)
        if (outFile.parentFile?.exists() != true) {
            outFile.parentFile?.mkdirs()
        }
        if (outFile.exists()) {
            return mediaSaveErrorPolicy?.runPolicy(FileAlreadyExistsException(outFile))
        }
        try {
            FileOutputStream(outFile).use { fos ->
                this.copyTo(fos)
            }
            MediaScannerConnection.scanFile(context.applicationContext, arrayOf(outFile.absolutePath), null) { _, uri ->
                result = uri
                countDownLatch.countDown()
            }
        } catch (ex: Exception) {
            return mediaSaveErrorPolicy?.runPolicy(ex)
        }
        countDownLatch.await()
        return result
    }
}

/**
 * @return a new cache file copied by the uri
 */
@Throws(IOException::class)
fun Uri.copyToNewCacheFile(fileSuffix: String? = null): File {
    throwIfMainThread()
    app.openInputStreamNotNull(this).use { `is` ->
        var fileName = SystemClock.elapsedRealtimeNanos().toString()
        if (fileSuffix != null) {
            fileName += fileSuffix
        }
        val newCacheFile = File(app.cacheDir, fileName)
        FileOutputStream(newCacheFile).use {  fos ->
            `is`.copyTo(fos)
        }
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