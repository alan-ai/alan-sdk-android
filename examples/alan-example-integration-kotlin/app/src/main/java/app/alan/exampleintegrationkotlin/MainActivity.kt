package app.alan.exampleintegrationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.alan.basicexample.R
import com.alan.alansdk.AlanConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alanConfig = AlanConfig.builder()
                .setProjectId("314203787ccd9370974f1bf6b6929c1b2e956eca572e1d8b807a3e2338fdd0dc/prod")
                .build()
        alan_button.initWithConfig(alanConfig)
    }
}