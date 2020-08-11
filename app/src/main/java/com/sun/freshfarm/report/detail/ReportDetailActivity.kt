package com.sun.freshfarm.report.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sun.freshfarm.Constants.Companion.isAdmin
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.database.SaveDataCallBack
import com.sun.freshfarm.utils.ReportUtils
import com.sun.freshfarm.utils.TimeUtils
import org.jetbrains.anko.toast

class ReportDetailActivity : AppCompatActivity() {

    private lateinit var actionButton: TextView

    private lateinit var livestockKindView: TextView
    private lateinit var livestockCountView: TextView
    private lateinit var dateView: TextView

    private lateinit var farmNameView: TextView
    private lateinit var farmLocationView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_detail_activity)
        val id = intent.extras?.getString("id")
        if (id.isNullOrEmpty()) {
            toast("신고 정보를 조회할 수 없습니다.")
            finish()
        } else {
            ReportUtils().getReport(id, GetReportCallBack())
        }
    }

    inner class GetReportCallBack : GetDataCallBack<Report> {
        override fun onDataReceived(data: Report) {
            initialView(data)
        }

        override fun onCanceled(message: String) {
            toast(message)
            finish()
        }

    }

    private fun initialView(report: Report) {
        actionButton = findViewById(R.id.actionButton)

        livestockKindView = findViewById(R.id.livestockKind)
        livestockCountView = findViewById(R.id.livestockCount)
        dateView = findViewById(R.id.reportDate)

        farmNameView = findViewById(R.id.farmName)
        farmLocationView = findViewById(R.id.farmLocation)

//        livestockKindView.text = "신고 대상 :" + report.livestockKind
//        livestockCountView.text = "규모 :" + report.livestockCount.toString() + "마리"
//        dateView.text = "신고일 :" + TimeUtils().toDateString(report.reportTime)
//
//        farmNameView.text = "농장이름 : ${report.farmName}"
//        farmLocationView.text = "농장 위치 : ${report.farmLocation}"

        if (!isAdmin) {
            actionButton.text = "수정"
            actionButton.setOnClickListener {
//                Navigator(this).startReportEdit(report.reportId!!)
            }
        } else {
            actionButton.text = "처리"
            actionButton.setOnClickListener {
                ReportUtils().confirmReport(report.id!!, object : SaveDataCallBack {
                    override fun onSuccess(message: String) {
                        toast(message)
                        finish()
                    }

                    override fun onCanceled(message: String) {
                        toast(message)
                    }
                })
            }
        }
    }
}
