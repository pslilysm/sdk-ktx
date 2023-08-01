package pers.pslilysm.sdk_library.extention

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Extension for AES
 *
 * @author pslilysm
 * Created on 2023/06/29 15:57
 * @since 2.2.0
 */

private const val aesMode = "AES/CFB/NOPadding"
private const val aesKey = "NV9MCANO5VVCMUASPSLILYSM19990127"
private const val ivKey = "PSLILYSM19990127"

private val sDefaultEncryptCipher by lazy {
    try {
        val cipher = Cipher.getInstance(aesMode)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(aesKey.toByteArray(), "AES"),
            IvParameterSpec(
                ivKey.toByteArray()
            )
        )
        cipher
    } catch (ex: Exception) {
        throw ex.rethrow()
    }
}

private val sDefaultDecryptCipher by lazy {
    try {
        val cipher = Cipher.getInstance(aesMode)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(aesKey.toByteArray(), "AES"),
            IvParameterSpec(
                ivKey.toByteArray()
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
 * @param key The encryption key
 * @param params The algorithm parameters
 * @return A encrypted Base64 String
 */
fun String.encrypt(mode: String, key: Key, params: AlgorithmParameterSpec): String {
    return try {
        val cipher = Cipher.getInstance(mode)
        cipher.init(Cipher.ENCRYPT_MODE, key, params)
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
 * @param key The encryption key
 * @param params The algorithm parameters
 * @return A decrypted Base64 String
 */
fun String.decrypt(mode: String, key: Key, params: AlgorithmParameterSpec): String {
    return try {
        val cipher = Cipher.getInstance(mode)
        cipher.init(Cipher.DECRYPT_MODE, key, params)
        String(cipher.doFinal(Base64.decode(this, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    } catch (e: Exception) {
        throw e.rethrow()
    }
}