package com.sun.freshfarm.disease.symptom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.sun.freshfarm.R
import kotlinx.android.synthetic.main.fragment_disease_symptom_list.*

class DiseaseSymptomListFragment : Fragment() {
    private lateinit var titleView: TextView
    private lateinit var listRV: RecyclerView
    private lateinit var messageView: TextView

    private val symptomList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_disease_symptom_list, container, false)

        initialView(rootView)

        return rootView
    }


    private fun initialView(rootView: View) {
        listRV = rootView.findViewById(R.id.diseaseSymptomListRV)
        titleView = rootView.findViewById(R.id.diseaseSymptomListTitle)
        messageView = rootView.findViewById(R.id.message)

        titleView.text = "증상 목록"

        listRV.apply {
            adapter = DiseaseSymptomAdapter(
                context,
                symptomList
            )
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun getSelectedSymptoms(): List<String> {
        val adapter = listRV.adapter as DiseaseSymptomAdapter

        val result = arrayListOf<String>()

        symptomList.mapIndexed { index, symptom ->
            if (adapter.selected[index] == true) {
                result.add(symptom)
            }
        }

        return result
    }

    fun updateTitle(title: String) {
        titleView.text = title
    }

    fun updateSymptoms(symptoms: List<String>) {
        symptomList.clear()

        symptomList.addAll(symptoms)

        listRV.adapter?.notifyDataSetChanged()
        updateView()
    }

    private fun updateView() {
        if (symptomList.size == 0) {
            messageView.text = "알려진 증상이 없습니다."
        } else {
            message.visibility = View.GONE
        }
    }
}



