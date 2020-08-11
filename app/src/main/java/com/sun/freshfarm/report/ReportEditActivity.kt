package com.sun.freshfarm.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.utils.ReportUtils
import org.jetbrains.anko.toast

class ReportEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_edit_activity)

        val id = intent.extras?.getString("id")
        if (!id.isNullOrEmpty()) {
            ReportUtils().getReport(id, GetReportCallBack())
        } else {
            toast("신고 내용을 조회할 수 없습니다.")
            finish()
        }
    }

    private inner class GetReportCallBack : GetDataCallBack<Report> {
        override fun onDataReceived(data: Report) {
            initialView(data)
        }

        override fun onCanceled(message: String) {
            toast(message)
            finish()
        }

    }

    private fun initialView(report: Report) {

    }
}