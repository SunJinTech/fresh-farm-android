package com.sun.freshfarm.alert

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Alert
import kotlinx.android.synthetic.main.fragment_alert_unit.view.*

class AlertListAdapter(val data: ArrayList<Alert>) :
    RecyclerView.Adapter<AlertListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_alert_unit, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.itemView.alertMessageText.text = data[position].message
//        holder.itemView.alertKindIcon.setImageResource(Constants.convertId(data[position].kind)!!)

//        when (data[position].level) {
//            "WARNING" -> {
//                holder.itemView.setBackgroundColor(Color.RED)
//            }
//            "CAUTION" -> {
//                holder.itemView.setBackgroundColor(Color.YELLOW)
//            }
//            "INFORMATION" -> {
//                holder.itemView.setBackgroundColor(R.color.colorNormal)
//            }
//        }

    }
}