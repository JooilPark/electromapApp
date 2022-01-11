package com.sunday.electromapapp.model.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JsonDateDeserializser(private val format: String) : MyJsonDesirializer<LocalDate>() {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return getJsonLocalDateFormat(json.asString , format)
    }
}