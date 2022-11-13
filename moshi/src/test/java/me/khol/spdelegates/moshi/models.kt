package me.khol.spdelegates.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.util.Date

@JsonClass(generateAdapter = true)
internal data class Typed(
    val map: Map<String, String>,
    val nested: Nested,
)

@JsonClass(generateAdapter = true)
internal data class Nested(
    val text: String,
    val number: Int,
)

@JsonClass(generateAdapter = true)
internal data class Generic<out T>(
    val typed: T,
)

internal class DateJsonAdapter : JsonAdapter<Date>() {
    override fun fromJson(reader: JsonReader): Date { return Date(reader.nextLong()) }
    override fun toJson(writer: JsonWriter, value: Date?) { writer.value(value!!.time) }
}

internal val customNested = Nested(
    text = "text",
    number = 1,
)
internal val customTyped = Typed(
    map = mapOf("key" to "value"),
    nested = customNested
)
internal val customGeneric = Generic(
    customTyped
)
