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
import com.alan.alansdk.BasicSdkListener
import com.alan.alansdk.alanbase.ConnectionState
import com.alan.alansdk.alanbase.DialogState
import com.alan.alansdk.events.EventCommand
import kotlinx.android.synthetic.main.activity_main.*

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

        voice.setOnClickListener {
            if (alanButton.sdk.isInited) {
                alanButton.sdk.toggle()
            }
        }

        val scriptMethodCallBtn = findViewById<View>(R.id.callScript)
        scriptMethodCallBtn.setOnClickListener {
            //Call method on studio side(Alan backend)
            //method name should starts with "script" namespace.
            //params should be valid json string
            alanButton.sdk.call("script::test", "{\"test\":1}") { methodName, response, error ->
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
        alanButton.initSDK("8e0b083e795c924d64635bba9c3571f42e956eca572e1d8b807a3e2338fdd0dc/stage")

        //Register sdk listener to receive different events from sdk
        alanButton.sdk.registerCallback(MyCallback())
        Toast.makeText(this, "Sdk inited successfully", Toast.LENGTH_SHORT).show()
    }

    internal inner class MyCallback : BasicSdkListener() {
        override fun onConnectStateChanged(connectState: ConnectionState) {
            super.onConnectStateChanged(connectState)
            Log.i("AlanCallback", "Connection state changed -> " + connectState.name)
        }

        override fun onDialogStateChanged(dialogState: DialogState) {
            super.onDialogStateChanged(dialogState)
            Log.i("AlanCallback", "Dialog state changed -> " + dialogState.name)
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
