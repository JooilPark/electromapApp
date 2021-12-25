package com.sunday.electromapapp.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sunday.electromapapp.R
import com.sunday.electromapapp.databinding.FragmentPositionDetailBinding

class PositionDetailFragment : Fragment() {
    val args : PositionDetailFragmentArgs by navArgs()
    lateinit var binding : FragmentPositionDetailBinding;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_position_detail , container , false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.item = args.item
        return binding.root
    }
}