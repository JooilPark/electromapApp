package com.sunday.electromapapp2.model.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalTime

class JsonTimeDeserializser() : MyJsonDesirializer<LocalTime>() {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalTime {
        return when (json.asString.length) {
            5 -> {
                getJsonLocalTimeFormat(json.asString, "HH:mm")
            }
            else -> {
                getJsonLocalTimeFormat(json.asString, "HH:mm:ss")
            }
        }


    }
}