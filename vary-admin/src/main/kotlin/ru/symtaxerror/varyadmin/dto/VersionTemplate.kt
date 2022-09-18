package dto

import models.Version
import java.sql.Timestamp
import kotlinx.serialization.Serializable
import serializers.TimeStampSerializer

@Serializable
data class VersionSimpleTemplate(
    val code: String,
    val description: String?,
    @Serializable(with = TimeStampSerializer::class)
    val createdTs: Timestamp
) {
    fun toModel() = Version(code, description, createdTs, listOf())

    companion object {
        fun fromModel(version: Version) =
            VersionSimpleTemplate(
                version.code,
                version.description,
                version.createdTs
            )
    }
}

@Serializable
data class VersionPropsTemplate(
    val code: String,
    val description: String?,
    @Serializable(with = TimeStampSerializer::class)
    val createdTs: Timestamp
) {
    fun toModel() = Version(code, description, createdTs, listOf())

    companion object {
        fun fromModel(version: Version) =
            VersionPropsTemplate(
                version.code,
                version.description,
                version.createdTs
            )
    }
}

@Serializable
data class VersionFullTemplate(
    val code: String,
    val description: String?,
    @Serializable(with = TimeStampSerializer::class)
    val createdTs: Timestamp,
    @Serializable
    val categories: List<CategoryFullTemplate>
) {
    fun toModel() = Version(code, description, createdTs, categories.map { it.toModel() })

    companion object {
        fun fromModel(version: Version) =
            VersionFullTemplate(
                version.code,
                version.description,
                version.createdTs,
                version.categories.map { CategoryFullTemplate.fromModel(it) })
    }
}

@Serializable
data class VersionTemplate(
    val code: String,
    val description: String?,
    @Serializable(with = TimeStampSerializer::class)
    val createdTs: Timestamp,
    @Serializable
    val categories: List<CategorySimpleTemplate>
) {
    fun toModel() = Version(code, description, createdTs, categories.map { it.toModel(code) })

    companion object {
        fun fromModel(version: Version) =
            VersionTemplate(
                version.code,
                version.description,
                version.createdTs,
                version.categories.map { CategorySimpleTemplate.fromModel(it) }
            )
    }
}

