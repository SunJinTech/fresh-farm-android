package com.sun.freshfarm.disease.detail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sun.freshfaram.utils.DiseaseUtils
import com.sun.freshfarm.Constants
import com.sun.freshfarm.EditTextFragment
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.cases.list.CaseListFragment
import com.sun.freshfarm.database.Disease
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.livestock.list.horizontal.LivestockListHorizontalFragmentDisease
import kotlinx.android.synthetic.main.unit_livestock_icon.*
import org.jetbrains.anko.support.v4.withArguments
import org.jetbrains.anko.toast

class DiseaseDetailActivity : AppCompatActivity() {
    private lateinit var livestockSelectFragment: LivestockListHorizontalFragmentDisease
    private lateinit var caseListFragment: CaseListFragment

    private lateinit var titleTV: TextView
    private lateinit var summaryTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.disease_detail_activity)

        val code = intent.getStringExtra("code")

        if (code != null && savedInstanceState == null) {
            DiseaseUtils().getDisease(code, GetDiseaseCallBacK())
        } else {
            toast("질병 코드를 불러올 수 없습니다.")
            finish()
        }
    }

    private inner class GetDiseaseCallBacK : GetDataCallBack<Disease> {
        override fun onDataReceived(data: Disease) {
            initialView(data)
        }

        override fun onCanceled(message: String) {
            toast(message)
            finish()
        }
    }

    private fun initialView(disease: Disease) {
        if (Constants.isAdmin) {
            adminInitialView()
        }

        titleTV = findViewById(R.id.title)
        titleTV.text = disease.diseaseName

        livestockSelectFragment = LivestockListHorizontalFragmentDisease(
            "질병 신고하기",
            disease.targetKinds,
            object : LivestockListHorizontalFragmentDisease.SelectCallBack {
                override fun onSelect(kind: String) {
                    Navigator(this@DiseaseDetailActivity).startReportSubmit(
                        disease.diseaseCode,
                        kind
                    )
                }
            })

        caseListFragment = CaseListFragment().withArguments("code" to disease.diseaseCode)

        summaryTV = findViewById(R.id.summary)


        if (!disease.summary.isNullOrEmpty()) {
            summaryTV.text = disease.summary
        } else {
            summaryTV.text = "아직 질병 개요가 작성되지 않았습니다."
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.livestockListContainer, livestockSelectFragment)

        transaction.add(R.id.caseListContainer, caseListFragment)

        transaction.commit()
    }

    private fun adminInitialView() {
        val editSummaryButton = findViewById<TextView>(R.id.adminSummaryEdit)

        editSummaryButton.visibility = View.VISIBLE
        editSummaryButton.setOnClickListener {
            val editSummaryFragment = EditTextFragment(callBack = { content ->
                toast(content)
            }).withArguments("title" to "질병 개요 변경", "content" to "내용내용내용")
            supportFragmentManager.beginTransaction()
                .add(R.id.root, editSummaryFragment)
                .commit()
        }
    }
}
