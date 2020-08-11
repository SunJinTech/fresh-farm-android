package com.sun.freshfarm.disease.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfaram.utils.DiseaseUtils
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Disease
import com.sun.freshfarm.database.GetDataCallBack
import org.jetbrains.anko.support.v4.toast


class DiseaseListHorizontalFragment : Fragment() {

    private val diseaseList = arrayListOf<Disease>(
//        Disease(diseaseName = "낭충봉아부패병"),
//        Disease(diseaseName = "낭충봉아부패병"),
//        Disease(diseaseName = "낭충봉아부패병"),
//        Disease(diseaseName = "낭충봉아부패병"),
//        Disease(diseaseName = "낭충봉아부패병"),
//        Disease(diseaseName = "낭충봉아부패병")
    )

    private lateinit var listRV: RecyclerView
    private lateinit var messageView: TextView
    private lateinit var reportMissing: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_disease_list_horizontal, container, false)

        listRV = rootView.findViewById(R.id.listRV)
        messageView = rootView.findViewById(R.id.message)
        reportMissing = rootView.findViewById(R.id.reportMissing)

        val kind = arguments?.getString("kind")

        initialView(kind)

        return rootView
    }

    private fun initialView(kind: String?) {
        listRV.apply {
            adapter =
                DiseaseListHorizontalAdapter(diseaseList, context)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        DiseaseUtils().getDiseases(kind, DiseasesCallBack())


    }

    inner class DiseasesCallBack : GetDataCallBack<List<Disease>> {
        override fun onDataReceived(data: List<Disease>) {
            messageView.visibility = View.GONE

            diseaseList.clear()
            diseaseList.addAll(data)

            listRV.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            messageView.text = message
        }

    }

}