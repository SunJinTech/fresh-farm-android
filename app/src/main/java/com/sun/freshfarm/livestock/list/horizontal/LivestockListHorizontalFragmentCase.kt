package com.sun.freshfarm.livestock.list.horizontal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.utils.LivestockUtils
import kotlinx.android.synthetic.main.livestock_list_horizontal.view.*

class LivestockListHorizontalFragmentCase : Fragment() {

    private lateinit var titleView: TextView
    private lateinit var listRV: RecyclerView
    private lateinit var messageView: TextView
    private lateinit var editButtonView: TextView

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
        titleView = rootView.findViewById(R.id.title)
        listRV = rootView.listRV
        messageView = rootView.findViewById(R.id.message)
        editButtonView = rootView.findViewById(R.id.editLivestock)

        titleView.text = "동물별 확진 목록 조회"

        listRV.apply {
            adapter =
                LivestockListHorizontalAdapterCase(
                    livestockList,
                    context
                )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        editButtonView.setOnClickListener {
            Navigator(context!!).startLivestockSelect()
        }

        messageView.text = "축산물 정보를 받아오고 있습니다.."
    }

    override fun onStart() {
        super.onStart()

        LivestockUtils().getLivestock(object : GetDataCallBack<List<Livestock>> {
            override fun onDataReceived(data: List<Livestock>) {
                updateView(data, null)
            }

            override fun onCanceled(message: String) {
                updateView(null, message)
            }
        })
    }

    private fun updateView(data: List<Livestock>?, error: String?) {
        if (!error.isNullOrEmpty()) {
            messageView.text = error
            return
        }

        if (!data.isNullOrEmpty()) {
//            update view with livestock list
            messageView.visibility = View.GONE
            livestockList.clear()
            livestockList.addAll(data)
            listRV.adapter?.notifyDataSetChanged()
            return
        } else {
//            data is empty
            messageView.text = "축산물 정보가 없습니다."
            return
        }
    }
}