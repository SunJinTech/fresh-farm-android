package com.sun.freshfarm.confirmcase.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.utils.TimeUtils

class CaseListAdapter(
    private val data: List<ConfirmCase>,
    private val context: Context
) : RecyclerView.Adapter<CaseListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.unit_vertical, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        initialView(holder.itemView, position)
    }

    private fun initialView(itemView: View, position: Int) {
        val case = data[position]

        val titleView = itemView.findViewById<TextView>(R.id.title)
        val locationView = itemView.findViewById<TextView>(R.id.subtitle)
        val dateView = itemView.findViewById<TextView>(R.id.subcontent)
        val iconView = itemView.findViewById<ImageView>(R.id.icon)


        var kindImageId = Constants.convertId((Constants.toEnglish(data[position].livestockKind)))
        Constants.convertId(data[position].livestockKind)?.let {
            kindImageId = it
        }

        if (kindImageId != null) {
            iconView.setImageResource(kindImageId!!)
        }

        titleView.text = case.diseaseName

        locationView.text = case.farmLocation
        if (case.occureDate != null) {
            dateView.text = TimeUtils().toDateString(case.occureDate!!)
        }

        itemView.setOnClickListener {
            case.id?.let { it1 -> Navigator(context).startCaseDetail(it1) }
        }
    }
}
