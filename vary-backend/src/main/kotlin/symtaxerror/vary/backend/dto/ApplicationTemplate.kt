package symtaxerror.vary.backend.dto

import symtaxerror.vary.backend.models.Application

data class ApplicationFullTemplate(
    val currentVersion: VersionFullTemplate,
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

data class ApplicationTemplate(
    val currentVersion: VersionTemplate,
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