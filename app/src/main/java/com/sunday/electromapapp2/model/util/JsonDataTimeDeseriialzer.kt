package com.sunday.electromapapp2.model.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonDataTimeDeseriialzer : MyJsonDesirializer<LocalDateTime>() {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(
            json?.asString,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        )
    }
}