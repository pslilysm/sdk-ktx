package per.pslilysm.sdk_ktx

import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mmkv.MMKV
import per.pslilysm.sdk_library.application
import per.pslilysm.sdk_library.defaultEventHandler

/**
 *
 *
 * @author pslilysm
 * Created on 2022/8/16 10:25
 */
object SdkTest {

    private const val TAG = "CXD-SdkTest"

    init {
        MMKV.initialize(application)
    }

    fun test(activity: AppCompatActivity) {
        defaultEventHandler.registerEvent(activity, 10) {
            Log.d(TAG, "consumer() called -> message =  $it")
        }
        defaultEventHandler.sendMessage(Message.obtain(null, 10, 1, 2, "3"))
        defaultEventHandler.sendEventOpted(Message.obtain(defaultEventHandler, 10, 11, 22, "33"))
    }

}