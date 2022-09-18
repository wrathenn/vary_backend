package dto

import models.Stat
import serializers.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data class StatTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val cardId: UUID
) {
    fun toModel() = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatTemplate(stat.id, stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }
}

@Serializable
data class StatPropsTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val cardId: UUID
) {
    fun toModel() = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatPropsTemplate(stat.id, stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }
}

@Serializable
data class StatSimpleTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean
) {
    fun toModel(cardId: UUID) = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatSimpleTemplate(stat.id, stat.time, stat.penaltyType, stat.isSkipped)
    }
}

@Serializable
data class StatFullTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean
) {
    fun toModel(cardId: UUID) = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatFullTemplate(stat.id, stat.time, stat.penaltyType, stat.isSkipped)
    }
}

@Serializable
data class StatNoIdTemplate(
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val cardId: UUID
) {
    fun toModel(id: UUID) = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatNoIdTemplate(stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }
}
