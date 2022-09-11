package com.airy.mypids.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.R
import com.baidu.mapapi.search.core.PoiInfo

@SuppressLint("NotifyDataSetChanged")
class LineSearchAdapter(list: List<PoiInfo>? = null, private val listener: (info: PoiInfo) -> Unit) :
    RecyclerView.Adapter<LineSearchAdapter.LineSearchHolder>() {
    var list: List<PoiInfo>? = list
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class LineSearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView

        init {
            textView = itemView.findViewById(R.id.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineSearchHolder {
        return LineSearchHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_left_text, parent, false)
        )
    }

    /**
     * eg: 1路(东山总站(署前路)-芳村花园南门总站)
     */
    override fun onBindViewHolder(holder: LineSearchHolder, position: Int) {
        holder.textView.text = list?.get(holder.adapterPosition)?.name
        holder.itemView.setOnClickListener { listener(list!![holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = list?.size ?: 0
}