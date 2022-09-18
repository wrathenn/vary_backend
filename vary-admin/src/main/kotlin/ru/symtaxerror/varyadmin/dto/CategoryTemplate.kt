package dto

import ru.symtaxerror.varyadmin.models.Category
import java.util.*
import kotlinx.serialization.Serializable
import serializers.UUIDSerializer

@Serializable
data class CategorySimpleTemplate(
    @Serializable(with = UUIDSerializer::class)
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

@Serializable
data class CategoryTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
    @Serializable
    val cards: List<CardSimpleTemplate>
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

@Serializable
data class CategoryFullTemplate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
    @Serializable
    val cards: List<CardFullTemplate>
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

@Serializable
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

@Serializable
data class CategoryPropsTemplate(
    @Serializable(with = UUIDSerializer::class)
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
