package per.pslilysm.sdk_ktx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *
 *
 * @author caoxuedong
 * Created on 2023/12/28 17:44
 * @since
 */
class SecondActivity: AppCompatActivity(R.layout.activity_second) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SdkTest.test(this)
    }

}