package com.sunday.electromapapp.model.net

import com.sunday.electromapapp.model.vo.Positioninfo
import com.sunday.electromapapp.model.vo.RequestCurrentPosition
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class MapRepository @Inject constructor(private val api :  ElectromapApi) {
    fun getPositions(pos: RequestCurrentPosition): Flow<List<Positioninfo>> = flow {
        emit(api.getpositions(pos))
    }.flowOn(Dispatchers.IO)

}