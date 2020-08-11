package com.sun.freshfarm.confirmcase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sun.freshfarm.R
import com.sun.freshfarm.RankingContentList
import com.sun.freshfarm.RankingFragment
import com.sun.freshfarm.disease.list.DiseaseListHorizontalFragment
import com.sun.freshfarm.livestock.list.horizontal.LivestockListHorizontalFragmentCase

class CaseActivity : AppCompatActivity() {

    private lateinit var ranking: RankingFragment
    private lateinit var ranking2: RankingFragment
    private lateinit var ranking3: RankingFragment

    private lateinit var livestockListFragment: LivestockListHorizontalFragmentCase
    private lateinit var diseaseListFragment: DiseaseListHorizontalFragment

    private lateinit var pagerView: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.case_main)

        initialView()
    }

    private fun initialView() {
        ranking = RankingFragment(
            "한 달 내 최다 확진 질병 목록",
            RankingContentList(
                Pair("1. 가금티푸스", "13회"),
                Pair("2. 구제역", "12회"),
                Pair("3. 돼지열병", "1회")
            )
        )
        ranking2 = RankingFragment(
            "한 달 내 최다 확진 동물 목록",
            RankingContentList(
                Pair("1.가금티푸스", "내용1"),
                Pair("2. 구제역", "내용2"),
                Pair("3. 돼지열병", "내용3")
            )
        )
        ranking3 = RankingFragment(
            "역대 최다 확진 질병 목록",
            RankingContentList(
                Pair("1.가금티푸스", "내용1"),
                Pair("2. 구제역", "내용2"),
                Pair("3. 돼지열병", "내용3")
            )
        )

        pagerView = findViewById(R.id.rankingPager)

        val rankingList = listOf(
            ranking, ranking2, ranking3
        )

        pagerView.adapter = ScreenSlidePagerAdapter(rankingList, this)

        pagerView.offscreenPageLimit = rankingList.size

        livestockListFragment =
            LivestockListHorizontalFragmentCase()
        diseaseListFragment = DiseaseListHorizontalFragment()

        val transaction = supportFragmentManager.beginTransaction()
//
        transaction.add(R.id.livestockListContainer, livestockListFragment)
        transaction.add(R.id.diseaseListContainer, diseaseListFragment)
        transaction.commit()
    }

    private inner class ScreenSlidePagerAdapter(
        val fragments: List<Fragment>,
        fa: FragmentActivity
    ) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}