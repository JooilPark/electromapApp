package com.sunday.electromapapp.view.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.viewModels
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
import com.naver.maps.map.util.MarkerIcons
import com.sunday.electromapapp.R
import com.sunday.electromapapp.databinding.MapNaverFragmentBinding
import com.sunday.electromapapp.model.vo.Positioninfo
import com.sunday.electromapapp.view.maps.adapters.NaverInfoWindowAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
//https://navermaps.github.io/android-map-sdk/guide-ko/1.html
//https://navermaps.github.io/android-map-sdk/guide-ko/5-3.html
/**
 * 남은이슈
 * 1. 마커 시간표시에 관한 문제 .
 * 2.
 */
class FragmentMapNaver : BaseMapFragment(), OnMapReadyCallback {
    private lateinit var binding: MapNaverFragmentBinding
    private val vmPositions: PositionsViewModel by viewModels()
    private lateinit var naverMap: NaverMap
    private lateinit var naverinfoWindow: InfoWindow
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        binding = MapNaverFragmentBinding.inflate(inflater, null, false)
        binding.viewmodel = vmPositions
        binding.lifecycleOwner = this.viewLifecycleOwner

        setNaverMap()
        observers()
        init()
        return binding.root
    }

    private fun init() {
        tryEnableLocation()
        binding.maptools.reFrashGps.setOnClickListener {
            vmPositions.getLastGpsPosition()
        }
        binding.maptools.reFrashList.setOnClickListener {
            naverMap.cameraPosition.let {
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

    val TAG = "Map"
    var mapMakers = arrayListOf<Marker>()

    /**
     * 옵저버들 설정
     */
    private fun observers() {
        vmPositions.positions.observe(this.viewLifecycleOwner, {
            mapMakers.forEach {
                it.map = null
            }

            Log.i(TAG, "크기 ${mapMakers.size}")
            mapMakers.clear()
            it.forEach { positioninfo ->


                mapMakers.add(Marker().apply {
                    position = LatLng(positioninfo.latitude, positioninfo.longitude)
                    angle = 0f
                    icon =
                            if (positioninfo.isRechargeEnable()) MarkerIcons.GREEN else MarkerIcons.GRAY
                    setOnClickListener {
                        naverinfoWindow.tag = positioninfo
                        naverinfoWindow.open(this, Align.Left)
                        true
                    }
                    tag = positioninfo
                    map = naverMap
                })


            }
        })
        vmPositions.currlocatoin.observe(this.viewLifecycleOwner, {
            when (it) {
                null -> {
                    // 그냥 맵에서 가운데 좌표로 호출한다 .
                    Toast.makeText(requireContext(), "권한 체크 필요 ?", Toast.LENGTH_LONG).show()
                    naverMap.cameraPosition.let {
                        onNewPosition(it.target.latitude, it.target.latitude)
                    }
                }
                else -> {
                    Toast.makeText(requireContext(), "위치 확인", Toast.LENGTH_LONG).show()
                    onNewPosition(it.latitude, it.longitude)
                    naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude)))
                }
            }
        })
    }

    /**
     * 지금위치의 충전소 목록 가져온다 .
     */
    fun onNewPosition(latitude: Double, longitude: Double) {
        GlobalScope.launch(Dispatchers.IO)
        { vmPositions.getPosition(latitude, longitude) }

    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap


        val infoWindow = InfoWindow().apply {
            offsetX = resources.getDimensionPixelSize(R.dimen.custom_info_window_offset_x)
            offsetY = resources.getDimensionPixelSize(R.dimen.custom_info_window_offset_y)
            adapter = NaverInfoWindowAdapter(requireContext())
            setOnClickListener {
                close()
                Log.i(TAG, "${it.tag!!.javaClass.name}")
                findNavController().navigate(FragmentMapNaverDirections.actionFragmentMapNaverToPositionDetailFragment(it.tag as Positioninfo))
                true
            }
        }
        naverinfoWindow = infoWindow
        vmPositions.getLastGpsPosition()
        naverMap.setOnMapClickListener { pointF, latLng ->
            infoWindow.position = latLng
            infoWindow.close()
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
    private val PERMISSION_REQUEST_CODE = 100
    private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private fun tryEnableLocation() {

        if (PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                            requireContext(),
                            it
                    ) == PermissionChecker.PERMISSION_GRANTED
                }) {
            getLocationInit()
        } else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    PERMISSIONS,
                    PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {


        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            getLocationInit()
        }
    }


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getLocationInit() {
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            vmPositions._location.postValue(it)
        }
    }
}