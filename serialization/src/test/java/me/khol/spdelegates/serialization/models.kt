package me.khol.spdelegates.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

@Serializable
internal data class Typed(
    val map: Map<String, String>,
    val nested: Nested,
)

@Serializable
internal data class Nested(
    val text: String,
    val number: Int,
)

@Serializable
internal data class Generic<out T>(
    val typed: T,
)

internal object DateAsLongSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())
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
