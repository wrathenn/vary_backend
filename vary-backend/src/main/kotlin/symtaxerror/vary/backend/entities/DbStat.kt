package symtaxerror.vary.backend.entities

import symtaxerror.vary.backend.models.Stat
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "public", name = "card_stats")
data class DbStat(
    @Id
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    @Column(name = "card_id")
    val cardId: UUID
) {
    companion object {
        fun fromModel(stat: Stat) =
            DbStat(stat.id, stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }

    fun toModel() =
        Stat(id, time, penaltyType, isSkipped, cardId)
}

@Entity
@Table(schema = "public", name = "card_stats")
data class DbInsertableStat(
    @Id
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    @Column(name = "card_id")
    val cardId: UUID
) {
    companion object {
        fun fromModel(stat: Stat) =
            DbInsertableStat(stat.id, stat.time, stat.penaltyType, stat.isSkipped, stat.cardId)
    }

    fun toModel() =
        Stat(id, time, penaltyType, isSkipped, cardId)
}
