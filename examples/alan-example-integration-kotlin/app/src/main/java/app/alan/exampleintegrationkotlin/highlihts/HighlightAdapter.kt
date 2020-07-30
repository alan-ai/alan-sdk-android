package app.alan.exampleintegrationkotlin.highlihts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.alan.basicexample.R
import kotlinx.android.synthetic.main.item_highlight.view.*

/**
 * Prepare adapter and corresponding classes to show list of items to be highlighted
 */

/**
 * Model data class which represents item state
 */
data class HighlightItem(
    val text: String,
    var isHighlighted: Boolean
)

class HighlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: HighlightItem) {
        itemView.tvText.text = item.text

        // Change card background color if item is highlighted
        if (item.isHighlighted) {
            itemView.setBackgroundColor(itemView.resources.getColor(R.color.colorPrimary))
        } else {
            itemView.setBackgroundColor(itemView.resources.getColor(android.R.color.white))
        }
    }
}

class HighlightAdapter(
    private val items: List<HighlightItem>
) : RecyclerView.Adapter<HighlightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_highlight, parent, false)
        return HighlightViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HighlightViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun turnHighlight(item: String): Int {
        val idx = items.indexOfFirst {
            it.text == item
        }

        //dehighlight all items except picked one
        items.forEachIndexed { index, highlightItem ->
          highlightItem.isHighlighted = index == idx
        }
        notifyDataSetChanged()
        return idx
    }
}