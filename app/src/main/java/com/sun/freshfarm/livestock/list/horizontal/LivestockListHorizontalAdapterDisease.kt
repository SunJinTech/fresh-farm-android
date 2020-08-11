package com.sun.freshfarm.livestock.list.horizontal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Livestock

class LivestockListHorizontalAdapterDisease(
    private val data: List<Livestock>,
    private val targets: List<String>?,
    private val context: Context,
    private val callBack: LivestockListHorizontalFragmentDisease.SelectCallBack
) :
    RecyclerView.Adapter<LivestockListHorizontalAdapterDisease.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var iconContainer: ConstraintLayout
    private lateinit var iconView: ImageView
    private lateinit var kindView: TextView
    private lateinit var countView: TextView
    private lateinit var alertView: ImageView

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

        iconView = itemView.findViewById(R.id.iconImage)
        Constants.convertId(livestock.kind)?.let { iconView.setImageResource(it) }

        kindView = itemView.findViewById(R.id.kind)
        kindView.text = Constants.toKorean(livestock.kind)

        countView = itemView.findViewById(R.id.count)
        countView.text = "신고대상"


        iconContainer = itemView.findViewById(R.id.icon)
        targets?.let {
            if (!it.contains(livestock.kind)) {
                iconContainer.setBackgroundResource(R.drawable.background_primary_round_bolder)
                iconView.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )

                kindView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))


                countView.text = "해당없음"
                countView.setTextColor(ContextCompat.getColor(context, R.color.greyLight))
            }
        }

        alertView = itemView.findViewById(R.id.alert)
        alertView.visibility = View.GONE

        itemView.setOnClickListener {
            callBack.onSelect(livestock.kind)
        }
    }
}