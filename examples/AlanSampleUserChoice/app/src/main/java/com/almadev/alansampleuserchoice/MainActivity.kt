package com.almadev.alansampleuserchoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alanButton.initSDK("012cfb9ded14239e64635bba9c3571f42e956eca572e1d8b807a3e2338fdd0dc/stage")

        start_button.setOnClickListener {
            alanButton.sdk.call("script::choice", "{}") { method, response, error ->
                if (error.isNullOrEmpty()) {
                    val json = JSONObject(response)
                    parseAlanResponse(json)
                }
            }
        }
    }

    private fun parseAlanResponse(response: JSONObject) {
        if (response.getString("result") == "left") {
            radio_left.isChecked = true
        } else {
            radio_right.isChecked = true
        }
    }
}
