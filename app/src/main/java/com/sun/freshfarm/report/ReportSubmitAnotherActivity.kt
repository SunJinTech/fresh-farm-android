package com.sun.freshfarm.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Report
import com.sun.freshfarm.utils.ReportUtils
import kotlinx.android.synthetic.main.activity_report_submit_another.*
import org.jetbrains.anko.toast

class ReportSubmitAnotherActivity : AppCompatActivity() {

    private var reportAnother = Report()
    private var livestockKindsList: List<String> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_submit_another)

        complete_bt.setOnClickListener {
            reportSubmit()

        }
    }


    private fun reportSubmit() {

        var farmName = framName_input.text.toString()
        var farmLocation = farmLocation_input.text.toString()
        var livestock = livestock_input.text.toString()
        var disease = disease_input.text.toString()

        if (farmName.isNullOrEmpty() || farmLocation.isNullOrEmpty() || livestock.isNullOrEmpty() || disease.isNullOrEmpty()) {
            toast("항목을 모두 입력해주세요.")
            return
        }
        reportAnother.livestockKinds = listOf(livestock)
        reportAnother.reportFarmName = farmName
        reportAnother.reportFarmLocation = farmLocation
        reportAnother.diseaseName = disease
        reportAnother.livestockKinds = livestockKindsList

        ReportUtils().reportsRef().document().set(reportAnother)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    toast("신고가 완료되었습니다.\n감사합니다.")
                    finish()
                }
            }

    }
}