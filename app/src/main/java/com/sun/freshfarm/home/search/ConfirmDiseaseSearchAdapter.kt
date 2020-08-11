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
import com.sun.freshfarm.utils.TimeUtils
import kotlinx.android.synthetic.main.unit_search_confirm.view.*

class ConfirmDiseaseSearchAdapter(
    private val confirm_data: List<ConfirmCase>, private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var iconView: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_search_confirm, parent, false)
        )

    override fun getItemCount(): Int = confirm_data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = confirm_data[position]

        holder.itemView.search_confirm_item_title.text = data.diseaseName
        holder.itemView.search_confirm_item_date.text = TimeUtils().toDateString(data.occureDate!!)
        holder.itemView.search_confirm_item_location.text = data.farmLocation
        val kindEng = Constants.toEnglish(data.livestockKind)
        Constants.convertId(kindEng)?.let {
            holder.itemView.search_confirm_item_icon.setImageResource(
                it
            )
        }


    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}