package com.airy.mypids.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.R
import com.airy.mypids.objects.Station
import com.baidu.mapapi.search.core.PoiInfo

@SuppressLint("NotifyDataSetChanged")
class StationAdapter(private val list: List<Station>, private val listener: (idx: Int) -> Unit) :
    RecyclerView.Adapter<StationAdapter.StationHolder>() {

    class StationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView

        init {
            textView = itemView.findViewById(R.id.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationHolder {
        return StationHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_left_text, parent, false)
        )
    }

    /**
     * eg: 东山总站(署前路)
     */
    override fun onBindViewHolder(holder: StationHolder, position: Int) {
        holder.textView.text = list.get(holder.adapterPosition).name
        holder.itemView.setOnClickListener { listener(holder.adapterPosition) }
    }

    override fun getItemCount(): Int = list.size
}