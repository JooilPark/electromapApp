package com.sunday.electromapapp2.view.maps

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sunday.electromapapp2.R
import com.sunday.electromapapp2.databinding.MapNaverFragmentBinding
import com.sunday.electromapapp2.model.vo.Positioninfo
import com.sunday.electromapapp2.view.maps.adapters.NaverInfoWindowAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
//https://navermaps.github.io/android-map-sdk/guide-ko/1.html
//https://navermaps.github.io/android-map-sdk/guide-ko/5-3.html
/**
 * 남은이슈
 * 1. 마커 시간표시에 관한 문제 .
 * 2.
 */
class FragmentMapNaver : BaseMapFragment<MapNaverFragmentBinding>(), OnMapReadyCallback {

    private val vmPositions: PositionsViewModel by viewModels()
    private lateinit var naverMap: NaverMap
    private lateinit var naverinfoWindow: InfoWindow
    private var isGpspermission = false
    private var positions: List<Positioninfo> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding.viewmodel = vmPositions
        getBinding.lifecycleOwner = this.viewLifecycleOwner
        isGpspermission = isCheck()
        setNaverMap()
        observers()
        init()
    }

    private fun init() {
        getBinding.maptools.reFrashGps.setOnClickListener {
            positions = arrayListOf()
            vmPositions.getLastGpsPosition(isGpspermission)
        }
        getBinding.maptools.reFrashList.setOnClickListener {
            getBinding.maptools.reFrashList.isEnabled = false
            naverMap.cameraPosition.let {
                Log.i(TAG, "INI")
                onNewPosition(it.target.latitude, it.target.longitude)
            }
        }
    }

    /**
     * 지도 설정
     */
    private fun setNaverMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }


    var mapMakers = arrayListOf<Marker>()
    fun reFrashList(positions: List<Positioninfo>) {
        getBinding.maptools.reFrashList.isEnabled = true
        mapMakers.forEach {
            it.map = null
        }

        Log.i(TAG, "크기 ${mapMakers.size}")
        mapMakers.clear()
        positions.forEach { positioninfo ->
            mapMakers.add(Marker().apply {
                position = LatLng(positioninfo.latitude, positioninfo.longitude)
                angle = 0f
                height = 51.dpTopx()
                width = 46.dpTopx()
                icon =
                    if (positioninfo.isRechargeEnable()) OverlayImage.fromResource(R.drawable.icon_on) else
                        OverlayImage.fromResource(R.drawable.icon_off)
                setOnClickListener {
                    naverinfoWindow.tag = positioninfo
                    naverinfoWindow.open(this, Align.Left)
                    true
                }
                tag = positioninfo
                map = naverMap
            })


        }
    }
    /**
     * 옵저버들 설정
     */
    private fun observers() {
        vmPositions.positions.observe(this.viewLifecycleOwner) {
            positions = it
            reFrashList(positions)
        }
        vmPositions.currlocatoin.observe(this.viewLifecycleOwner) {
            if (positions.size == 0) {

                when (it) {
                    null -> {
                        // 그냥 맵에서 가운데 좌표로 호출한다 .
                        naverMap.cameraPosition.let {
                            Log.i(TAG, "currlocatoin = null")
                            onNewPosition(it.target.latitude, it.target.longitude)
                        }
                    }
                    else -> {
                        Log.i(TAG, "currlocatoin = ok")
                        onNewPosition(it.latitude, it.longitude)
                        naverMap.moveCamera(
                            CameraUpdate.scrollTo(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            )
                        )
                    }
                }
            }

        }
    }

    /**
     * 지금위치의 충전소 목록 가져온다 .
     */
    private fun onNewPosition(latitude: Double, longitude: Double) {
        Log.i(TAG, "onNewPosition")
        lifecycleScope.launch(Dispatchers.IO)
        {
            Log.i(TAG, "onNewPosition2 ${positions.size}")
            vmPositions.getPosition(latitude, longitude)
        }

    }

    fun Int.dpTopx(): Int {
        return (this * resources.displayMetrics.density).toInt()

    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        Log.i(TAG, "onMapReady")

        val infoWindow = InfoWindow().apply {
            offsetX = resources.getDimensionPixelSize(R.dimen.custom_info_window_offset_x)
            offsetY = resources.getDimensionPixelSize(R.dimen.custom_info_window_offset_y)
            adapter = NaverInfoWindowAdapter(requireContext())
            setOnClickListener {
                close()
                Log.i(TAG, "${it.tag!!.javaClass.name}")
                findNavController().navigate(
                    FragmentMapNaverDirections.actionFragmentMapNaverToPositionDetailFragment(
                        it.tag as Positioninfo
                    )
                )
                true
            }
        }
        naverinfoWindow = infoWindow
        vmPositions.getLastGpsPosition(isGpspermission)
        naverMap.setOnMapClickListener { pointF, latLng ->
            infoWindow.position = latLng
            infoWindow.close()
        }
        if(positions.isNotEmpty()){
            reFrashList(positions)
        }

    }

    /**
     * 네이버 API
     * 지금 지도에서 보고 있는 위치로 확인
     *
     */
    fun getCurrentPosition() {
        val position = naverMap.cameraPosition
    }

    private val LOCATION_REQUEST_INTERVAL = 1000


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getLocationInit() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            vmPositions._location.postValue(it)
        }
    }

    override val layoutId: Int
        get() = R.layout.map_naver_fragment
}