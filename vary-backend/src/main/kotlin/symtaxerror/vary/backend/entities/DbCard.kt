package symtaxerror.vary.backend.entities

import symtaxerror.vary.backend.models.Card
import java.util.UUID
import javax.persistence.*

@Entity
@Table(schema = "public", name = "cards")
data class DbCard(
    @Id
    val id: UUID,
    val name: String,
    @Column(name = "category_id")
    val categoryId: UUID,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    val stats: List<DbStat>
) {
    companion object {
        fun fromModel(card: Card) =
            DbCard(card.id, card.name, card.categoryId, card.stats.map { DbStat.fromModel(it) })
    }

    fun toModel() =
        Card(id, name, categoryId, stats.map { it.toModel() })
}

@Entity
@Table(schema = "public", name = "cards")
data class DbInsertableCard(
    @Id
    val id: UUID,
    val name: String,
    @Column(name = "category_id")
    val categoryId: UUID
) {
    companion object {
        fun fromModel(card: Card) =
            DbInsertableCard(card.id, card.name, card.categoryId)
    }

    fun toModel() =
        Card(id, name, categoryId, emptyList())
}
