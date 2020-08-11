package com.sun.freshfarm.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.utils.TimeUtils
import kotlinx.android.synthetic.main.unit_search_confirm.view.*

class ReportCaseSearchAdapter(
    private val report_data: List<Report>, private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var iconView: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_vertical_sm, parent, false)
        )

    override fun getItemCount(): Int = report_data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = report_data[position]

        holder.itemView.search_confirm_item_title.text = data.diseaseCode
        holder.itemView.search_confirm_item_date.text = TimeUtils().toDateString(data.reportTime!!)
        holder.itemView.search_confirm_item_location.text = data.reportedUid
        val kindEng = Constants.toEnglish(data.livestockKinds[0])
        Constants.convertId(kindEng)?.let {
            holder.itemView.search_confirm_item_icon.setImageResource(
                it
            )
        }


    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}