package com.sunday.electromapapp.view.maps.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.naver.maps.map.overlay.InfoWindow
import com.sunday.electromapapp.BR
import com.sunday.electromapapp.R
import com.sunday.electromapapp.databinding.ItemMapMakerBinding
import com.sunday.electromapapp.model.vo.Positioninfo

class NaverInfoWindowAdapter(private val context: Context) : InfoWindow.ViewAdapter() {

    override fun getView(infoWindow: InfoWindow): View {
        val itemView =DataBindingUtil.inflate<ItemMapMakerBinding>(LayoutInflater.from(context) , R.layout.item_map_maker , null , false)
        val item = infoWindow.marker!!.tag as Positioninfo
        itemView.item = item
        //itemView.ivChargeOnoff.isEnabled = item.isRechargeEnable()
        itemView.executePendingBindings()


        Log.i("adapter" , "${item}")
        return itemView.root
    }


}