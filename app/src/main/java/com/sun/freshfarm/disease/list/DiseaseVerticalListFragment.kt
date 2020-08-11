package com.sun.freshfarm.disease.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.freshfaram.utils.DiseaseUtils
import com.sun.freshfarm.Constants

import com.sun.freshfarm.R
import com.sun.freshfarm.database.Disease
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import kotlinx.android.synthetic.main.fragment_disease_list.*
import org.jetbrains.anko.support.v4.toast

class DiseaseVerticalListFragment(private val livestock: Livestock?) : Fragment() {
    private var diseaseList = arrayListOf<Disease>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_disease_list, container, false)

        if (livestock != null) {
            initialView(livestock.kind, rootView)
        } else {
            diseaseListTitle?.text = "선택된 동물이 없습니다."
        }

        return rootView

    }

    private fun initialView(kind: String, rootView: View) {
        diseaseListTitle?.text = "${Constants.toKorean(kind)}의 주요 질병 목록"

        diseaseListRV?.apply {
            adapter = DiseaseVerticalListAdapter(diseaseList, kind, context)
            layoutManager = LinearLayoutManager(context)
        }

        DiseaseUtils().getDiseases(kind, object : GetDataCallBack<List<Disease>> {
            override fun onDataReceived(data: List<Disease>) {
                diseaseList.clear()
                data.forEach {
                    diseaseList.add(it)
                }
                diseaseListRV?.adapter?.notifyDataSetChanged()
            }

            override fun onCanceled(message: String) {
                toast(message)
            }
        })
    }
}
