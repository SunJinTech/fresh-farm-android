package com.sun.freshfarm.livestock.select

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.database.SaveDataCallBack
import com.sun.freshfarm.livestock.list.vertical.LivestockListVerticalFragment
import com.sun.freshfarm.utils.LivestockUtils
import kotlinx.android.synthetic.main.activity_livestock_select.*
import org.jetbrains.anko.toast

class LivestockSelectActivity : AppCompatActivity() {

    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livestock_select)

        val livestockListFragment =
            LivestockListVerticalFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.livestockListContainer,
                    livestockListFragment
                )
                .commit()
        }

        livestockSelectButton.setOnClickListener {
            doSelect(livestockListFragment.livestockList)
        }
    }


    private fun doSelect(livestocks: ArrayList<Livestock>) {
        if (isLoading) {
            return
        } else {
            isLoading = true
        }

        LivestockUtils().saveLivestock(livestocks, object : SaveDataCallBack {
            override fun onSuccess(message: String) {
                toast(message)
                finish()
            }

            override fun onCanceled(message: String) {
                toast(message)
                finish()
            }
        })
    }
}
