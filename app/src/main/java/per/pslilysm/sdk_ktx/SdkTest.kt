package per.pslilysm.sdk_ktx

import android.os.SystemClock
import android.util.Log
import com.tencent.mmkv.MMKV
import per.pslilysm.sdk_library.AppHolder
import per.pslilysm.sdk_library.extention.checkIsMainThread
import per.pslilysm.sdk_library.extention.decrypt
import per.pslilysm.sdk_library.extention.encrypt
import per.pslilysm.sdk_library.extention.pattern_0d00DecimalFormat
import per.pslilysm.sdk_library.extention.pattern_yyyyMMddHHmmssDateFormat
import per.pslilysm.sdk_library.extention.showLongToast
import per.pslilysm.sdk_library.extention.showShortToast
import per.pslilysm.sdk_library.extention.toJson
import per.pslilysm.sdk_library.extention.toMap
import per.pslilysm.sdk_library.util.concurrent.GlobalExecutors
import java.util.Date

/**
 *
 *
 * @author pslilysm
 * Created on 2022/8/16 10:25
 */
object SdkTest {

    private const val TAG = "CXD-SdkTest"

    init {
        MMKV.initialize(AppHolder.get())
    }

    fun test() {
        var value: Any = "pslilysm".encrypt()
        Log.i(TAG, "test: $value")
        value = (value as String).decrypt()
        Log.i(TAG, "test: $value")
        val json = "{\n" +
                "  \"psl\": 24,\n" +
                "  \"cxd\": 23,\n" +
                "  \"fyh\": 23\n" +
                "}"
        value = json.toMap(Int::class.java)
        (value as Map<String, Int>).forEach { (_key: String, _value: Int) ->
            Log.i(TAG, "test: key -> $_key, value -> $_value")
        }
        value = value.toJson(true)
        Log.i(TAG, "test: $value")
        Log.i(TAG, "test: " + pattern_yyyyMMddHHmmssDateFormat.format(Date()))
        Log.i(TAG, "test: " + pattern_0d00DecimalFormat.format(36.24444))
        GlobalExecutors.io.execute {
            Log.i(TAG, "test: run")
        }
        checkIsMainThread()
        GlobalExecutors.io.execute {
            "sdk test1".showShortToast()
            SystemClock.sleep(1000)
            "sdk test2".showShortToast()
            SystemClock.sleep(1000)
            "sdk test3".showShortToast()
            SystemClock.sleep(1000)
            "sdk test4".showShortToast()
            SystemClock.sleep(1000)
            "sdk test5".showLongToast()
            SystemClock.sleep(1000)
            "sdk test6".showLongToast()
        }
    }

}