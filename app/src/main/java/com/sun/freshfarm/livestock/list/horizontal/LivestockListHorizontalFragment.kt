package com.sun.freshfarm.livestock.list.horizontal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.utils.LivestockUtils
import kotlinx.android.synthetic.main.fragment_disease_symptom_list.*
import kotlinx.android.synthetic.main.unit_livestock_icon.*

class LivestockListHorizontalFragment : Fragment() {
    private lateinit var listRV: RecyclerView
    private lateinit var messageTV: TextView
    private lateinit var editButton: TextView

    private var livestockList = arrayListOf<Livestock>(
//        Livestock("COW", 5),
//        Livestock("PIG", 12),
//        Livestock("DOG", 1),
//        Livestock("SHEEP", 7)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.livestock_list_horizontal, container, false)

        initialView(rootView)

        return rootView
    }

    private fun initialView(rootView: View) {
        listRV = rootView.findViewById(R.id.listRV)
        messageTV = rootView.findViewById(R.id.message)
        editButton = rootView.findViewById(R.id.editLivestock)

        listRV.apply {
            adapter =
                LivestockListHorizontalAdapter(
                    livestockList,
                    context
                )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        editButton.setOnClickListener {
            Navigator(context!!).startLivestockSelect()
        }

        messageTV.text = "축산물 정보를 받아오고 있습니다.."
    }

    override fun onStart() {
        super.onStart()

        LivestockUtils().getLivestock(object : GetDataCallBack<List<Livestock>> {
            override fun onDataReceived(data: List<Livestock>) {
                updateView(data, null)
                val kinds = arrayListOf<String>()
                data.filter {
                    it.count > 0
                }.map {
                    kinds.add(it.kind)
                }
                Constants.livestocks = kinds
            }

            override fun onCanceled(message: String) {
                updateView(null, message)
            }
        })
    }

    private fun updateView(data: List<Livestock>?, error: String?) {
        if (!error.isNullOrEmpty()) {
            messageTV.text = error
            return
        }

        if (!data.isNullOrEmpty()) {
//            update view with livestock list
            messageTV.visibility = View.GONE
            livestockList.clear()
            livestockList.addAll(data)
            listRV.apply {
                adapter =
                    LivestockListHorizontalAdapter(
                        livestockList,
                        context
                    )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            return
        } else {
//            data is empty
            messageTV.text = "축산물 정보가 없습니다."
            return
        }
    }
}