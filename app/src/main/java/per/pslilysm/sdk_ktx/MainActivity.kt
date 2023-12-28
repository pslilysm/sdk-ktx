package per.pslilysm.sdk_ktx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import per.pslilysm.sdk_ktx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            this.activity = this@MainActivity
        }.also {
            setContentView(it.root)
        }
    }

    fun gotoSecond() {
        startActivity(Intent(this, SecondActivity::class.java))
    }

}