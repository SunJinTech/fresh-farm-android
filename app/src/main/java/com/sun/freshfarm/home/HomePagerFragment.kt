package com.sun.freshfarm.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.freshfarm.R
import com.sun.freshfarm.cases.list.CaseListFragment
import com.sun.freshfarm.report.list.ReportVerticalListFragment
import kotlinx.android.synthetic.main.fragment_home_pager.view.*

class HomePagerFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private inner class ScreenSlidePagerAdapter(
        val fragments: List<Fragment>,
        fa: FragmentActivity
    ) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home_pager, container, false)

        val viewPager = rootView.pager

//        viewPager?.adapter = HomePagerAdapter(listOf<String>("Hi", "Hello", "영민"))


        val fragmentList = listOf<Fragment>(
            CaseListFragment(),
            ReportVerticalListFragment()
        )

        viewPager?.adapter = ScreenSlidePagerAdapter(fragmentList, activity!!)

//        if not use this, recycler view cannot attach next or previous object
        viewPager?.offscreenPageLimit = 3

        val pageMarginPx = 70
        val offsetPx = 50

//        Log.d("dp", offsetPx.toString())
//        viewPager?.setPageTransformer { page, position ->
//            val viewPager = page.parent.parent as ViewPager2
//            val offset = position * -(2 * offsetPx + pageMarginPx)
////            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
////                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
////                    page.translationX = -offset
////                } else {
////                    page.translationX = offset
////                }
////            } else {
////                page.translationY = offset
////            }
//            page.translationX = position * -(160)
//        }

        val tabLayout = rootView.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //Some implementation
        }.attach()
        return rootView
    }
}