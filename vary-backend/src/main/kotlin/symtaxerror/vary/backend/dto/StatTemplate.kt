package symtaxerror.vary.backend.dto

import symtaxerror.vary.backend.models.Stat
import java.util.*

data class StatTemplate(
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    val cardId: UUID
) {
    fun toModel() = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatTemplate(stat.id, stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }
}

data class StatPropsTemplate(
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    val cardId: UUID
) {
    fun toModel() = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatPropsTemplate(stat.id, stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }
}

data class StatSimpleTemplate(
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

data class StatFullTemplate(
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

data class StatNoIdTemplate(
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    val cardId: UUID
) {
    fun toModel(id: UUID) = Stat(id, time, penaltyType, isSkipped, cardId)

    companion object {
        fun fromModel(stat: Stat) =
            StatNoIdTemplate(stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }
}
