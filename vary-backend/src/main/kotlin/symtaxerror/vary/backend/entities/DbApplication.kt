package symtaxerror.vary.backend.entities

import symtaxerror.vary.backend.models.Application
import javax.persistence.*

@Entity
@Table(schema = "public", name = "application")
data class DbApplication(
    @Id
    val id: Int = 0,
    @OneToOne(targetEntity = DbVersion::class, cascade = [CascadeType.ALL])
    val currentVersion: DbVersion,
    @OneToOne(targetEntity = DbVersion::class, cascade = [CascadeType.ALL])
    val devVersion: DbVersion?,
) {
    companion object {
        fun fromModel(application: Application) =
            DbApplication(
                currentVersion = DbVersion.fromModel(application.currentVersion),
                devVersion = application.devVersion?.let { DbVersion.fromModel(it) }
            )
    }

    fun toModel() =
        Application(currentVersion.toModel(), devVersion?.toModel())
}

@Entity
@Table(schema = "public", name = "application")
data class DbInsertableApplication(
    @Id
    val id: Int = 0,
    @Column(name = "current_version_code")
    val currentVersionCode: String,
    @Column(name = "dev_version_code")
    val devVersionCode: String?,
) {
    companion object {
        fun fromModel(application: Application) =
            DbInsertableApplication(
                currentVersionCode = application.currentVersion.code,
                devVersionCode = application.devVersion?.code
            )
    }
}
