package com.sunday.electromapapp.model.net

import com.sunday.electromapapp.model.vo.Positioninfo
import com.sunday.electromapapp.model.vo.RequestCurrentPosition
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class MapRepository @Inject constructor() {


    suspend fun getPositions(pos: RequestCurrentPosition): Flow<List<Positioninfo>> =
        RetrofitUtil.getApi().getpositions(pos)
}