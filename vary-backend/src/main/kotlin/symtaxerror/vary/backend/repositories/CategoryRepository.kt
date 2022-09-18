package symtaxerror.vary.backend.repositories

import io.ebean.Database
import io.ebean.DuplicateKeyException
import org.springframework.stereotype.Repository
import symtaxerror.vary.backend.entities.DbCategory
import symtaxerror.vary.backend.entities.DbInsertableCategory
import symtaxerror.vary.backend.models.Category
import java.util.*

interface CategoryRepository : CrudRepository<Category, UUID> {
    fun test(id: UUID): Iterable<Category>
}

@Repository
class CategoryRepositoryImpl(
    private val db: Database
) : CategoryRepository {
    override fun test(id: UUID): Iterable<Category> {
        return db.find(DbCategory::class.java).findList().map { it.toModel() }
    }

    override fun save(model: Category): Category {
        try {
            db.save(DbInsertableCategory.fromModel(model))
        } catch (_: DuplicateKeyException) {
            db.update(DbInsertableCategory.fromModel(model))
        }
        return db.find(DbCategory::class.java, model.id)?.toModel()
            ?: throw RepoSaveException("Error while saving category $model")
    }

    override fun saveAll(models: Iterable<Category>): Iterable<Category> {
        val ids = models.map { it.id }
        val existCategories: List<DbInsertableCategory> =
            db.find(DbInsertableCategory::class.java)
                .where().`in`("id", ids)
                .findList()
        val existIds = existCategories.map { it.id }
        val notExistCategories =
            models.filter { !existIds.contains(it.id) }
                .map { DbInsertableCategory.fromModel(it) }

        db.saveAll(notExistCategories)
        db.updateAll(existCategories)

        return db.find(DbCategory::class.java)
            .where().`in`("id", ids)
            .findList()
            .map { it.toModel() }
    }

    override fun delete(model: Category) {
        db.delete(DbInsertableCategory.fromModel(model))
    }

    override fun deleteAll(models: Iterable<Category>) {
        db.deleteAll(models.map { DbInsertableCategory.fromModel(it) })
    }

    override fun deleteById(id: UUID) {
        db.delete(DbInsertableCategory::class.java, id)
    }

    override fun deleteAllById(ids: Iterable<UUID>) {
        db.deleteAll(DbInsertableCategory::class.java, ids.toMutableList())
    }

    override fun findAll(): Iterable<Category> =
        db.find(DbCategory::class.java)
            .findList()
            .map { it.toModel() }

    override fun findById(id: UUID): Category? =
        db.find(DbCategory::class.java, id)?.toModel()

    override fun findAllById(ids: Iterable<UUID>): Iterable<Category> =
        db.find(DbCategory::class.java)
            .where().`in`("id", ids)
            .findList()
            .map { it.toModel() }

    override fun existsById(id: UUID): Boolean =
        db.find(DbInsertableCategory::class.java, id) != null

    override fun existsAllById(ids: Iterable<UUID>): Boolean =
        db.find(DbInsertableCategory::class.java)
            .where().`in`("id", ids)
            .findList()
            .size == ids.count()

    override fun count(): Long =
        db.sqlQuery("select count(*) from public.categories").findOne()?.get("count") as Long
}
