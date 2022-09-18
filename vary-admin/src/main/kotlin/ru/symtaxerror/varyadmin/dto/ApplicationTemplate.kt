package dto

import models.Application
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationFullTemplate(
    @Serializable
    val currentVersion: VersionFullTemplate,
    @Serializable
    val devVersion: VersionFullTemplate?,
) {
    fun toModel() = Application(currentVersion.toModel(), devVersion?.toModel())

    companion object {
        fun fromModel(app: Application) =
            ApplicationFullTemplate(
                VersionFullTemplate.fromModel(app.currentVersion),
                app.devVersion?.let { VersionFullTemplate.fromModel(it) }
            )
    }
}

@Serializable
data class ApplicationTemplate(
    @Serializable
    val currentVersion: VersionTemplate,
    @Serializable
    val devVersion: VersionTemplate?,
) {
    fun toModel() = Application(currentVersion.toModel(), devVersion?.toModel())

    companion object {
        fun fromModel(app: Application) =
            ApplicationTemplate(
                VersionTemplate.fromModel(app.currentVersion),
                app.devVersion?.let { VersionTemplate.fromModel(it) }
            )
    }
}