package symtaxerror.vary.backend.repositories

import io.ebean.Database
import io.ebean.DuplicateKeyException
import org.springframework.stereotype.Repository
import symtaxerror.vary.backend.entities.DbStat
import symtaxerror.vary.backend.entities.DbInsertableStat
import symtaxerror.vary.backend.models.Stat
import java.util.*

interface StatRepository : CrudRepository<Stat, UUID> {
    fun findAllByCardId(id: UUID): Iterable<Stat>
}

@Repository
class StatRepositoryImpl(
    private val db: Database
) : StatRepository {
    override fun findAllByCardId(id: UUID): Iterable<Stat> =
        db.find(DbStat::class.java)
            .where().idEq(id)
            .findList()
            .map { it.toModel() }

    override fun save(model: Stat): Stat {
        try {
            db.save(DbInsertableStat.fromModel(model))
        } catch (_: DuplicateKeyException) {
            db.update(DbInsertableStat.fromModel(model))
        }
        return db.find(DbStat::class.java, model.id)?.toModel()
            ?: throw RepoSaveException("Error while saving $model")
    }

    override fun saveAll(models: Iterable<Stat>): Iterable<Stat> {
        val ids = models.map { it.id }
        val existStats: List<DbInsertableStat> =
            db.find(DbInsertableStat::class.java)
                .where().`in`("id", ids)
                .findList()
        val existIds = existStats.map { it.id }
        val notExistStats =
            models.filter { !existIds.contains(it.id) }
                .map { DbInsertableStat.fromModel(it) }
        db.saveAll(notExistStats)
        db.updateAll(existStats)

        return db.find(DbStat::class.java)
            .where().`in`("id", ids)
            .findList()
            .map { it.toModel() }
    }

    override fun delete(model: Stat) {
        db.delete(DbInsertableStat.fromModel(model))
    }

    override fun deleteAll(models: Iterable<Stat>) {
        db.deleteAll(models.map { DbInsertableStat.fromModel(it) })
    }

    override fun deleteById(id: UUID) {
        db.delete(DbInsertableStat::class.java, id)
    }

    override fun deleteAllById(ids: Iterable<UUID>) {
        db.deleteAll(DbInsertableStat::class.java, ids.toMutableList())
    }

    override fun findAll(): Iterable<Stat> =
        db.find(DbStat::class.java)
            .findList()
            .map { it.toModel() }

    override fun findById(id: UUID): Stat? =
        db.find(DbStat::class.java, id)?.toModel()

    override fun findAllById(ids: Iterable<UUID>): Iterable<Stat> =
        db.find(DbStat::class.java)
            .where().`in`("id", ids)
            .findList()
            .map { it.toModel() }

    override fun existsById(id: UUID): Boolean {
        return db.find(DbInsertableStat::class.java, id) != null
    }

    override fun existsAllById(ids: Iterable<UUID>): Boolean =
        db.find(DbInsertableStat::class.java)
            .where().`in`("id", ids)
            .findList()
            .size == ids.count()

    override fun count(): Long =
        db.sqlQuery("select count(*) from public.stats").findOne()?.get("count") as Long
}
