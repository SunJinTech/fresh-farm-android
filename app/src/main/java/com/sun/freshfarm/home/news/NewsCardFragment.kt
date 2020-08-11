package com.sun.freshfarm.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.R

private var recyclerView: RecyclerView? = null
private var charItem: ArrayList<NewsCardItem>? = null
private var alphaAdapters: Adapter? = null


class NewsCardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_news_card, container, false)
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.news_card_recycler_view)
        val gridLayoutManager =
            GridLayoutManager(
                context,
                3,
                LinearLayoutManager.VERTICAL,
                false
            )

        gridLayoutManager.offsetChildrenVertical(0)
        gridLayoutManager.offsetChildrenHorizontal(0)
        recyclerView?.layoutManager = gridLayoutManager
//        recyclerView?.setHasFixedSize(true)

        charItem = ArrayList()
        charItem = setAlphas()
        alphaAdapters = Adapter(context!!, charItem!!)

        recyclerView?.adapter = alphaAdapters

        return view
    }

    private fun setAlphas(): ArrayList<NewsCardItem> {

        var arrayList: ArrayList<NewsCardItem> = ArrayList()

        arrayList.add(NewsCardItem(R.drawable.hen_white, "구제역의 심각성"))
        arrayList.add(NewsCardItem(R.drawable.cow, "나는광고광고"))
        arrayList.add(NewsCardItem(R.drawable.bee, "구제역 대처방안"))
        arrayList.add(NewsCardItem(R.drawable.pig, "구제역이란?"))
        arrayList.add(NewsCardItem(R.drawable.hen_white, "구제역의 심각성"))
        arrayList.add(NewsCardItem(R.drawable.cow, "나는광고광고"))
//        arrayList.add(NewsCardItem(R.drawable.bee, "구제역 대처방안"))
//        arrayList.add(NewsCardItem(R.drawable.hen_white, "구제역의 심각성"))
//        arrayList.add(NewsCardItem(R.drawable.cow, "나는광고광고"))
//        arrayList.add(NewsCardItem(R.drawable.bee, "구제역 대처방안"))


        return arrayList
    }

}

