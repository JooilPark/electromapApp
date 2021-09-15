package com.sunday.electromapapp.model.net

import com.sunday.electromapapp.model.vo.Positioninfo
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class MapRepository @Inject constructor(){

    fun getPositions(): List<Positioninfo> {
        val postions = arrayListOf<Positioninfo>()
        postions.add(Positioninfo("역촌동주민센터", 37.60440124, 126.9151783))
        postions.add(Positioninfo("은평노인종합복지관", 37.63225153, 126.9299824))
        postions.add(Positioninfo("은평재활원", 37.60564519, 126.9063428))
        return postions
    }
}