package app.alan.exampleintegrationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import app.alan.basicexample.R
import app.alan.exampleintegrationkotlin.highlihts.HighlightAdapter
import app.alan.exampleintegrationkotlin.highlihts.HighlightItem
import com.alan.alansdk.Alan
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.events.EventCommand
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

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

        Alan.enableLogging(true)
        val alanConfig = AlanConfig.builder()
                .setProjectId("619761b4c85efe3364635bba9c3571f42e956eca572e1d8b807a3e2338fdd0dc/stage")
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
                        val idx = highlightAdapter.turnHighlight(itemToHighlight)
                        rvList.scrollToPosition(idx)
                    }
                    else -> {
                        Log.d("Main", "Unknown command ${command}")
                    }
                }
            }
        })

        val visualState = JSONObject()
        visualState.put("screen", "second")
        val itemJsonArray = JSONArray()
        dummyHighlightItems.forEach {
            itemJsonArray.put(it.text)
        }
        visualState.put("items", itemJsonArray)
        alan_button.setVisualState(visualState.toString())
    }
}