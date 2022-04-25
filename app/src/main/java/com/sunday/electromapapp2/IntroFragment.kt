package com.sunday.electromapapp2

import android.Manifest
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.sunday.electromapapp2.databinding.FragmentIntroBinding
import com.sunday.electromapapp2.view.commons.FragmentBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IntroFragment : FragmentBase<FragmentIntroBinding>() {
    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        val PERMISSION_REQUEST_CODE = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isCheck()) {
            startMap()
        } else {
            requestPermissions(

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
        Log.i(TAG, "onRequestPermissionsResult")
        startMap()
    }

    private fun startMap() {
        ObjectAnimator.ofFloat(getBinding.icon, "translationY", 1000f, 0f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            addListener(
                onStart = {},
                onEnd = {
                    lifecycleScope.launch(Dispatchers.Main) {
                        delay(500)
                        NavHostFragment.findNavController(this@IntroFragment)
                            .navigate(R.id.action_introFragment_to_fragmentMapNaver)

                    }
                }
            )
            start()
        }

    }


    override val layoutId: Int
        get() = R.layout.fragment_intro
}