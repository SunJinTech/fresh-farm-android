package com.sun.freshfarm.report.submit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sun.freshfaram.utils.DiseaseUtils
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Disease
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.disease.symptom.DiseaseSymptomListFragment
import kotlinx.android.synthetic.main.report_submit_activity.*
import org.jetbrains.anko.toast

class ReportSubmitActivity :
    AppCompatActivity() {

    lateinit var disease: Disease

    private var loading = false

    private lateinit var code: String
    private lateinit var kind: String

    private lateinit var titleView: TextView
    private lateinit var submitView: TextView

    private lateinit var symptomListfragment: DiseaseSymptomListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_submit_activity)

        loading = true

        code = intent.getStringExtra("code")!!
        kind = intent.getStringExtra("kind")!!

        if (savedInstanceState == null) {
            if (diseaseSymptomListContainer != null) {
                initializeView()
            }
        }
    }

    private fun initializeView() {
        titleView = findViewById(R.id.titleTV)
        submitView = findViewById(R.id.submitButton)


        symptomListfragment = DiseaseSymptomListFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.diseaseSymptomListContainer, symptomListfragment)
            .commit()

        submitView.setOnClickListener {
            doSubmit()
        }

        DiseaseUtils().getDisease(code, DiseaseCallBack())
    }

    private fun doSubmit() {
        if (loading) {
            toast("로딩중입니다.")
            return
        } else {
            loading = true
        }

        val symptoms = symptomListfragment.getSelectedSymptoms()

        val submitDialog =
            ReportConfirmDialogFragment(
                disease, kind, symptoms
            ) {
                finish()
            }

        submitDialog.show(supportFragmentManager, "submitDialog")
    }


    private fun updateView(data: List<String>?) {
        titleView.text = disease.diseaseName
//        diseaseDetailSummary?.text = disease.summary

        val symptoms = arrayListOf<String>()

        data?.forEach {
            symptoms.add(it)
        }

        symptomListfragment.updateSymptoms(symptoms)
    }

    private open inner class DiseaseCallBack : GetDataCallBack<Disease> {
        override fun onDataReceived(data: Disease) {
            disease = data

            symptomListfragment.updateTitle("${kind}의 알려진 증상 목록")
            updateView(data.symptoms)

            loading = false
        }

        override fun onCanceled(message: String) {
            toast(message)
            finish()
        }
    }

}
