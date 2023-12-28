package per.pslilysm.sdk_library.extention

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Extension for format
 *
 * @author pslilysm
 * Created on 2023/06/29 14:54
 * @since 2.2.0
 */

const val TB_SIZE = 1024f * 1024 * 1024 * 1024

const val GB_SIZE = 1024f * 1024 * 1024

const val MB_SIZE = 1024f * 1024

const val KB_SIZE = 1024

private val dateFormatTLS: ThreadLocal<SimpleDateFormat> by lazy {
    object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("", Locale.getDefault())
        }
    }
}

private val decimalFormatTLS: ThreadLocal<DecimalFormat> by lazy {
    object : ThreadLocal<DecimalFormat>() {
        override fun initialValue(): DecimalFormat {
            return DecimalFormat()
        }
    }
}

val simpleDateFormat: SimpleDateFormat get() = dateFormatTLS.get() as SimpleDateFormat
val decimalFormat: DecimalFormat get() = decimalFormatTLS.get() as DecimalFormat

val pattern_yyyyMMddHHmmssDateFormat: SimpleDateFormat
    get() = simpleDateFormat.apply {
        applyPattern("yyyy-MM-dd HH-mm-ss")
    }

val pattern_MMddDateFormat: SimpleDateFormat
    get() = simpleDateFormat.apply {
        applyPattern("MM-dd")
    }

val pattern_HHmmssDateFormat: SimpleDateFormat
    get() = simpleDateFormat.apply {
        applyPattern("HH:mm:ss")
    }

val pattern_0d000DecimalFormat: DecimalFormat
    get() = decimalFormat.apply {
        applyPattern("0.000")
    }

val pattern_0d00DecimalFormat: DecimalFormat
    get() = decimalFormat.apply {
        applyPattern("0.00")
    }

val pattern_0d0DecimalFormat: DecimalFormat
    get() = decimalFormat.apply {
        applyPattern("0.0")
    }

/**
 * @return A formatted file size string with adaptive pattern
 */
fun Long.autoFormatFileSize(): String {
    return if (this > TB_SIZE) {
        (this / (TB_SIZE)).let {
            it.getAdaptiveFormatter().format(it)
        } + " TB"
    } else if (this > GB_SIZE) {
        (this / (GB_SIZE)).let {
            it.getAdaptiveFormatter().format(it)
        } + " GB"
    } else if (this > MB_SIZE) {
        (this / (MB_SIZE)).let {
            it.getAdaptiveFormatter().format(it)
        } + " MB"
    } else if (this > KB_SIZE) {
        (this / (KB_SIZE)).toFloat().let {
            it.getAdaptiveFormatter().format(it)
        } + " KB"
    } else {
        "$this B"
    }
}

private fun Float.getAdaptiveFormatter(): DecimalFormat {
    return if (this >= 100) {
        decimalFormat.apply {
            applyPattern("0")
        }
    } else if (this >= 10) {
        decimalFormat.apply {
            applyPattern("0.#")
        }
    } else {
        decimalFormat.apply {
            applyPattern("0.##")
        }
    }
}

/**
 * @return A formatted media duration string with pattern "HHmmss"
 */
fun Long.formatMediaDurationWithHHmmss(): String {
    var durationSec = this / 1000
    val sb = StringBuilder()
    if (durationSec >= 60 * 60) {
        val hour = durationSec / 60 / 60
        if (hour < 10) {
            sb.append("0").append(hour).append(":")
        } else {
            sb.append(hour).append(":")
        }
        // 取余数
        durationSec %= (60 * 60)
    } else {
        sb.append("00:")
    }
    if (durationSec >= 60) {
        val minute = durationSec / 60
        if (minute < 10) {
            sb.append("0").append(minute).append(":")
        } else {
            sb.append(minute).append(":")
        }
        // 取余数
        durationSec %= 60
    } else {
        sb.append("00:")
    }
    if (durationSec < 10) {
        sb.append("0").append(durationSec)
    } else {
        sb.append(durationSec)
    }
    return sb.toString()
}

/**
 * @return A formatted media duration string with pattern "mmssSSS"
 */
fun Long.formatMediaDurationWithmmssSSS(): String {
    var durationSec = this / 1000
    val sb = StringBuilder()
    if (durationSec >= 60) {
        val minutes = durationSec / 60
        if (minutes < 10) {
            sb.append("0").append(minutes).append(":")
        } else {
            sb.append(minutes).append(":")
        }
        // 取余数
        durationSec %= 60
    } else {
        sb.append("00:")
    }
    if (durationSec < 10) {
        sb.append("0").append(durationSec)
    } else {
        sb.append(durationSec)
    }
    val milliSeconds = this % 1000
    if (milliSeconds >= 100) {
        sb.append(".").append(milliSeconds)
    } else if (milliSeconds >= 10) {
        sb.append(".0").append(milliSeconds)
    } else {
        sb.append(".00").append(milliSeconds)
    }
    return sb.toString()
}

/**
 * @return A formatted media duration string with pattern "mmss"
 */
fun Long.formatMediaDurationWithmmss(): String {
    var durationSec = this / 1000
    val sb = java.lang.StringBuilder()
    if (durationSec >= 60) {
        val minutes = durationSec / 60
        if (minutes < 10) {
            sb.append("0").append(minutes).append(":")
        } else {
            sb.append(minutes).append(":")
        }
        // 取余数
        durationSec %= 60
    } else {
        sb.append("00:")
    }
    if (durationSec < 10) {
        sb.append("0").append(durationSec)
    } else {
        sb.append(durationSec)
    }
    return sb.toString()
}