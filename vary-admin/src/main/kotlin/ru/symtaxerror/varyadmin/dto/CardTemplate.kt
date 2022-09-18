package dto

import models.Card
import serializers.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data class CardTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID,
    val stats: List<StatSimpleTemplate>
) {
    fun toModel() = Card(id, name, categoryId, stats.map { it.toModel(id) })

    companion object {
        fun fromModel(card: Card) =
            CardTemplate(card.id, card.name, card.categoryId, card.stats.map { StatSimpleTemplate.fromModel(it) })
    }
}

@Serializable
data class CardSimpleTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
) {
    fun toModel(categoryId: UUID) = Card(id, name, categoryId, emptyList())

    companion object {
        fun fromModel(card: Card) =
            CardSimpleTemplate(card.id, card.name)
    }
}

@Serializable
data class CardFullTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID,
    @Serializable
    val stats: List<StatFullTemplate>
) {
    fun toModel() = Card(id, name, categoryId, stats.map { it.toModel(id) })

    companion object {
        fun fromModel(card: Card) =
            CardFullTemplate(card.id, card.name, card.categoryId, card.stats.map { StatFullTemplate.fromModel(it) })
    }
}

@Serializable
data class CardNoIdTemplate(
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID
) {
    fun toModel(id: UUID) = Card(id, name, categoryId, emptyList())

    companion object {
        fun fromModel(card: Card) =
            CardNoIdTemplate(card.name, card.categoryId)
    }
}

@Serializable
data class CardPropsTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID
) {
    fun toModel() = Card(id, name, categoryId, emptyList())

    companion object {
        fun fromModel(card: Card) =
            CardPropsTemplate(card.id, card.name, card.categoryId)
    }
}
