package com.sun.freshfarm.report.submit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.sun.freshfarm.R
import com.sun.freshfarm.database.*
import com.sun.freshfarm.utils.ReportUtils
import kotlinx.android.synthetic.main.fragment_report_confirm_dialog.*
import org.jetbrains.anko.support.v4.toast

class ReportConfirmDialogFragment(
    private val disease: Disease,
    private val kind: String,
    private val symptoms: List<String>,
    private val callBack: () -> Unit
) :
    DialogFragment() {

    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (symptoms.isEmpty()) {
            toast("선택된 증상이 없습니다.")
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_confirm_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        initializeView()
    }

    private fun initializeView() {
        reportSubmitDialogTitle.text = "${disease.diseaseName}을 신고하시겠습니까?"
        reportSubmitDialogSymptoms.text = "선택하신 증상 목록"
        symptoms.forEach {
            reportSubmitDialogSymptoms.text = "${reportSubmitDialogSymptoms.text}\n - $it"
        }

        reportSubmitDialogConfirm.setOnClickListener {
            doSubmit()
        }
    }

    private fun doSubmit() {
        if (loading) {
            return
        } else {
            loading = true
        }
        ReportUtils().submitReport(
            disease,
            Livestock(kind),
            symptoms,
            object : SaveDataCallBack {
                override fun onSuccess(message: String) {
                    toast(message)
                    callBack()
                    dismiss()
                }

                override fun onCanceled(message: String) {
                    toast(message)
                    loading = false
                }

            }
        )
    }
}
