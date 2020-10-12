package app.alan.alanexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alan.alansdk.Alan
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.AlanState
import com.alan.alansdk.alanbase.ConnectionState
import com.alan.alansdk.alanbase.DialogState
import com.alan.alansdk.events.EventCommand
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init_button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        PERMISSION_REQUEST_CODE)
            } else {
                       initAlanSDK()
            }
        }

        //Use `toggle` method if you need to simulate user interaction with Alan Button.
        //It executes the same behavior if user taps the button
        voice.setOnClickListener {
            if (alanButton.sdk.isInited) {
                alanButton.sdk.toggle()
            }
        }

        val scriptMethodCallBtn = findViewById<View>(R.id.callScript)
        scriptMethodCallBtn.setOnClickListener {
            //Call method on studio side(Alan backend)
            //params should be valid json string
            val callParams = JSONObject("{\"test\":1}")
            alanButton.callProjectApi("test", callParams.toString()) { methodName, response, error ->
                if (error != null && !error.isEmpty()) {
                    Log.i("AlanResponse", "$methodName failed with: $error")
                } else {
                    Log.i("AlanResponse", "$methodName response is: $response")
                    runOnUiThread {
                        Toast.makeText(this, "Response is: $response", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAlanSDK()
        }
    }

    private fun initAlanSDK() {
        Alan.enableLogging(true)
        //Insert your project key here from "Embed code" button on the tutor.alan.app page
        //Link Alan button with sdk so it can listen to the dialog state and control voice interaction
        val config = AlanConfig.builder()
                .setProjectId("8e0b083e795c924d64635bba9c3571f42e956eca572e1d8b807a3e2338fdd0dc/stage")
                .build()
        alanButton.initWithConfig(config)

        //Register sdk listener to receive different events from sdk
        alanButton.registerCallback(MyCallback())
        Toast.makeText(this, "SDK inited successfully", Toast.LENGTH_SHORT).show()
    }

    internal inner class MyCallback : AlanCallback() {
        override fun onAlanStateChanged(alanState: AlanState) {
            super.onAlanStateChanged(alanState)
            Log.i("AlanCallback", "Alan state changed -> " + alanState.name)
        }

        override fun onCommandReceived(eventCommand: EventCommand?) {
            super.onCommandReceived(eventCommand)
            Log.i("AlanCallback", "Got command from studio ->" + eventCommand?.data)
        }
    }

    companion object {

        private val PERMISSION_REQUEST_CODE = 101
    }
}
