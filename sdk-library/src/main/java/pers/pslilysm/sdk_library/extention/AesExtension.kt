package pers.pslilysm.sdk_library.extention

import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Extension for AES
 *
 * @author cxd
 * Created on 2023/06/29 15:57
 * @since 2.2.0
 */

private const val sAesMode = "AES/CFB/NOPadding"
private const val sAesKey = "NV9MCANO5VVCMUASPSLILYSM19990127"
private const val sIvKey = "PSLILYSM19990127"

private val sDefaultEncryptCipher by lazy {
    try {
        val cipher = Cipher.getInstance(sAesMode)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(sAesKey.toByteArray(), "AES"),
            IvParameterSpec(
                sIvKey.toByteArray()
            )
        )
        cipher
    } catch (ex: Exception) {
        throw ex.rethrow()
    }
}

private val sDefaultDecryptCipher by lazy {
    try {
        val cipher = Cipher.getInstance(sAesMode)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(sAesKey.toByteArray(), "AES"),
            IvParameterSpec(
                sIvKey.toByteArray()
            )
        )
        cipher
    } catch (ex: Exception) {
        throw ex.rethrow()
    }
}

/**
 * Encrypt the string with default aes config
 *
 * @return A encrypted Base64 String
 */
fun String.encrypt(): String {
    return try {
        val bytes = sDefaultEncryptCipher.doFinal(toByteArray(StandardCharsets.UTF_8))
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        throw e.rethrow()
    }
}

/**
 * Decrypt the string with default aes config
 *
 * @return A decrypted Base64 String
 */
fun String.decrypt(): String {
    return try {
        String(sDefaultDecryptCipher.doFinal(Base64.decode(this, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    } catch (e: Exception) {
        throw e.rethrow()
    }
}

/**
 * Encrypt the string with given config
 *
 * @param mode The name of the cipher mode, e.g.,
 * <i>DES/CBC/PKCS5Padding</i>.
 * @param secretKey The secret key
 * @param ivKey The iv key
 * @return A encrypted Base64 String
 */
fun String.encrypt(mode: String, secretKey: String, ivKey: String): String {
    return try {
        val cipher = Cipher.getInstance(mode)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(secretKey.toByteArray(), "AES"),
            IvParameterSpec(ivKey.toByteArray())
        )
        val bytes = cipher.doFinal(toByteArray(StandardCharsets.UTF_8))
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        throw e.rethrow()
    }
}

/**
 * Decrypt the string with given config
 *
 * @param mode The name of the cipher mode, e.g.,
 * <i>DES/CBC/PKCS5Padding</i>.
 * @param secretKey The secret key
 * @param ivKey The iv key
 * @return A decrypted Base64 String
 */
fun String.decrypt(mode: String, secretKey: String, ivKey: String): String {
    return try {
        val cipher = Cipher.getInstance(mode)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(secretKey.toByteArray(), "AES"),
            IvParameterSpec(ivKey.toByteArray())
        )
        String(cipher.doFinal(Base64.decode(this, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    } catch (e: Exception) {
        throw e.rethrow()
    }
}