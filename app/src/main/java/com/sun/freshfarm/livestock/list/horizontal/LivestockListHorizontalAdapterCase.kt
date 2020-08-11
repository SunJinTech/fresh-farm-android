package com.sun.freshfarm.livestock.list.horizontal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Livestock
import kotlinx.android.synthetic.main.unit_livestock_icon.view.*

class LivestockListHorizontalAdapterCase(
    private val data: List<Livestock>,
    private val context: Context
) :
    RecyclerView.Adapter<LivestockListHorizontalAdapterCase.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.unit_livestock_icon, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        initialView(holder.itemView, position)
    }

    private fun initialView(itemView: View, position: Int) {
        val livestock = data[position]

        itemView.iconImage?.setImageResource(Constants.convertId(livestock.kind)!!)
        itemView.kind?.text = Constants.toKorean(livestock.kind)

        if (livestock.count > 0) {
            itemView.count?.text = "${livestock.count}마리"
        } else {
            itemView.icon.setBackgroundResource(R.drawable.background_primary_round_bolder)
            itemView.kind.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))

            itemView.iconImage.setColorFilter(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )

            itemView.count?.text = "안길러요"
            itemView.count.setTextColor(ContextCompat.getColor(context, R.color.greyLight))
//            itemView.count.textSize = context.resources.getDimension(R.dimen.text_sm)
        }

//       TODO(GET ALERT DATA and update view)
        itemView.alert?.visibility = View.GONE

//        TODO(NAVIGATE TO CASE LIST ACTIVITY AND FILTER WITH SELECTED LIVESTOCK
        itemView.setOnClickListener {
            Navigator(context).startLivestockDetail(livestock.kind)
        }
    }
}
