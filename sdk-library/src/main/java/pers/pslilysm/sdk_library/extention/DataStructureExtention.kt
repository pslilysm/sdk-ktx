package pers.pslilysm.sdk_library.extention

/**
 * Extension for data structure
 *
 * @author cxd
 * Created on 2023/06/29 16:16
 * @since 2.2.0
 */

fun Collection<*>?.haveElement(): Boolean {
    return !isNullOrEmpty()
}

fun Map<*, *>?.haveElement(): Boolean {
    return !isNullOrEmpty()
}

fun BooleanArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun ByteArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun CharArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun ShortArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun IntArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun FloatArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun LongArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun DoubleArray?.haveElement(): Boolean {
    return this != null && isNotEmpty()
}

fun <T> Array<T>?.haveElement(): Boolean {
    return !this.isNullOrEmpty()
}