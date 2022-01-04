package com.sunday.electromapapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.sunday.electromapapp.databinding.FragmentIntroBinding
import com.sunday.electromapapp.view.commons.FragmentBase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep


class IntroFragment : FragmentBase<FragmentIntroBinding>() {
    companion object{
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        val PERMISSION_REQUEST_CODE = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isCheck()){
            startMap()
        }else{
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
        Log.i(TAG , "onRequestPermissionsResult")
        startMap()
    }
    private fun startMap(){
        lifecycleScope.launch(Dispatchers.Main) {
            NavHostFragment.findNavController(this@IntroFragment)
                .navigate(R.id.action_introFragment_to_fragmentMapNaver)

        }
    }


    override val layoutId: Int
        get() = R.layout.fragment_intro
}