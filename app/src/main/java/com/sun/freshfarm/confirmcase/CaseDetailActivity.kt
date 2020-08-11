package com.sun.freshfarm.confirmcase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.sun.freshfaram.utils.DiseaseUtils
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.database.Disease
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.utils.CaseUtils
import com.sun.freshfarm.utils.TimeUtils
import org.jetbrains.anko.toast

class CaseDetailActivity : AppCompatActivity() {
    private lateinit var diseaseNameView: TextView
    private lateinit var diseaseNameSubView: TextView
    private lateinit var diseaseCountView: TextView
    private lateinit var diseaseCountSubView: TextView
    private lateinit var iconView: ImageView
    private lateinit var countView: TextView
    private lateinit var locationView: TextView
    private lateinit var dateView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_case_detail)

        val id = intent.extras?.getString("id")
        if (!id.isNullOrEmpty()) {
            initialView(id)
        } else {
            toast("질병 정보를 불러올 수 없습니다.")
        }
    }

    private fun initialView(id: String) {
        iconView = findViewById(R.id.kindIcon)
        diseaseNameView = findViewById(R.id.diseaseName)
        diseaseNameSubView = findViewById(R.id.diseaseNameSub)
        diseaseCountView = findViewById(R.id.diseaseCount)
        diseaseCountSubView = findViewById(R.id.diseaseCountSub)
        countView = findViewById(R.id.occurenceCount)
        locationView = findViewById(R.id.farmLocation)
        dateView = findViewById(R.id.occurenceDate)

        CaseUtils().getCase(id, GetCaseCallBack())
    }


    private fun updateView(case: ConfirmCase?, disease: Disease?) {
        if (case != null) {
            Constants.convertId(Constants.toEnglish(case.livestockKind))?.let {
                iconView.setImageResource(it)
            }
            diseaseNameView.text = case.diseaseName
            locationView.text = case.farmLocation
            countView.text = "${case.occureCount}마리"
            dateView.text = "(${case.occureDate?.let { TimeUtils().toDateString(it) }}발생)"
        }

        if (disease != null) {
            diseaseNameSubView.text =
                "(code : ${disease.diseaseCode}, ${disease.englishDiseaseName})"

            diseaseCountView.text = "${disease.occureCount}회"
            diseaseCountSubView.text = "(한 달 내 발생: ${disease.recentCount}회)"
        }
    }

    inner class GetCaseCallBack : GetDataCallBack<ConfirmCase> {
        override fun onDataReceived(data: ConfirmCase) {
            updateView(data, null)
            DiseaseUtils().getDisease(data.diseaseCode, GetDiseaseCallBack())
        }

        override fun onCanceled(message: String) {
            toast(message)
            finish()
        }
    }

    inner class GetDiseaseCallBack : GetDataCallBack<Disease> {
        override fun onDataReceived(data: Disease) {
            updateView(null, data)
        }

        override fun onCanceled(message: String) {
            toast(message)
        }
    }
}
