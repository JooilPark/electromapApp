package com.sunday.electromapapp2.model.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate

class JsonDateDeserializser(private val format: String) : MyJsonDesirializer<LocalDate>() {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return getJsonLocalDateFormat(json.asString , format)
    }
}