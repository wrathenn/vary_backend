package symtaxerror.vary.backend.dto

import symtaxerror.vary.backend.models.Version
import java.sql.Timestamp

data class VersionSimpleTemplate(
    val code: String,
    val description: String?,
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

data class VersionPropsTemplate(
    val code: String,
    val description: String?,
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

data class VersionFullTemplate(
    val code: String,
    val description: String?,
    val createdTs: Timestamp,
    val categories: Iterable<CategoryFullTemplate>
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

data class VersionTemplate(
    val code: String,
    val description: String?,
    val createdTs: Timestamp,
    val categories: Iterable<CategorySimpleTemplate>
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

