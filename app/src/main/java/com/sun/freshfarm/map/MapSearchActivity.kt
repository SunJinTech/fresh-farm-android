package com.sun.freshfarm.map

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.sun.freshfarm.R
import com.sun.freshfarm.utils.MapUtils
import org.jetbrains.anko.toast


@SuppressLint("MissingPermission")
class MapSearchActivity : AppCompatActivity(), OnMapReadyCallback {

    private var naverMap: NaverMap? = null

    private lateinit var currentLocation: LatLng
    private val addresses = arrayListOf<MapUtils.Address>()
    private val markers = arrayListOf<Marker>()

    private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_search_activity)

        val query = intent.extras?.getString("query")

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("teyhoqytpg")


        initialView(query)
    }

    private fun initialView(query: String?) {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        searchView = findViewById(R.id.searchView)

        searchView.setQuery(query, false)

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    doSearch(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun doSearch(query: String) {
        if (naverMap != null) {
            MapUtils().doGeocode(query).addOnCompleteListener { task ->
                addresses.clear()
                task.result?.addresses?.map { address ->
                    addresses.add(address)
                }
                setMarker()
            }
        }
    }

    private fun setMarker() {
        markers.clear()
        addresses.mapIndexed { index, address ->
            val x = address.x?.toDouble()
            val y = address.y?.toDouble()
            if (x != null && y != null && naverMap != null) {
                val marker = Marker()

                val position = LatLng(y, x)

                marker.position = position
                marker.map = naverMap
                if (index == 0) {
                    MapUtils().setCameraPosition(map = naverMap!!, position = marker.position)
                }
                marker.icon = MarkerIcons.BLACK
                marker.iconTintColor = resources.getColor(R.color.colorPrimary)

                marker.captionText = address.roadAddress.toString()
                marker.setCaptionAligns(Align.Top)

                marker.subCaptionText = "주소가 맞으면 터치"
                marker.subCaptionColor = resources.getColor(R.color.colorPrimary)
                marker.subCaptionTextSize = 15f

                marker.setOnClickListener {
                    var addressStr = "주소를 읽어오는데 실패했습니다."
                    if (address.jibunAddress != null) {
                        addressStr = address.jibunAddress!!
                    }
                    if (address.roadAddress != null) {
                        addressStr = address.roadAddress!!
                    }
                    confirmLocation(addressStr, address.x!!, address.y!!)
                    true
                }

                markers.add(marker)
            }
        }
    }

    private fun confirmLocation(address: String, x: String, y: String) {
        intent.putExtra("address", address)
        intent.putExtra("x", x)
        intent.putExtra("y", y)
        setResult(2, intent)
        finish()
        return
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(map: NaverMap) {
        toast("Naver Map is ready now.")
        naverMap = map

//        val marker = Marker()
//        marker.position = LatLng(37.5670135, 126.9783740)
//        marker.map = naverMap

        MapUtils().getMyLocation(map, this).addOnCompleteListener {
            if (it.isSuccessful) {
                MapUtils().setCameraPosition(
                    map, it.result
                )
            }
        }
    }


}