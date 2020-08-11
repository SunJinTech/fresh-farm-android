package com.sun.freshfarm.home.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.R
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.utils.CaseUtils
import kotlinx.android.synthetic.main.activity_confirm_disease_search.*
import kotlinx.android.synthetic.main.activity_report_case_search.*
import org.jetbrains.anko.toast

class ReportCaseSearch : AppCompatActivity() {

    private lateinit var listRV: RecyclerView

    private val reportSearchList = arrayListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_case_search)

        if (savedInstanceState == null) {
            initialView()
        }

    }

    private fun initialView() {
        listRV = findViewById(R.id.reportSearchRV)

        listRV.apply {
            adapter = ReportCaseSearchAdapter(
                report_data = reportSearchList,
                context = this@ReportCaseSearch
            )
            layoutManager = LinearLayoutManager(this@ReportCaseSearch)
        }

        reportSearch_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val diseaseName = query

                if (diseaseName.isNullOrBlank()) {
                    return false
                }

                CaseUtils().getSearchReport(diseaseName, GetCaseListCallBack())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private inner class GetCaseListCallBack() : GetDataCallBack<List<Report>> {
        override fun onDataReceived(data: List<Report>) {
            reportSearchList.clear()
            reportSearchList.addAll(data)

            listRV.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            toast(message)
        }
    }

}