package com.sunday.electromapapp.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
        return binding.root
    }

    private fun setNaverMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun observers() {
        vmPositions.positions.observe(this.viewLifecycleOwner, Observer {
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
     * 새로운 포지션 요청
     */
    fun onNewPosition() {
        GlobalScope.launch(Dispatchers.IO)
        { vmPositions.getPosition() }

    }

    override fun onMapReady(naverMap: NaverMap) {
        Toast.makeText(requireContext(), "onMapReady", Toast.LENGTH_SHORT).show()
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
        this.naverMap = naverMap
        onNewPosition()

        naverMap.setOnMapClickListener { pointF, latLng ->
            infoWindow.position = latLng
            infoWindow.close()
        }

    }

}