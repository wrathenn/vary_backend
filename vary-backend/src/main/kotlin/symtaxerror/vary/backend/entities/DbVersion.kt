package symtaxerror.vary.backend.entities

import symtaxerror.vary.backend.models.Version
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(schema = "public", name = "versions")
data class DbVersion(
    @Id
    val code: String,
    val description: String?,
    val createdTs: Timestamp,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "version_code", referencedColumnName = "code")
    val categories: List<DbCategory>
) {
    companion object {
        fun fromModel(version: Version) =
            DbVersion(
                version.code,
                version.description,
                version.createdTs,
                version.categories.map { DbCategory.fromModel(it) }
            )
    }

    fun toModel() =
        Version(code, description, createdTs, categories.map { it.toModel() })
}

@Entity
@Table(schema = "public", name = "versions")
data class DbInsertableVersion(
    @Id
    val code: String,
    val description: String?,
    val createdTs: Timestamp
) {
    companion object {
        fun fromModel(version: Version) =
            DbInsertableVersion(
                version.code,
                version.description,
                version.createdTs
            )
    }

    fun toModel() =
        Version(code, description, createdTs, emptyList())
}
