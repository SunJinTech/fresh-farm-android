package com.sun.freshfarm.alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.sun.freshfarm.R
import com.sun.freshfarm.database.Alert
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.utils.AlertUtils
import kotlinx.android.synthetic.main.fragment_alert_list.*
import org.jetbrains.anko.support.v4.toast

class AlertListFragment : Fragment() {

    private var kind: String? = null
    private val alertList = arrayListOf<Alert>()
    private lateinit var listRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kind = arguments?.getString("kind")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        alertListRV.apply {
            adapter = AlertListAdapter(alertList)
            layoutManager = LinearLayoutManager(context)
        }

        listRV = alertListRV

        if (kind.isNullOrEmpty()) {
            AlertUtils().getAlerts(DataCallBack())
        } else {
            AlertUtils().getAlerts(kind!!, DataCallBack())
        }

    }

    private open inner class DataCallBack : GetDataCallBack<List<Alert>> {
        override fun onDataReceived(data: List<Alert>) {
            alertList.clear()
            data.forEach { alert ->
                alertList.add(alert)
            }
            listRV.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            toast(message)
        }

    }
}
