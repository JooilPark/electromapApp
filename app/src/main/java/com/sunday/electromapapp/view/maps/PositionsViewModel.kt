package com.sunday.electromapapp.view.maps

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunday.electromapapp.model.net.MapRepository
import com.sunday.electromapapp.model.vo.Positioninfo
import com.sunday.electromapapp.model.vo.RequestCurrentPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val _isLoding = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoding
    private val TAG = "PositionsViewModel"
    /**
     * 검색범위
     */
    private val searchRidus = 20
    private var location  : LocationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    /**
     * 지금 위치에서 가능한 충전소 정보 확인
     */
    suspend fun getPosition(latitude : Double , longitude : Double) {

        Log.i(TAG, "getPosition")
        /* positions = liveData {
             Log.i(TAG , "getPosition waiting")
             kotlinx.coroutines.delay(5000) //딜레이 테스트용
             emitSource(mapReposition.getPositions(RequestCurrentPosition(37.51408, 127.10440, 10)).asLiveData())
             isLoading.value = false
             Log.i(TAG , "getPosition waiting end")
         }*/
        //positions = mapReposition.getPositions(RequestCurrentPosition(37.51408, 127.10440, 10)).asLiveData()
        /*mapReposition.getPositions(RequestCurrentPosition(37.51408, 127.10440, 10)).collect { it ->
            _positions.postValue(it)
            isLoading.postValue(false)
        }*/

        mapReposition.getPositions(RequestCurrentPosition(37.51408, 127.10440, searchRidus))
            .onStart {
                Log.i(TAG, "Loading")
                _isLoding.postValue(true)
            }
            .catch { e -> e.printStackTrace() }
            .collect {
                delay(10000)
                _positions.postValue(it)
                Log.i(TAG, "Loading End")
                _isLoding.postValue(false)
            }

    }

    /**
     * Android Api
     * 위치 정보 가져오기
     * 처음시작시 위치 표시용
     */
    fun getLastGpsPosition() : Location?{
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return location.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        return null
    }
}