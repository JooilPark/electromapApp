package com.sunday.electromapapp2.view.maps

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunday.electromapapp2.model.net.MapRepository
import com.sunday.electromapapp2.model.vo.Positioninfo
import com.sunday.electromapapp2.model.vo.RequestCurrentPosition
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
    private val mapReposition: MapRepository,
) : AndroidViewModel(application) {
    //private val context: Context by lazy { getApplication<Application>().applicationContext as Context }


    private val _positions: MutableLiveData<List<Positioninfo>> = MutableLiveData()
    val _location: MutableLiveData<Location?> = MutableLiveData()
    val currlocatoin: LiveData<Location?> = _location
    var positions: LiveData<List<Positioninfo>> = _positions
    private val _isLoding = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoding
    private val TAG = "PositionsViewModel"

    init {


    }


    /**
     * 검색범위
     */
    private val searchRidus = 20
    private var location: LocationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * 지금 위치에서 가능한 충전소 정보 확인
     */
    suspend fun getPosition(latitude: Double, longitude: Double) {
        Log.i(TAG, "getPosition")
        mapReposition.getPositions(RequestCurrentPosition(latitude, longitude, searchRidus))
            .onStart {
                _isLoding.postValue(true)
            }
            .catch { e -> e.printStackTrace() }
            .collect {
                _positions.postValue(it)
                _isLoding.postValue(false)
            }
    }

    /**
     * Android Api
     * 위치 정보 가져오기
     * 처음시작시 위치 표시용
     */
    @SuppressLint("MissingPermission")
    fun getLastGpsPosition(isGpspermission: Boolean) {
        Log.i(TAG , "getLastGpsPosition $isGpspermission")
        if (isGpspermission) {
            _location.postValue(location.getLastKnownLocation(LocationManager.GPS_PROVIDER))
        }else{
            _location.postValue(null)
        }


    }


}