package com.sunday.electromapapp2.view.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.sunday.electromapapp2.IntroFragment


abstract class FragmentBase<BINDING : ViewDataBinding> : Fragment() {
    private var binding : BINDING? = null
    protected val getBinding
        get() = binding!!
    protected abstract val layoutId: Int
    protected val TAG = this.javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , layoutId , container , false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        return binding?.root
    }

    protected fun isCheck() : Boolean{
        return IntroFragment.PERMISSIONS.all{
            ContextCompat.checkSelfPermission(requireContext() , it) == PermissionChecker.PERMISSION_GRANTED
        }
    }
}