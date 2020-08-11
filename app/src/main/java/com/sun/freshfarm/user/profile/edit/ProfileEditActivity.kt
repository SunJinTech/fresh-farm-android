package com.sun.freshfarm.user.profile.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Profile
import com.sun.freshfarm.database.SaveDataCallBack
import com.sun.freshfarm.home.HomeActivity
import com.sun.freshfarm.map.MapSearchActivity
import com.sun.freshfarm.utils.MapUtils
import com.sun.freshfarmadmin.utils.UserUtils
import kotlinx.android.synthetic.main.profile_edit_activity.*
import org.jetbrains.anko.toast

class ProfileEditActivity : AppCompatActivity(), OnMapReadyCallback {

    private var naverMap: NaverMap? = null

    private var profile = Profile()

    private lateinit var nameTV: EditText
    private lateinit var emailTV: TextView
    private lateinit var phoneInput: EditText

    private lateinit var farmNameInput: EditText
    private lateinit var introduceInput: EditText
    private lateinit var farmLocationTV: TextView


    //    Get Profile -> InitialView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_edit_activity)

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("teyhoqytpg")

        val uid = intent.extras?.getString("uid")

        if (uid == null) {
            toast("유저 정보를 받아올 수 없습니다.")
        } else {
            UserUtils().getProfile(uid).addOnCompleteListener {
                if (it.result != null) {
                    profile = it.result!!
                    initialView(it.result!!)
                } else {
                    initialView(null)
                }
            }
        }

    }

    private fun initialView(profile: Profile?) {
//        Naver Map initialize
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)


        if (profile == null) {
            toast("프로필 정보가 없습니다. 작성하여주세요.")
        }

        if (profile!!.farmLat != null && profile.farmLng != null) {
            updateMap(profile.farmLocation, LatLng(profile.farmLat!!, profile.farmLng!!))
        }

        nameTV = findViewById(R.id.editProfileNameText)
        nameTV.setText(AuthUtils().user?.displayName)

        if (profile.name.isNotEmpty()) {
            nameTV.setText(profile.name)
        }

        emailTV = findViewById(R.id.editProfileEmailText)
        emailTV.text = AuthUtils().user?.email

        phoneInput = findViewById(R.id.phoneNumber)
        phoneInput.setText(profile.phoneNumber)

        farmNameInput = findViewById(R.id.editProfileFarmNameText)
        farmNameInput.setText(profile.farmName)

        introduceInput = findViewById(R.id.editProfileFarmIntroduceText)
        introduceInput.setText(profile.introduce)

        farmLocationTV = findViewById(R.id.editProfileFarmLocationText)
        farmLocationTV.text = profile.farmLocation

        farmLocationTV.setOnClickListener {
            startMapSearch()
        }

        editProfileSaveTextButton.setOnClickListener {
            saveProfile()
        }
    }


    private fun startMapSearch() {
        val intent = Intent(this, MapSearchActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2) {
            val address = data?.getStringExtra("address").toString()
            val x = data?.getStringExtra("x").toString()
            val y = data?.getStringExtra("y").toString()

            setAddress(address, x, y)
        }
    }

    private fun updateMap(address: String, position: LatLng) {
        val marker = Marker()

        marker.position = position

        marker.map = naverMap

        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = resources.getColor(R.color.colorPrimary)

        marker.captionText = address
        marker.setCaptionAligns(Align.Top)

        if (naverMap != null) {
            MapUtils().setCameraPosition(
                naverMap!!, LatLng(profile.farmLat!!, profile.farmLng!!)
            )
        }
    }

    private fun setAddress(address: String, x: String, y: String) {
        profile.farmLocation = address
        profile.farmLat = y.toDouble()
        profile.farmLng = x.toDouble()

        farmLocationTV.text = profile.farmLocation

        val marker = Marker()

        marker.position = LatLng(profile.farmLat!!, profile.farmLng!!)

        marker.map = naverMap

        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = resources.getColor(R.color.colorPrimary)

        marker.captionText = profile.farmLocation
        marker.setCaptionAligns(Align.Top)

        if (naverMap != null) {
            MapUtils().setCameraPosition(
                naverMap!!, LatLng(profile.farmLat!!, profile.farmLng!!)
            )
        }
    }

    private fun saveProfile() {
//        val profile = Profile(
        profile.uid = AuthUtils().uid!!
        profile.name = editProfileNameText.text.toString()
        profile.email = editProfileEmailText.text.toString()
        profile.phoneNumber = phoneNumber.text.toString()

        profile.farmName = editProfileFarmNameText.text.toString()
        profile.introduce = editProfileFarmIntroduceText.text.toString()

        if (profile.farmLocation.isNullOrEmpty()) {
            toast("위치를 입력해 주십시오.")
            return
        }

        UserUtils().setProfile(profile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast(task.result.toString())
                finish()
            } else {
                toast(task.result.toString())
            }
        }

    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        MapUtils().getMyLocation(map, this).addOnCompleteListener {
            if (it.isSuccessful) {
                MapUtils().setCameraPosition(
                    map, it.result
                )
            }
        }
    }
}
