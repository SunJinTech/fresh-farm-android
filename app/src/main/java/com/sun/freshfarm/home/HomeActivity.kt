package com.sun.freshfarm.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Profile
import com.sun.freshfarmadmin.utils.UserUtils
import com.google.android.material.navigation.NavigationView
//import com.google.firebase.functions.ktx.functions
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.home.search.ConfirmDiseaseSearch
import com.sun.freshfarm.home.news.NewsCardFragment
import com.sun.freshfarm.home.search.ReportCaseSearch
import com.sun.freshfarm.livestock.list.horizontal.LivestockListHorizontalFragment
import com.sun.freshfarm.map.MapSearchActivity
import com.sun.freshfarm.report.ReportSubmitAnotherActivity
import com.sun.freshfarm.user.profile.ProfileActivity
import kotlinx.android.synthetic.main.home_main.*
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var livestockListFragment: LivestockListHorizontalFragment
    private lateinit var pagerFragment: HomePagerFragment
    private lateinit var newsFragment: NewsCardFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)


        if (savedInstanceState == null) {
            initialView()
        }
    }

    private fun initialView() {
        livestockListFragment =
            LivestockListHorizontalFragment()
        pagerFragment = HomePagerFragment()
        newsFragment = NewsCardFragment()

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        transaction.add(R.id.livestockContainer, livestockListFragment)
        transaction.add(R.id.pagerContainer, pagerFragment)
        transaction.add(R.id.newsContainer, newsFragment)

        transaction.commit()

        //navigationView
        homeMenuButton.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        chatButton.setOnClickListener {
//            val report = Report(
//                reporterUid = AuthUtils().uid!!,
//                reportTime = TimeUtils().getTimestamp(),
//                reportedUid = "empty",
//                diseaseCode = "1",
//                occurrenceCount = 0,
//                livestockKinds = listOf("HEN")
//            )
//            addMessage(report).addOnCompleteListener {
//                toast(it.result.toString())
//            }
            Navigator(this).startMapCase()
        }

        naviView.setNavigationItemSelectedListener(this)   //네비게이션 메뉴 아이템에 클릭 속성 부여
    }

//    private fun addMessage(report: Report): Task<String?> {
//        val functions = Firebase.functions
//        return functions.getHttpsCallable("addReport").call(report.toMap()).continueWith { task ->
//            val result = task.result?.data as String?
//            result
//        }
//    }

    private fun updateView(profile: Profile) {
        homeTitleText.text = profile.farmName
    }

    override fun onResume() {
        super.onResume()

        if (!AuthUtils().isLoggedIn) {
            Navigator(this).goLoginPage()
        }

        checkProfile()
    }


    private fun checkProfile() {
        val uid = AuthUtils().uid
        if (uid != null) {
            UserUtils().getProfile(uid, ProfileCallBack())
        } else {
            Navigator(this).goLoginPage()
        }
    }

    private open inner class ProfileCallBack : GetDataCallBack<Profile> {
        override fun onDataReceived(data: Profile) {
            updateView(data)
        }

        override fun onCanceled(message: String) {
            toast(message)
            Navigator(this@HomeActivity).goProfileEdit(AuthUtils().uid!!)
        }
    }


    //navigation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {    //네비게이션 메뉴 아이템 클릭시 수행
        when (item.itemId) {
            //R.id.menu_home -> Toast.makeText(applicationContext, "접근성", Toast.LENGTH_SHORT).show()
            //R.id.menu_report -> Toast.makeText(applicationContext, "헬로", Toast.LENGTH_SHORT).show()
            R.id.menu_profile -> startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                ).putExtra("uid", AuthUtils().uid)
            )

            R.id.menu_search_confirm_disease -> startActivity(
                Intent(this, ConfirmDiseaseSearch::class.java)
            )

            R.id.menu_search_report_submit_anther -> startActivity(
                Intent(this, ReportSubmitAnotherActivity::class.java)
            )

            R.id.menu_search_report_disease -> startActivity(
                Intent(this, ReportCaseSearch::class.java)
            )
        }

        drawer_layout.closeDrawers()
        return false
    }
}



