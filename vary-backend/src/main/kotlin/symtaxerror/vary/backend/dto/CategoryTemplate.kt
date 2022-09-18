package symtaxerror.vary.backend.dto

import symtaxerror.vary.backend.models.Category
import java.util.*

data class CategorySimpleTemplate(
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int
) {
    fun toModel(versionCode: String) =
        Category(id, name, description, difficulty, versionCode, listOf())

    companion object {
        fun fromModel(category: Category) =
            CategorySimpleTemplate(
                category.id,
                category.name,
                category.description,
                category.difficulty
            )
    }
}

data class CategoryTemplate(
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
    val cards: Iterable<CardSimpleTemplate>
) {
    fun toModel() = Category(id, name, description, difficulty, versionCode, cards.map { it.toModel(id) })

    companion object {
        fun fromModel(category: Category) =
            CategoryTemplate(
                category.id,
                category.name,
                category.description,
                category.difficulty,
                category.versionCode,
                category.cards.map { CardSimpleTemplate.fromModel(it) }
            )
    }
}

data class CategoryFullTemplate(
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
    val cards: Iterable<CardFullTemplate>
) {
    fun toModel() = Category(id, name, description, difficulty, versionCode, cards.map { it.toModel() })

    companion object {
        fun fromModel(category: Category) =
            CategoryFullTemplate(
                category.id,
                category.name,
                category.description,
                category.difficulty,
                category.versionCode,
                category.cards.map { CardFullTemplate.fromModel(it) }
            )
    }
}

data class CategoryNoIdNoCardsTemplate(
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
) {
    fun toModel(id: UUID) = Category(id, name, description, difficulty, versionCode, emptyList())

    companion object {
        fun fromModel(category: Category) =
            CategoryNoIdNoCardsTemplate(
                category.name,
                category.description,
                category.difficulty,
                category.versionCode
            )
    }
}

data class CategoryPropsTemplate(
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
) {
    fun toModel() = Category(id, name, description, difficulty, versionCode, emptyList())

    companion object {
        fun fromModel(category: Category) =
            CategoryPropsTemplate(
                category.id,
                category.name,
                category.description,
                category.difficulty,
                category.versionCode
            )
    }
}
