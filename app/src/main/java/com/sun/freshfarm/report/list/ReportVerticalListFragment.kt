package com.sun.freshfarm.report.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Navigator

import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.utils.ReportUtils
import org.jetbrains.anko.support.v4.toast

class ReportVerticalListFragment : Fragment() {

    val reportList = arrayListOf<Report>()

    private lateinit var titleView: TextView
    private lateinit var listView: RecyclerView
    private lateinit var listButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.report_list_fragment, container, false)

        initializeView(rootView)

        return rootView
    }

    private fun initializeView(rootView: View) {
        titleView = rootView.findViewById(R.id.title)
        listView = rootView.findViewById(R.id.reportListRV)
        listButton = rootView.findViewById(R.id.listButton)

        titleView.text = "최근 신고 목록"

        listView.apply {
            adapter = ReportVerticalListAdapter(reportList, context!!)
            layoutManager = LinearLayoutManager(context).apply {
                isNestedScrollingEnabled = false
            }
        }

        listButton.setOnClickListener {
            Navigator(context!!).startReportList()
        }


    }

    override fun onStart() {
        super.onStart()
        ReportUtils().getReports(5, GetReportsCallBack())
    }

    private open inner class GetReportsCallBack() : GetDataCallBack<List<Report>> {
        override fun onDataReceived(data: List<Report>) {
            reportList.clear()
            data.forEach { report ->
                reportList.add(report)
            }
            listView.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            toast(message)
        }
    }
}
