package com.sun.freshfarm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import org.jetbrains.anko.toast
import java.util.HashMap

class MapUtils {

    val functions = Firebase.functions

    fun toLatLng(x: String, y: String): LatLng {
        return LatLng(x.toDouble(), y.toDouble())
    }

    fun setCameraPosition(map: NaverMap, position: LatLng?) {
        val cameraUpdate =
            position?.let { CameraUpdate.scrollTo(it) }
        cameraUpdate?.let { map.moveCamera(it) }
    }

    @SuppressLint("MissingPermission")
    fun getMyLocation(map: NaverMap, context: Context): Task<LatLng> {
        return LocationServices.getFusedLocationProviderClient(context).lastLocation.continueWith {
            if (it.isSuccessful && it.result != null) {
                return@continueWith LatLng(it.result!!.latitude, it.result!!.longitude)
            } else {
                return@continueWith null
            }
        }
    }

    data class GeocodeResponse(
        var status: String = "",
        var errorMessage: String? = null,
        var addresses: ArrayList<Address> = arrayListOf(),
        var meta: GeocodeMeta? = null
    ) {
        fun from(map: HashMap<*, *>): GeocodeResponse {
            val result = GeocodeResponse()
            val status_ = map["status"]
            if (status_ is String) {
                result.status = status_
            }

            val errorMessage_ = map["errorMessage"]
            if (errorMessage_ is String) {
                result.errorMessage = errorMessage_
            }

            val meta_ = map["meta"]
            if (meta_ is HashMap<*, *>) {
                val meta = GeocodeMeta()
                meta_.entries.map {
                    when (it.key) {
                        "count" -> meta.count = it.value as Int?
                        "page" -> meta.page = it.value as Int?
                        "totalCount" -> meta.totalCount = it.value as Int?
                    }
                }
                result.meta = meta
            }

            val addresses = map["addresses"]
            if (addresses is ArrayList<*>) {
                addresses.map { address_ ->
                    val address = Address()
                    if (address_ is HashMap<*, *>) {
                        address_.entries.map {
                            when (it.key) {
                                "roadAddress" -> address.roadAddress = it.value as String?
                                "jibunAddress" -> address.jibunAddress = it.value as String?
                                "englishAddress" -> address.englishAddress = it.value as String?
                                "x" -> address.x = it.value as String?
                                "y" -> address.y = it.value as String?
                                "distance" -> address.distance = it.value as Int?
                                "addressElements" -> address.addressElements = it.value
                            }
                        }
                    }
                    result.addresses.add(address)
                }
            }
            return result
        }
    }

    data class Address(
        var roadAddress: String? = null,
        var jibunAddress: String? = null,
        var englishAddress: String? = null,
        var x: String? = null,
        var y: String? = null,
        var distance: Int? = null,
        var addressElements: Any? = null
    )

    data class GeocodeMeta(
        var totalCount: Int? = null,
        var page: Int? = null,
        var count: Int? = null
    )

    fun doGeocode(query: String): Task<GeocodeResponse?> {
        val req = mapOf<String, String>(
            "query" to query
        )
        return functions.getHttpsCallable("doGeocode").call(req).continueWith {
            if (it.isSuccessful) {
                val data = it.result?.data as HashMap<*, *>
                val result = GeocodeResponse().from(data)
                return@continueWith result
            } else {
                return@continueWith null
            }
        }
    }
}
//type Address = {
//    // 도로명 주소
//    roadAddress?: string;
//    //  지번 주소
//    jibunAddress?: string;
//    // 영어 주소
//    englishAddress?: string;
//    // 경도
//    x?: string;
//    // 위도
//    y?: string;
//    // 검색 중심 좌표로부터의 거리(단위: 미터)
//    distance?: number;
//    // 주소를 이루는 요소들
//    addressElements?: Array<any>;
//};