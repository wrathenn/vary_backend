package serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.util.*

object TimeStampSerializer : KSerializer<Timestamp> {
    /**
     * Describes the structure of the serializable representation of [T], produced
     * by this serializer. Knowing the structure of the descriptor is required to determine
     * the shape of the serialized form (e.g. what elements are encoded as lists and what as primitives)
     * along with its metadata such as alternative names.
     *
     * The descriptor is used during serialization by encoders and decoders
     * to introspect the type and metadata of [T]'s elements being encoded or decoded, and
     * to introspect the type, infer the schema or to compare against the predefined schema.
     */
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    /**
     * Deserializes the value of type [T] using the format that is represented by the given [decoder].
     * [deserialize] method is format-agnostic and operates with a high-level structured [Decoder] API.
     * As long as most of the formats imply an arbitrary order of properties, deserializer should be able
     * to decode these properties in an arbitrary order and in a format-agnostic way.
     * For that purposes, [CompositeDecoder.decodeElementIndex]-based loop is used: decoder firstly
     * signals property at which index it is ready to decode and then expects caller to decode
     * property with the given index.
     *
     * Throws [SerializationException] if value cannot be deserialized.
     *
     * Example of deserialize method:
     * ```
     * class MyData(int: Int, stringList: List<String>, alwaysZero: Long)
     *
     * fun deserialize(decoder: Decoder): MyData = decoder.decodeStructure(descriptor) {
     *     // decodeStructure decodes beginning and end of the structure
     *     var int: Int? = null
     *     var list: List<String>? = null
     *     loop@ while (true) {
     *         when (val index = decodeElementIndex(descriptor)) {
     *             DECODE_DONE -> break@loop
     *             0 -> {
     *                 // Decode 'int' property as Int
     *                 int = decodeIntElement(descriptor, index = 0)
     *             }
     *             1 -> {
     *                 // Decode 'stringList' property as List<String>
     *                 list = decodeSerializableElement(descriptor, index = 1, serializer<List<String>>())
     *             }
     *             else -> throw SerializationException("Unexpected index $index")
     *         }
     *      }
     *     if (int == null || list == null) throwMissingFieldException()
     *     // Always use 0 as a value for alwaysZero property because we decided to do so.
     *     return MyData(int, list, alwaysZero = 0L)
     * }
     * ```
     */
    override fun deserialize(decoder: Decoder): Timestamp {
        val string = decoder.decodeString()
        val odt = OffsetDateTime.parse(string)
        return Timestamp.valueOf(odt.toLocalDateTime())
    }


    /**
     * Serializes the [value] of type [T] using the format that is represented by the given [encoder].
     * [serialize] method is format-agnostic and operates with a high-level structured [Encoder] API.
     * Throws [SerializationException] if value cannot be serialized.
     *
     * Example of serialize method:
     * ```
     * class MyData(int: Int, stringList: List<String>, alwaysZero: Long)
     *
     * fun serialize(encoder: Encoder, value: MyData): Unit = encoder.encodeStructure(descriptor) {
     *     // encodeStructure encodes beginning and end of the structure
     *     // encode 'int' property as Int
     *     encodeIntElement(descriptor, index = 0, value.int)
     *     // encode 'stringList' property as List<String>
     *     encodeSerializableElement(descriptor, index = 1, serializer<List<String>>, value.stringList)
     *     // don't encode 'alwaysZero' property because we decided to do so
     * } // end of the structure
     * ```
     */
    override fun serialize(encoder: Encoder, value: Timestamp) {
        val string = value.toInstant().toString()
        encoder.encodeString(string)
    }
}