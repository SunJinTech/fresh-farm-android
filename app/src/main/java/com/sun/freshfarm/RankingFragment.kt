package com.sun.freshfarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

data class RankingContentList(
    val content1: Pair<String, String>,
    val content2: Pair<String, String>,
    val content3: Pair<String, String>
)

class RankingFragment(val title: String, val content: RankingContentList) : Fragment() {
    private lateinit var titleView: TextView

    private lateinit var content1: TextView
    private lateinit var content2: TextView
    private lateinit var content3: TextView

    private lateinit var sub1: TextView
    private lateinit var sub2: TextView
    private lateinit var sub3: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.ranking, container, false)

        initialView(rootView)

        return rootView
    }

    private fun initialView(rootView: View) {
        titleView = rootView.findViewById(R.id.title)

        content1 = rootView.findViewById(R.id.content1)
        content2 = rootView.findViewById(R.id.content2)
        content3 = rootView.findViewById(R.id.content3)

        sub1 = rootView.findViewById(R.id.sub1)
        sub2 = rootView.findViewById(R.id.sub2)
        sub3 = rootView.findViewById(R.id.sub3)

        titleView.text = title

        content1.text = content.content1.first
        content2.text = content.content2.first
        content3.text = content.content3.first

        sub1.text = content.content1.second
        sub2.text = content.content2.second
        sub3.text = content.content3.second
    }

}