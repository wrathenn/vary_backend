package symtaxerror.vary.backend.repositories

import io.ebean.Database
import io.ebean.DuplicateKeyException
import org.springframework.stereotype.Repository
import symtaxerror.vary.backend.entities.DbCard
import symtaxerror.vary.backend.entities.DbInsertableCard
import symtaxerror.vary.backend.models.Card
import java.util.*

interface CardRepository : CrudRepository<Card, UUID>

@Repository
class CardRepositoryImpl(
    private val db: Database
) : CardRepository {
    override fun save(model: Card): Card {
        try {
            db.save(DbInsertableCard.fromModel(model))
        } catch (_: DuplicateKeyException) {
            db.update(DbInsertableCard.fromModel(model))
        }
        return db.find(DbCard::class.java, model.id)?.toModel()
            ?: throw RepoSaveException("Error while saving card $model")
    }

    override fun saveAll(models: Iterable<Card>): Iterable<Card> {
        val ids = models.map { it.id }
        val existCards: List<DbInsertableCard> =
            db.find(DbInsertableCard::class.java)
                .where().`in`("id", ids)
                .findList()
        val existIds = existCards.map { it.id }
        val notExistCards =
            models.filter { !existIds.contains(it.id) }
                .map { DbInsertableCard.fromModel(it) }
        db.saveAll(notExistCards)
        db.updateAll(existCards)

        return db.find(DbCard::class.java)
            .where().`in`("id", ids)
            .findList()
            .map { it.toModel() }
    }

    override fun delete(model: Card) {
        db.delete(DbInsertableCard.fromModel(model))
    }

    override fun deleteAll(models: Iterable<Card>) {
        db.deleteAll(models.map { DbInsertableCard.fromModel(it) })
    }

    override fun deleteById(id: UUID) {
        db.delete(DbInsertableCard::class.java, id)
    }

    override fun deleteAllById(ids: Iterable<UUID>) {
        db.deleteAll(DbInsertableCard::class.java, ids.toMutableList())
    }

    override fun findAll(): Iterable<Card> =
        db.find(DbCard::class.java)
            .findList()
            .map { it.toModel() }

    override fun findById(id: UUID): Card? =
        db.find(DbCard::class.java, id)?.toModel()

    override fun findAllById(ids: Iterable<UUID>): Iterable<Card> =
        db.find(DbCard::class.java)
            .where().`in`("id", ids)
            .findList()
            .map { it.toModel() }

    override fun existsById(id: UUID): Boolean {
        return db.find(DbInsertableCard::class.java, id) != null
    }

    override fun existsAllById(ids: Iterable<UUID>): Boolean =
        db.find(DbInsertableCard::class.java)
            .where().`in`("id", ids)
            .findList()
            .size == ids.count()

    override fun count(): Long =
        db.sqlQuery("select count(*) from public.cards").findOne()?.get("count") as Long
}
