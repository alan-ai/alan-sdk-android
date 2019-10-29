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
import com.alan.alansdk.button.AlanButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var alanButton: AlanButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init_button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        PERMISSION_REQUEST_CODE)
            } else {
                       initAlanSDK();
            }
        }

        /**You should also listen to the
         * [ to update visual state according to the dialog state][com.alan.alansdk.AlanCallback.onDialogStateChanged]
         */
        //        View voiceBtn = findViewById(R.id.voice);
        //        voiceBtn.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                if (sdk != null && sdk.isInited()) {
        //                    sdk.toggle();
        //                }
        //            }
        //        });

        val scriptMethodCallBtn = findViewById<View>(R.id.callScript)
        scriptMethodCallBtn.setOnClickListener {
            alanButton!!.sdk.call("script::test", "{\"test\":1}") { methodName, response, error ->
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
        //        sdk = Alan.getInstance();
        //
        //        Insert your project key here from "Embed code" button on the tutor.alan.app page
        //        sdk.init("8e0b083e795c924d64635bba9c3571f42e956eca572e1d8b807a3e2338fdd0dc/stage");
        //
        //        sdk.registerCallback(new MyCallback());
        //
        //        Link Alan button with sdk so it can listen to the dialog state and control voice interaction
        alanButton = findViewById(R.id.alanBtn)
        alanButton!!.initSDK("wss://studio.alan-stage.app",
                "bb42ab84666368c2e1b9fb493a3ca1dc2e956eca572e1d8b807a3e2338fdd0dc/stage",
                null)

        alanButton?.sdk?.registerCallback(MyCallback())
        alanButton?.sdk?.record()
        Toast.makeText(this, "Sdk inited successfully", Toast.LENGTH_SHORT).show()
    }

    internal inner class MyCallback : BasicSdkListener() {
        override fun onConnectStateChanged(connectState: ConnectionState) {
            super.onConnectStateChanged(connectState)
            Log.i("AlanCallback", "Connection state changed -> " + connectState.name)
        }

        override fun onDialogStateChanged(dialogState: DialogState) {
            super.onDialogStateChanged(dialogState)
            if (dialogState == DialogState.IDLE) {
                alanButton?.sdk?.record()
            }
        }
    }

    companion object {

        private val PERMISSION_REQUEST_CODE = 101
    }
}
