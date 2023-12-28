package per.pslilysm.sdk_library.extention

/**
 * Extension for exception
 *
 * @author pslilysm
 * Created on 2023/06/29 16:24
 * @since 2.2.0
 */

/**
 * Get thr root cause of the `throwable`
 *
 * @return the root cause
 */
fun Throwable.getRootCause(): Throwable {
    var cause = this
    while (cause.cause != null) {
        cause = cause.cause!!
    }
    return cause
}

/**
 * Rethrow a throwable as a [RuntimeException]
 *
 * @return a RuntimeException wrapped the original throwable
 */

fun Throwable.rethrow(): RuntimeException {
    return if (this is RuntimeException) {
        this
    } else {
        RuntimeException(this)
    }
}