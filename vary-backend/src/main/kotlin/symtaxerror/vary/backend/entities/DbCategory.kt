package symtaxerror.vary.backend.entities

import symtaxerror.vary.backend.models.Category
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "public", name = "categories")
data class DbCategory(
    @Id
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    @Column(name = "version_code")
    val versionCode: String,
    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val cards: List<DbCard>
) {
    companion object {
        fun fromModel(category: Category) =
            DbCategory(
                category.id,
                category.name,
                category.description,
                category.difficulty,
                category.versionCode,
                category.cards.map { DbCard.fromModel(it) })
    }

    fun toModel() =
        Category(id, name, description, difficulty, versionCode, cards.map { it.toModel() })
}

@Entity
@Table(schema = "public", name = "categories")
data class DbInsertableCategory(
    @Id
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    @Column(name = "version_code")
    val versionCode: String
) {
    companion object {
        fun fromModel(category: Category) =
            DbInsertableCategory(
                category.id,
                category.name,
                category.description,
                category.difficulty,
                category.versionCode
            )
    }

    fun toModel() =
        Category(id, name, description, difficulty, versionCode, emptyList())
}
