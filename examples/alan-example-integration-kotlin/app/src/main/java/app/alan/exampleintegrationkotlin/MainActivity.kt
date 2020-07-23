package app.alan.exampleintegrationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import app.alan.basicexample.R
import app.alan.exampleintegrationkotlin.highlihts.HighlightAdapter
import app.alan.exampleintegrationkotlin.highlihts.HighlightItem
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.events.EventCommand
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Create dummy items for highlight list
    private val dummyHighlightItems =
        listOf("one", "two", "three", "four", "five")
        .map {
            HighlightItem(it, false)
        }

    //Init adapter with dummy items
    private val highlightAdapter: HighlightAdapter = HighlightAdapter(dummyHighlightItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alanConfig = AlanConfig.builder()
                .setProjectId("314203787ccd9370974f1bf6b6929c1b2e956eca572e1d8b807a3e2338fdd0dc/prod")
                .build()
        alan_button.initWithConfig(alanConfig)

        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = highlightAdapter

        //Register Alan callback to handle events from Alan Studio script
        alan_button.registerCallback(object : AlanCallback() {

            //Register command handler
            override fun onCommandReceived(eventCommand: EventCommand?) {
                super.onCommandReceived(eventCommand)

                //Extract command payload from json Object
                val commandObject = eventCommand?.data?.optJSONObject("data")
                val command = commandObject?.optString("command")
                when (command) {
                    "highlight" -> {
                        //extract item to highlight and pass it to adapter
                        val itemToHighlight = commandObject.optString("item")
                        highlightAdapter.turnHighlight(itemToHighlight)
                    }
                    else -> {
                        Log.d("Main", "Unknown command ${command}")
                    }
                }
            }
        })
    }
}