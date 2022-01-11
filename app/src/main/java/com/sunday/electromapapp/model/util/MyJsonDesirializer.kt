package com.sunday.electromapapp.model.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

abstract class MyJsonDesirializer<T> : JsonDeserializer<T> {
    companion object{
        fun getJsonLocalTimeFormat(json: String, formatString: String): LocalTime =
            LocalTime.parse(
                json,
                DateTimeFormatter.ofPattern(formatString)
            )
        fun getJsonLocalDateFormat(json: String, formatString: String): LocalDate =
            LocalDate.parse(
                json,
                DateTimeFormatter.ofPattern(formatString)
            )
    }
}