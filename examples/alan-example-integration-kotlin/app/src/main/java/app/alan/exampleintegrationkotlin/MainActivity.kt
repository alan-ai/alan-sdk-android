package app.alan.exampleintegrationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import app.alan.basicexample.R
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.events.EventCommand
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alanConfig = AlanConfig.builder()
                .setProjectId("314203787ccd9370974f1bf6b6929c1b2e956eca572e1d8b807a3e2338fdd0dc/prod")
                .build()
        alan_button.initWithConfig(alanConfig)

        alan_button.registerCallback(
            object : AlanCallback() {
                override fun onCommandReceived(eventCommand: EventCommand?) {
                    super.onCommandReceived(eventCommand)
                    eventCommand?.data?.let {
                        if (it.optString("command") == "navigation") {
                            if (it.optString("route") == "forward") {
                                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment).navigate(R.id.action_global_screen2)
                            } else {
                                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment).navigate(R.id.action_global_screen1)
                            }
                        }
                    }
                }
            }
        )
    }
}