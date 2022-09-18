package symtaxerror.vary.backend.dto

import symtaxerror.vary.backend.models.Card
import java.util.*

data class CardTemplate(
    val id: UUID,
    val name: String,
    val categoryId: UUID,
    val stats: Iterable<StatSimpleTemplate>
) {
    fun toModel() = Card(id, name, categoryId, stats.map { it.toModel(id) })

    companion object {
        fun fromModel(card: Card) =
            CardTemplate(card.id, card.name, card.categoryId, card.stats.map { StatSimpleTemplate.fromModel(it) })
    }
}

data class CardSimpleTemplate(
    val id: UUID,
    val name: String
) {
    fun toModel(categoryId: UUID) = Card(id, name, categoryId, emptyList())

    companion object {
        fun fromModel(card: Card) =
            CardSimpleTemplate(card.id, card.name)
    }
}

data class CardFullTemplate(
    val id: UUID,
    val name: String,
    val categoryId: UUID,
    val stats: Iterable<StatFullTemplate>
) {
    fun toModel() = Card(id, name, categoryId, stats.map { it.toModel(id) })

    companion object {
        fun fromModel(card: Card) =
            CardFullTemplate(card.id, card.name, card.categoryId, card.stats.map { StatFullTemplate.fromModel(it) })
    }
}

data class CardNoIdTemplate(
    val name: String,
    val categoryId: UUID
) {
    fun toModel(id: UUID) = Card(id, name, categoryId, emptyList())

    companion object {
        fun fromModel(card: Card) =
            CardNoIdTemplate(card.name, card.categoryId)
    }
}

data class CardPropsTemplate(
    val id: UUID,
    val name: String,
    val categoryId: UUID
) {
    fun toModel() = Card(id, name, categoryId, emptyList())

    companion object {
        fun fromModel(card: Card) =
            CardPropsTemplate(card.id, card.name, card.categoryId)
    }
}
