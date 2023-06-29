package perx.pslilysm.sdk_ktx

import android.os.SystemClock
import android.util.Log
import com.tencent.mmkv.MMKV
import pers.pslilysm.sdk_library.AppHolder
import pers.pslilysm.sdk_library.util.*
import pers.pslilysm.sdk_library.util.MMKVExtensions.decode
import pers.pslilysm.sdk_library.util.concurrent.GlobalExecutors
import java.util.*

/**
 *
 *
 * @author cxd
 * Created on 2022/8/16 10:25
 */
object SdkTest {

    private const val TAG = "CXD-SdkTest"

    init {
        MMKV.initialize(AppHolder.get())
    }

    fun test() {
        var value: Any = AesUtil.encrypt("pslilysm")
        Log.i(TAG, "test: $value")
        value = AesUtil.decrypt(value as String)
        Log.i(TAG, "test: $value")
        val json = "{\n" +
                "  \"psl\": 24,\n" +
                "  \"cxd\": 23,\n" +
                "  \"fyh\": 23\n" +
                "}"
        value = GsonUtil.jsonToMap(json, Int::class.java)
        (value as Map<String, Int>).forEach { _key: String, _value: Int ->
            Log.i(TAG, "test: key -> $_key, value -> $_value")
        }
        value = GsonUtil.objToJson(value, true)
        Log.i(TAG, "test: $value")
        MMKV.defaultMMKV().encode("key", 12)
        value = MMKV.defaultMMKV().decode("key", 123)
        Log.i(TAG, "test: $value")
        MMKV.defaultMMKV().encode("key", "abc")
        value = MMKV.defaultMMKV().decode("key", "aa")
        Log.i(TAG, "test: $value")
        MMKV.defaultMMKV().encode("key", 3F)
        value = MMKV.defaultMMKV().decode("key", 4F)
        Log.i(TAG, "test: $value")
        MMKV.defaultMMKV().encode("key", 4L)
        value = MMKV.defaultMMKV().decode("key", 5L)
        Log.i(TAG, "test: $value")
        MMKV.defaultMMKV().encode("key", true)
        value = MMKV.defaultMMKV().decode("key", false)
        Log.i(TAG, "test: $value")
        val set = mutableSetOf<String>()
        set.add("1")
        set.add("2")
        MMKV.defaultMMKV().encode("key", set)
        value = MMKV.defaultMMKV().decode("key", HashSet<String>())
        Log.i(TAG, "test: $value")
        Log.i(TAG, "test: " + FormatterUtil.getyyyyMMddHHmmssFormatter().format(Date()))
        Log.i(TAG, "test: " + FormatterUtil.get_0dot00Formatter().format(36.24444))
        GlobalExecutors.io().execute {
            Log.i(TAG, "test: run")
        }
        ThreadUtil.checkIsMainThread()
        GlobalExecutors.io().execute {
            ToastUtil.showShort("sdk test")
            SystemClock.sleep(1000)
            ToastUtil.showShort("sdk test1")
            SystemClock.sleep(1000)
            ToastUtil.showShort("sdk test2")
            SystemClock.sleep(1000)
            ToastUtil.showShort("sdk test3")
            SystemClock.sleep(1000)
            ToastUtil.showLong("sdk test4")
            SystemClock.sleep(1000)
            ToastUtil.showLong("sdk test5")
        }
    }

}