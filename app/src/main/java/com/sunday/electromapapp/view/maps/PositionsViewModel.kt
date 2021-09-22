package com.sunday.electromapapp.view.maps

import android.app.Application
import androidx.lifecycle.*
import com.sunday.electromapapp.model.net.MapRepository
import com.sunday.electromapapp.model.vo.Positioninfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 1. 서버에서맵 정보 받아오기
 * 2. 위치 정보 확인 .
 */
@HiltViewModel
class PositionsViewModel @Inject constructor(
    application: Application,
    private val mapReposition: MapRepository
) : AndroidViewModel(application) {
    private val _positions: MutableLiveData<List<Positioninfo>> = MutableLiveData()
    var positions: LiveData<List<Positioninfo>> = _positions
    private val _isLoding = MutableLiveData<Boolean>(false)
    val isLoading = _isLoding

    /**
     * 지금 위치에서 가능한 정보 확인
     */
    fun getPosition() {
        isLoading.value = true
        positions = liveData {
            kotlinx.coroutines.delay(5000) //딜레이 테스트용
            emitSource(mapReposition.getPositions(37.51408, 127.10440, 10).asLiveData())
            isLoading.value = false
        }
    }
}