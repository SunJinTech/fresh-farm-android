package com.sun.freshfarm.disease.list

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
import com.sun.freshfarm.database.Disease

class DiseaseListHorizontalAdapter(
    val data: List<Disease>,
    val context: Context
) :
    RecyclerView.Adapter<DiseaseListHorizontalAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.unit_card_short, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disease = data[position]
        initialView(holder.itemView, disease)
    }

    private fun initialView(itemView: View, disease: Disease) {
        val nameView = itemView.findViewById<TextView>(R.id.title)
        val countView = itemView.findViewById<TextView>(R.id.subtitle)
        val iconView = itemView.findViewById<ImageView>(R.id.icon)

        val count = disease.recentCount


        nameView.text = disease.diseaseName
        countView.text = "최근 확진 : ${count}회"

        if (disease.targetKinds.isNullOrEmpty()) {
//            TODO : NEED QUESTION MARK DRAWABLE AND CHANGE TO DAT
        } else {
            val kind = Constants.toEnglish(disease.targetKinds!![0])
            iconView.setImageDrawable(Constants.convertId(kind)?.let { context.getDrawable(it) })
        }

        itemView.setOnClickListener {
            Navigator(context).startDiseaseDetail(disease.diseaseCode)
        }

    }
}