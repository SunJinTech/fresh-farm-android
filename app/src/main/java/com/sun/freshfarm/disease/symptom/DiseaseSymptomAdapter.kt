package com.sun.freshfarm.disease.symptom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.R

class DiseaseSymptomAdapter(
    val context: Context,
    val data: List<String>
) :
    RecyclerView.Adapter<DiseaseSymptomAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    val selected = hashMapOf<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.symptom_unit, parent, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        initialView(holder.itemView, position)
//        holder.itemView.diseaseSymptonUnitCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            onClickAction.onCheckChange(position, isChecked)
//        }
//        holder.itemView.diseaseSymptomUnitTitle.text = data[position]
    }

    private fun initialView(rootView: View, position: Int) {
        val contentView = rootView.findViewById<TextView>(R.id.content)

        val symptom = data[position]

        contentView.text = symptom

        rootView.setOnClickListener {
            doSelect(position) { updateView(rootView, position) }
        }
    }

    private fun updateView(rootView: View, position: Int) {
        val bodyView = rootView.findViewById<ConstraintLayout>(R.id.body)
        val contentView = rootView.findViewById<TextView>(R.id.content)
        val checkedView = rootView.findViewById<CheckBox>(R.id.checked)

        if (selected[position] == true) {
            bodyView.setBackgroundResource(R.drawable.background_primary_round)
            contentView.setTextColor(Color.WHITE)

            checkedView.isChecked = true
            checkedView.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        } else {
            bodyView.setBackgroundResource(R.drawable.background_lightgrey_round_bolder_sm)
            contentView.setTextColor(ContextCompat.getColor(context, R.color.greyLight))

            checkedView.isChecked = false
            checkedView.buttonTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLight))
        }
    }

    private fun doSelect(position: Int, callBack: () -> Unit) {
        if (selected[position] == null) {
            selected[position] = true
        } else {
            selected[position] = !selected[position]!!
        }
        callBack()
    }

    interface OnClickAction {
        fun onCheckChange(position: Int, isChecked: Boolean)
    }
}