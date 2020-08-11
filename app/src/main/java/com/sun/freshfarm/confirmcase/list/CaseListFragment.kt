package com.sun.freshfarm.cases.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.Navigator

import com.sun.freshfarm.R
import com.sun.freshfarm.confirmcase.list.CaseListAdapter
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.utils.CaseUtils
import kotlinx.android.synthetic.main.fragment_case_list.*
import org.jetbrains.anko.support.v4.toast

// Case List Fragment
class CaseListFragment : Fragment() {

    private var kind: String? = null
    private var diseaseCode: String? = null

    var confirmCaseList = arrayListOf<ConfirmCase>()

    private lateinit var listView: RecyclerView
    private lateinit var titleView: TextView
    private lateinit var goListView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_case_list, container, false)

        kind = arguments?.getString("kind")
        diseaseCode = arguments?.getString("code")

        initialView(rootView, kind, diseaseCode)

        return rootView
    }

    private fun initialView(
        rootView: View,
        kind: String?,
        diseaseCode: String?
    ) {

        val title = if (kind.isNullOrEmpty()) {
            "최근 확진 목록"
        } else {
            val kindKor = Constants.toKorean(kind)
            "${kindKor}의 최근 확진 목록"
        }

        titleView = rootView.findViewById(R.id.title)
        listView = rootView.findViewById(R.id.caseListRV)
        goListView = rootView.findViewById(R.id.listButton)

        titleView.text = title

        goListView.setOnClickListener {
            Navigator(context!!).startCaseList()
        }

        listView.apply {
            adapter = CaseListAdapter(confirmCaseList, context)
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }
    }

    override fun onStart() {
        super.onStart()

        if (kind.isNullOrEmpty()) {
            CaseUtils().getCases(5, null, diseaseCode, CaseCallBack())
        } else {
            CaseUtils().getCases(5, Constants.toKorean(kind!!), diseaseCode, CaseCallBack())
        }
    }

    private open inner class CaseCallBack : GetDataCallBack<List<ConfirmCase>> {
        override fun onDataReceived(data: List<ConfirmCase>) {
            confirmCaseList.clear()

            confirmCaseList.addAll(
                data.filter { case ->
                    Constants.livestocks.contains(Constants.toEnglish(case.livestockKind))
                }
            )

            caseListRV?.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            toast(message)
        }

    }
}
