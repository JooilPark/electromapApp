package com.sunday.electromapapp.view.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class FragmentBase<BINDING : ViewDataBinding> : Fragment() {
    private var binding : BINDING? = null
    protected val getBinding
        get() = binding!!
    protected abstract val layoutId: Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , layoutId , container , false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        return binding?.root
    }
}