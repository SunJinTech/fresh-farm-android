package com.sun.freshfarm.report.list

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
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.utils.TimeUtils

class ReportVerticalListAdapter(var data: ArrayList<Report>, val context: Context) :
    RecyclerView.Adapter<ReportVerticalListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var iconView: ImageView
    private lateinit var titleView: TextView
    private lateinit var locationView: TextView
    private lateinit var dateView: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.unit_vertical, parent, false)
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
        val report = data[position]

        titleView = itemView.findViewById(R.id.title)
        locationView = itemView.findViewById(R.id.subtitle)
        iconView = itemView.findViewById(R.id.icon)
        dateView = itemView.findViewById(R.id.subcontent)

//        titleView.text = report.diseaseName
//        locationView.text = report.farmLocation
//        iconView.setImageResource(Constants.convertId(report.livestockKind)!!)
//        dateView.text = TimeUtils().toDateString(report.reportTime)
//
//        itemView.setOnClickListener {
//            report.reportId?.let { it1 -> Navigator(context).startReportDetail(it1) }
//        }
        titleView.text = report.diseaseName
        locationView.text = report.reportFarmLocation
        Constants.convertId(report.diseaseCode)?.let { iconView.setImageResource(it) }
        dateView.text = TimeUtils().toDateString(report.reportTime)

        itemView.setOnClickListener {
            report.id?.let { it1 -> Navigator(context).startReportDetail(it1) }
        }
    }

}
