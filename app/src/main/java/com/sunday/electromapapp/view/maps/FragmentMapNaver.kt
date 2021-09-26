package com.sunday.electromapapp.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.sunday.electromapapp.R
import com.sunday.electromapapp.databinding.MapNaverFragmentBinding
import com.sunday.electromapapp.view.maps.adapters.NaverInfoWindowAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
//https://navermaps.github.io/android-map-sdk/guide-ko/1.html
//https://navermaps.github.io/android-map-sdk/guide-ko/5-3.html
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
        binding.lifecycleOwner = viewLifecycleOwner
        setNaverMap()
        observers()
        init()
        return binding.root
    }

    private fun init() {
        binding.maptools.reFrashGps.setOnClickListener {
            vmPositions.getLastGpsPosition()
        }
        binding.maptools.reFrashList.setOnClickListener {

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

    /**
     * 옵저버들 설정
     */
    private fun observers() {
        vmPositions.positions.observe(this.viewLifecycleOwner, {
            it.forEach { positioninfo ->
                Marker().apply {
                    position = LatLng(positioninfo.latitude, positioninfo.longitude)
                    angle = 0f
                    setOnClickListener {
                        naverinfoWindow.open(this, Align.Left)
                        true
                    }
                    tag = positioninfo
                    map = naverMap
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
                true
            }

        }
        naverinfoWindow = infoWindow

        vmPositions.getLastGpsPosition()?.let {
            onNewPosition(it.latitude, it.longitude)
        }

        when(val location = vmPositions.getLastGpsPosition()){
            null ->{
                Toast.makeText(requireContext() , "권한 체크 필요 ?" , Toast.LENGTH_LONG ).show()
            }
            else ->{
                Toast.makeText(requireContext() , "위치 확인" , Toast.LENGTH_LONG ).show()
                onNewPosition(location.latitude , location.longitude)
            }
        }

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
}