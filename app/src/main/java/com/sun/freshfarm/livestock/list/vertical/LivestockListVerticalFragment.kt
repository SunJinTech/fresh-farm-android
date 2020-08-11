package com.sun.freshfarm.livestock.list.vertical

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.utils.LivestockUtils
import org.jetbrains.anko.support.v4.toast

class LivestockListVerticalFragment : Fragment() {

    private lateinit var titleView: TextView
    private lateinit var listRV: RecyclerView

    var livestockList = arrayListOf<Livestock>(
        Livestock("BEE", 0),
        Livestock("COW", 0),
        Livestock("HEN", 0),
        Livestock("PIG", 0),
        Livestock("SHEEP", 0)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_livestock_list_vertical, container, false)

//        rootView.livestockListVerticalTitle.text = ""

        initialView(rootView)

        return rootView
    }

    private fun initialView(rootView: View) {
        titleView = rootView.findViewById(R.id.title)
        listRV = rootView.findViewById(R.id.listRV)

        listRV.apply {
            adapter =
                LivestockListVerticalAdapter(
                    livestockList,
                    true,
                    context!!,
                    object :
                        LivestockListVerticalAdapter.LivestockListCallBack {
                        override fun onCountChanged(livestock: Livestock) {
                            livestockList.remove(livestockList.find { it.kind == livestock.kind })
                            livestockList.add(livestock)
                            livestockList.sortBy { it.kind }
                        }
                    }
                )
            layoutManager = LinearLayoutManager(context)
        }

        LivestockUtils().getLivestock(GetLivestockCallBack())
    }

    inner class GetLivestockCallBack : GetDataCallBack<List<Livestock>> {
        override fun onDataReceived(data: List<Livestock>) {
            livestockList.clear()
            livestockList.addAll(data)
            livestockList.sortBy { livestock -> livestock.count }

            listRV.adapter?.notifyDataSetChanged()
        }

        override fun onCanceled(message: String) {
            toast(message)
        }
    }
}
