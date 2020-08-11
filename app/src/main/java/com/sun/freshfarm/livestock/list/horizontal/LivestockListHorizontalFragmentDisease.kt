package com.sun.freshfarm.livestock.list.horizontal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.utils.LivestockUtils

class LivestockListHorizontalFragmentDisease(
    val title: String,
    val targets: List<String>?,
    val callBack: SelectCallBack
) :
    Fragment() {

    interface SelectCallBack {
        fun onSelect(kind: String)
    }

    private lateinit var titleTV: TextView
    private lateinit var editLivestockButton: TextView
    private lateinit var listRV: RecyclerView
    private lateinit var messageTV: TextView

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
        titleTV = rootView.findViewById(R.id.title)
        titleTV.text = title

        editLivestockButton = rootView.findViewById(R.id.editLivestock)
        editLivestockButton.setOnClickListener {
            Navigator(context!!).startLivestockSelect()
        }

        messageTV = rootView.findViewById(R.id.message)
        messageTV.text = "축산물 정보를 받아오고 있습니다.."


        val targetsEng = arrayListOf<String>()
        targets?.map { target ->
            Constants.toEnglish(target).let {
                targetsEng.add(it)
            }
        }
        listRV = rootView.findViewById(R.id.listRV)
        listRV.apply {
            adapter =
                LivestockListHorizontalAdapterDisease(
                    livestockList,
                    targetsEng,
                    context!!,
                    callBack
                )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
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
            messageTV.text = error
            return
        }

        if (!data.isNullOrEmpty()) {
//            update view with livestock list
            messageTV.visibility = View.GONE
            livestockList.clear()
            livestockList.addAll(data)
            listRV.adapter?.notifyDataSetChanged()
            return
        } else {
//            data is empty
            messageTV.text = "축산물 정보가 없습니다."
            return
        }
    }
}