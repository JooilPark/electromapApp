package com.sunday.electromapapp.model.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunday.electromapapp.model.vo.Positioninfo
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class MapRepository @Inject constructor( ){
    private val _isLoding = MutableLiveData<Boolean>(false)
    val isLoading = _isLoding
    @Inject lateinit var api : RetrofitUtil
    fun getPositions(lat : Double , longit : Double , distence : Int): Flow<List<Positioninfo>> =
        api.getApi().getpositions(lat, longit , distence )
}