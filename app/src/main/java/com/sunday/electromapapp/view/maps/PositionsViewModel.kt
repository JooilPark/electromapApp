package com.sunday.electromapapp.view.maps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.sunday.electromapapp.model.net.MapRepository
import com.sunday.electromapapp.model.vo.Positioninfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 1. 서버에서맵 정보 받아오기
 * 2. 위치 정보 확인 .
 */
@HiltViewModel
class  PositionsViewModel @Inject  constructor(application: Application, private val  mapReposition : MapRepository) : AndroidViewModel(application) {
    private val _positions : MutableLiveData<List<Positioninfo>>  = MutableLiveData()
    var positions : LiveData<List<Positioninfo>> = _positions
    /**
     * 지금 위치에서 가능한 정보 확인
     */
    fun getPosition() {
        positions =  mapReposition.getPositions(37.51408,127.10440 , 10).asLiveData()
    }
}