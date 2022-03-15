package com.sunday.electromapapp.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.sunday.electromapapp.R
import com.sunday.electromapapp.databinding.FragmentPositionDetailBinding

class PositionDetailFragment : Fragment() , OnMapReadyCallback {
    val args : PositionDetailFragmentArgs by navArgs()
    lateinit var binding : FragmentPositionDetailBinding;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_position_detail , container , false)
        binding.lifecycleOwner = this.viewLifecycleOwner


        binding.ditem = args.item
        setNaverMap()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onMapReady(naverMap: NaverMap) {
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(args.item.latitude, args.item.longitude)))
        Marker().apply {
            position = LatLng(args.item.latitude, args.item.longitude)
            angle = 0f
            icon = MarkerIcons.GREEN
            map = naverMap
        }
    }
}