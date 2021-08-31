package com.sunday.electromapapp.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunday.electromapapp.databinding.MapNaverFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
//https://navermaps.github.io/android-map-sdk/guide-ko/1.html
class FragmentMapNaver : BaseMapFragment() {
    private lateinit var binding: MapNaverFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapNaverFragmentBinding.inflate(inflater, null, false)
        return binding.root
    }

}