package symtaxerror.vary.backend.repositories

import symtaxerror.vary.backend.models.Version
import io.ebean.Database
import io.ebean.DuplicateKeyException
import org.springframework.stereotype.Repository
import symtaxerror.vary.backend.entities.DbVersion
import symtaxerror.vary.backend.entities.DbInsertableVersion

interface VersionRepository : CrudRepository<Version, String>

@Repository
class VersionRepositoryImpl(
    private val db: Database
) : VersionRepository {
    override fun save(model: Version): Version {
        try {
            db.save(DbInsertableVersion.fromModel(model))
        } catch (_: DuplicateKeyException) {
            db.update(DbInsertableVersion.fromModel(model))
        }
        return db.find(DbVersion::class.java, model.code)?.toModel()
            ?: throw RepoSaveException("Error while saving version $model")
    }

    override fun saveAll(models: Iterable<Version>): Iterable<Version> {
        val codes = models.map { it.code }
        val existVersions: List<DbInsertableVersion> =
            db.find(DbInsertableVersion::class.java)
                .where().`in`("code", codes)
                .findList()
        val existIds = existVersions.map { it.code }
        val notExistVersions =
            models.filter { !existIds.contains(it.code) }
                .map { DbInsertableVersion.fromModel(it) }
        db.saveAll(notExistVersions)
        db.updateAll(existVersions)

        return db.find(DbVersion::class.java)
            .where()
            .`in`("code", codes)
            .findList()
            .map { it.toModel() }
    }

    override fun delete(model: Version) {
        db.delete(DbInsertableVersion.fromModel(model))
    }

    override fun deleteAll(models: Iterable<Version>) {
        db.deleteAll(models.map { DbInsertableVersion.fromModel(it) })
    }

    override fun deleteById(id: String) {
        db.delete(DbInsertableVersion::class.java, id)
    }

    override fun deleteAllById(ids: Iterable<String>) {
        db.deleteAll(DbInsertableVersion::class.java, ids.toMutableList())
    }

    override fun findAll(): Iterable<Version> =
        db.find(DbVersion::class.java)
            .findList()
            .map { it.toModel() }

    override fun findById(id: String): Version? =
        db.find(DbVersion::class.java, id)?.toModel()

    override fun findAllById(ids: Iterable<String>): Iterable<Version> =
        db.find(DbVersion::class.java)
            .where()
            .`in`("code", ids)
            .findList()
            .map { it.toModel() }

    override fun existsById(id: String): Boolean {
        return db.find(DbInsertableVersion::class.java, id) != null
    }

    override fun existsAllById(ids: Iterable<String>): Boolean =
        db.find(DbInsertableVersion::class.java)
            .where().`in`("code", ids)
            .findList()
            .size == ids.count()

    override fun count(): Long =
        db.sqlQuery("select count(*) from public.versions").findOne()?.get("count") as Long
}
