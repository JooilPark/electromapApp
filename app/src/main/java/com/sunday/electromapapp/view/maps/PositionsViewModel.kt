package com.sunday.electromapapp.view.maps

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunday.electromapapp.model.net.MapRepository
import com.sunday.electromapapp.model.vo.Positioninfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
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
    private val TAG = "PositionsViewModel"

    /**
     * 지금 위치에서 가능한 정보 확인
     */
    suspend fun getPosition() {

        Log.i(TAG, "getPosition")
        /*positions = liveData {
            Log.i(TAG , "getPosition waiting")
            kotlinx.coroutines.delay(5000) //딜레이 테스트용
            emitSource(mapReposition.getPositions(37.51408, 127.10440, 10).asLiveData())
            isLoading.value = false
            Log.i(TAG , "getPosition waiting end")
        }*/

        /* mapReposition.getPositions(37.51408, 127.10440, 10).collect { it ->
             _positions.postValue(it)
             isLoading.postValue(false)
         }*/

        mapReposition.getPositions(37.51408, 127.10440, 10)
            .onStart { isLoading.postValue(true) }
            .catch { e -> e.printStackTrace() }
            .collect {
                _positions.postValue(it)
                isLoading.postValue(false)
            }

    }
}