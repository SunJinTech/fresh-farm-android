package com.sun.freshfarm.home.search

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.utils.CaseUtils
import kotlinx.android.synthetic.main.activity_confirm_disease_search.*
import kotlinx.android.synthetic.main.report_detail_activity.*
import org.jetbrains.anko.toast

// 이해선사랑행
class ConfirmDiseaseSearch : AppCompatActivity() {

    private lateinit var listRV: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchWordInput: String
    private lateinit var diseaseButton: Button
    private lateinit var livestockButton: Button

    private var livestockName: String? = null
    private var diseaseName: String? = null


    private val confirmDiseaseSearchList = arrayListOf<ConfirmCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_disease_search)

        if (savedInstanceState == null) {
            initialView()
        }
    }


    private fun initialView() {
        listRV = findViewById(R.id.diseaseSearchRV)

        listRV.apply {
            adapter = ConfirmDiseaseSearchAdapter(
                confirm_data = confirmDiseaseSearchList,
                context = this@ConfirmDiseaseSearch
            )
            layoutManager = LinearLayoutManager(this@ConfirmDiseaseSearch)
        }

        searchView = findViewById(R.id.confirmDisease_search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchWordInput = query.toString()

                if (searchWordInput.isEmpty()) {
                    return false
                }

//                check query's disease name
                CaseUtils().isDiseaseName(searchWordInput).addOnCompleteListener {
                    if (it.isSuccessful) {
//                        if query is disease name
                        if (it.result!!) {
                            diseaseName = searchWordInput
                            selectDisease(searchWordInput)
                        }

//                check query's livestock
                        if (Constants.livestocks_kor.contains(searchWordInput)) {
                            livestockName = searchWordInput
                            selectLivestock(searchWordInput)
                        }

                        doSearch()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        diseaseButton = findViewById(R.id.selectDiseaseButton)
        diseaseButton.setOnClickListener {
            unselectDisease()
        }

        livestockButton = findViewById(R.id.selectLivestockButton)
        livestockButton.setOnClickListener {
            unselectLivestock()
        }
    }

    private fun doSearch() {
        var selector: String? = null
//        diseaseName n livestockKind both of not empty
        if (diseaseName != null && livestockName != null) {
            CaseUtils().searchCases(diseaseName, livestockName, GetCaseListCallBack())
//            only diseaseName
        } else if (diseaseName != null && livestockName == null) {
            selector = "diseaseName"
//            only livestockName
        } else if (diseaseName == null && livestockName != null) {
            selector = "livestockKind"

        } else {
            toast("검색어가 없습니다.")
        }
        selector?.let { it ->
            CaseUtils().getSearchConfirmCase(
                it,
                searchWordInput,
                GetCaseListCallBack()
            )
        }
    }

    private fun selectDisease(query: String) {
        diseaseButton.text = query
        diseaseButton.setBackgroundResource(R.drawable.background_primary_chip_selected)
    }

    private fun unselectDisease() {
        if (diseaseName != null) {
            diseaseName = null
            selectDiseaseButton.setBackgroundResource(R.drawable.background_primary_chip_unselected)
            selectDiseaseButton.setText("질병명")
        }
    }

    private fun selectLivestock(query: String) {
        livestockButton.text = query
        livestockButton.setBackgroundResource(R.drawable.background_primary_chip_selected)
    }

    private fun unselectLivestock() {
        if (livestockName != null) {
            livestockName = null
            selectLivestockButton.setBackgroundResource(R.drawable.background_primary_chip_unselected)
            selectLivestockButton.setText("가축명")
        }
    }

    private inner class GetCaseListCallBack() : GetDataCallBack<List<ConfirmCase>> {
        override fun onDataReceived(data: List<ConfirmCase>) {
            confirmDiseaseSearchList.clear()
            confirmDiseaseSearchList.addAll(data)

            listRV.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            toast(message)
        }
    }


}