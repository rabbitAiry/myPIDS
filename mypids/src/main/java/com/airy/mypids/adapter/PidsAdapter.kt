package com.airy.mypids.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.R
import com.airy.mypids.pids.BasePidsFragment
import com.airy.mypids.utils.ResUtil
import kotlin.reflect.KClass

class PidsAdapter(
    private val list: List<String>,
    private val context: Context
) :
    RecyclerView.Adapter<PidsAdapter.PidsHolder>() {
    private var selected = 0

    class PidsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PidsHolder {
        val view = TextView(context)
        val params = ViewGroup.MarginLayoutParams(dp2px(120), ViewGroup.LayoutParams.MATCH_PARENT)
        params.setMargins(dp2px(10))
        view.layoutParams = params
        view.gravity = Gravity.CENTER_VERTICAL
        view.background = ResUtil.getDrawable(R.drawable.outline_button_bg, context)
        view.setPadding(dp2px(10))
        view.setTextColor(Color.WHITE)
        return PidsHolder(view)
    }

    override fun onBindViewHolder(holder: PidsHolder, position: Int) {
        val view = holder.itemView as TextView
        view.text = list[holder.adapterPosition]
        view.isSelected = selected == holder.adapterPosition
        view.setOnClickListener{
            val oldSelect = selected
            selected = holder.adapterPosition
            view.isSelected = true
            notifyItemChanged(oldSelect)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getSelectedStyleText() = list[selected]

    private fun dp2px(dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}