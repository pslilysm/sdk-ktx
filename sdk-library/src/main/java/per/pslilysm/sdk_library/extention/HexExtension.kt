package per.pslilysm.sdk_library.extention

/**
 * Extension for Hex
 *
 * @author pslilysm
 * Created on 2023/06/29 14:59
 * @since 2.2.0
 */

private val DIGITS_LOWER: CharArray = charArrayOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
    'e', 'f'
)

/**
 * Used to build output as hex.
 */
private val DIGITS_UPPER = charArrayOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
    'E', 'F'
)

fun ByteArray.encodeHexString(): String {
    return String(this.encodeHex())
}

fun ByteArray.encodeHex(toLowerCase: Boolean = true): CharArray {
    return this.encodeHex(if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
}

fun ByteArray.encodeHex(toDigits: CharArray): CharArray {
    val l = this.size
    val out = CharArray(l shl 1)
    this.encodeHex(0, this.size, toDigits, out, 0)
    return out
}

private fun ByteArray.encodeHex(dataOffset: Int, dataLen: Int, toDigits: CharArray,
                                out: CharArray, outOffset: Int
) {
    // two characters form the hex value.
    var i = dataOffset
    var j = outOffset
    while (i < dataOffset + dataLen) {
        out[j++] = toDigits[0xF0 and this[i].toInt() ushr 4]
        out[j++] = toDigits[0x0F and this[i].toInt()]
        i++
    }
}