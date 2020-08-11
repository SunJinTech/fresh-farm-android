package com.sun.freshfarm.map

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Profile
import com.sun.freshfarm.utils.CaseUtils
import com.sun.freshfarm.utils.MapUtils

class MapCaseActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var profile: Profile

    private var naverMap: NaverMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_case_activity)

        if (Constants.myProfile != null) {
            profile = Constants.myProfile!!
        }

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("teyhoqytpg")


        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        if (profile.farmLat != null && profile.farmLng != null) {
            val myFarmLocation = LatLng(profile.farmLat!!, profile.farmLng!!)
            MapUtils().setCameraPosition(map, myFarmLocation)

            val marker = Marker()

            marker.icon = OverlayImage.fromResource(R.drawable.ic_baseline_home_24)
            marker.position = myFarmLocation

            marker.map = naverMap

            CaseUtils().getCases(500).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.map { case ->
                        val circle = CircleOverlay()
                        circle.center = LatLng(case.farmLat, case.farmLng)
                        circle.radius = 7000.0
                        circle.color = resources.getColor(R.color.colorPrimaryTP)
                        circle.map = naverMap
                        MapUtils().setCameraPosition(naverMap!!, LatLng(case.farmLat, case.farmLng))
                    }
                }
            }
        }


    }
}