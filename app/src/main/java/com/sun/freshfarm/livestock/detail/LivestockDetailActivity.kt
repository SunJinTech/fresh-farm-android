package com.sun.freshfarm.livestock.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.alert.AlertListFragment
import com.sun.freshfarm.cases.list.CaseListFragment
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.disease.list.DiseaseVerticalListFragment
import com.sun.freshfarm.disease.list.DiseaseListHorizontalFragment
import com.sun.freshfarm.livestock.select.LivestockCountPickerFragment
import com.sun.freshfarm.utils.LivestockUtils
import org.jetbrains.anko.support.v4.withArguments

class LivestockDetailActivity : AppCompatActivity() {

    private lateinit var titleView: TextView
    private lateinit var summaryView: TextView

    private lateinit var diseaseListFragment: DiseaseListHorizontalFragment
    private lateinit var caseListFragment: CaseListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.livestock_detail_activity)


        val kind = intent.extras?.getString("kind")!!
        val count = intent.extras?.getLong("count")!!

        if (savedInstanceState == null) {
            diseaseListFragment = DiseaseListHorizontalFragment()
            caseListFragment = CaseListFragment().withArguments("kind" to kind)
            initialView(kind, count)
        }

        if (savedInstanceState == null) {
            val diseaseList = DiseaseVerticalListFragment(Livestock(kind, count)).withArguments(
                "kind" to kind
            )
            val alertList = AlertListFragment().withArguments(
                "kind" to kind
            )
//            supportFragmentManager.beginTransaction()
//                .add(R.id.livestockDetailDiseaseListContainer, diseaseList)
//                .add(R.id.livestockDetailAlertContainer, alertList)
//                .commit()
        }

    }

    private fun initialView(kind: String, count: Long) {
        titleView = findViewById(R.id.title)
        summaryView = findViewById(R.id.summaryContent)

        titleView.text = Constants.toKorean(kind)

        LivestockUtils().getSummary(kind, GetSummaryCallBack())

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(
            R.id.diseaseListContainer,
            diseaseListFragment.withArguments("kind" to kind)
        )
        transaction.add(R.id.caseListContainer, caseListFragment)

        transaction.commit()
    }

    inner class GetSummaryCallBack : GetDataCallBack<String> {
        override fun onDataReceived(data: String) {
            summaryView.text = data
        }

        override fun onCanceled(message: String) {
            summaryView.text = message
        }
    }

    private fun updateView(kind: String, count: Long) {
//        livestockDetailSubtitle.text = "우리 농장에는 $count 마리"
    }


    private fun editCount(kind: String, count: Long) {
        val countPicker =
            LivestockCountPickerFragment(object : LivestockCountPickerFragment.CounterCallBack {
                override fun onCounterSet(count: Long) {
                    updateView(kind, count)
                }
            })
        countPicker.withArguments("count" to count.toString(), "kind" to kind)
            .show(supportFragmentManager, "countPicker")
    }
}
