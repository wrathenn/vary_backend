package symtaxerror.vary.backend.repositories

import io.ebean.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.entities.DbCategory
import symtaxerror.vary.backend.entities.DbInsertableCategory
import symtaxerror.vary.backend.models.Category
import java.util.*

internal class CategoryRepositoryImplTest {
    private val db: Database = mock()
    private val categoryRepository = CategoryRepositoryImpl(db)

    @Test
    fun save() {
        val insCategory = DbInsertableCategory(UUID.randomUUID(), "Animals", "Category with animals", 10, "1.0.0")
        var category: DbCategory? = null

        whenever(db.save(insCategory)).then {
            category = DbCategory(
                insCategory.id, "Animals", "Category with animals", 10, "1.0.0", listOf()
            )
            return@then Unit
        }

        whenever(db.find(DbCategory::class.java, insCategory.id)).then { category }

        val expected = Category(
            insCategory.id, "Animals", "Category with animals", 10, "1.0.0", listOf()
        )

        assertEquals(
            expected,
            Category(
                insCategory.id, "Animals", "Category with animals", 10, "1.0.0", listOf()
            )
        )
    }

    @Test
    fun saveAll() {
        val ids = List(2) { UUID.randomUUID() }
        val versions: MutableList<DbCategory> = mutableListOf()

        val insCategories = listOf(
            DbInsertableCategory(ids[0], "Animals", "Category with animals", 10, "1.0.0"),
            DbInsertableCategory(ids[1], "Cars", "Category with cars", 13, "1.0.0")
        )

        whenever(db.saveAll(insCategories)).then {
            versions.addAll(
                listOf(
                    DbCategory(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
                    DbCategory(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
                )
            )
            return@then 2
        }

        val queryMock: Query<DbCategory> = mock()
        val exprListMock: ExpressionList<DbCategory> = mock()
        whenever(db.find(DbCategory::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", ids)).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { versions }

        val expected = listOf(
            Category(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
            Category(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
        )

        assertEquals(
            expected,
            categoryRepository.saveAll(
                listOf(
                    Category(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
                    Category(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
                )
            )
        )
    }

    @Test
    fun delete() {
        val category = Category(UUID.randomUUID(), "Animals", "Category with animals", 10, "1.0.0", listOf())
        val insCategory = DbInsertableCategory(UUID.randomUUID(), "Animals", "Category with animals", 10, "1.0.0")

        whenever(db.delete(insCategory)).thenReturn(true)
        categoryRepository.delete(category)
    }

    @Test
    fun deleteAll() {
        val ids = List(2) { UUID.randomUUID() }
        val categories = listOf(
            Category(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
            Category(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
        )

        val insCategories = listOf(
            DbInsertableCategory(ids[0], "Animals", "Category with animals", 10, "1.0.0"),
            DbInsertableCategory(ids[1], "Cars", "Category with cars", 13, "1.0.0")
        )
        whenever(db.deleteAll(insCategories)).thenReturn(2)
        categoryRepository.deleteAll(categories)
    }

    @Test
    fun deleteById() {
        val id = UUID.randomUUID()

        whenever(db.delete(DbInsertableCategory::class.java, id)).thenReturn(1)
        categoryRepository.deleteById(id)
    }

    @Test
    fun deleteAllById() {
        val ids = List(2) { UUID.randomUUID() }
        whenever(db.deleteAll(ids)).thenReturn(2)
        categoryRepository.deleteAllById(ids)
    }

    @Test
    fun findAll() {
        val ids = List(2) { UUID.randomUUID() }
        val queryMock: Query<DbCategory> = mock()
        whenever(db.find(DbCategory::class.java)).thenReturn(queryMock)
        whenever(queryMock.findList()).thenReturn(
            listOf(
                DbCategory(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
                DbCategory(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
            )
        )

        val expected = listOf(
            Category(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
            Category(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
        )
        assertEquals(expected, categoryRepository.findAll())
    }

    @Test
    fun findById() {
        val id = UUID.randomUUID()
        whenever(db.find(DbCategory::class.java, id)).thenReturn(
            DbCategory(id, "Animals", "Category with animals", 10, "1.0.0", listOf())
        )
        val expected = Category(id, "Animals", "Category with animals", 10, "1.0.0", listOf())
        assertEquals(expected, categoryRepository.findById(id))
    }

    @Test
    fun findAllById() {
        val ids = List(2) { UUID.randomUUID() }
        val queryMock: Query<DbCategory> = mock()
        val exprListMock: ExpressionList<DbCategory> = mock()
        whenever(db.find(DbCategory::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", ids.asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then {
            listOf(
                DbCategory(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
                DbCategory(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
            )
        }

        val expected = listOf(
            Category(ids[0], "Animals", "Category with animals", 10, "1.0.0", listOf()),
            Category(ids[1], "Cars", "Category with cars", 13, "1.0.0", listOf())
        )
        assertEquals(expected, categoryRepository.findAllById(ids))
    }

    @Test
    fun existsById() {
        val id = UUID.randomUUID()
        whenever(db.find(DbInsertableCategory::class.java, id)).then {
            DbCategory(id, "Animals", "Category with animals", 10, "1.0.0", listOf())
        }
        assert(categoryRepository.existsById(id))

    }

    @Test
    fun existsAllById() {
        val ids = List(2) { UUID.randomUUID() }
        val queryMock: Query<DbInsertableCategory> = mock()
        val exprListMock: ExpressionList<DbInsertableCategory> = mock()
        whenever(db.find(DbInsertableCategory::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", ids.asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then {
            listOf(
                DbInsertableCategory(ids[0], "Animals", "Category with animals", 10, "1.0.0"),
                DbInsertableCategory(ids[1], "Cars", "Category with cars", 13, "1.0.0")
            )
        }
        assert(categoryRepository.existsAllById(ids))
    }

    @Test
    fun count() {
        val sqlQueryMock: SqlQuery = mock()
        val sqlRowMock: SqlRow = mock()
        whenever(db.sqlQuery("select count(*) from public.categories")).thenReturn(sqlQueryMock)
        whenever(sqlQueryMock.findOne()).thenReturn(sqlRowMock)
        whenever(sqlRowMock.get("count")).thenReturn(5L)
        assertEquals(5L, categoryRepository.count())
    }
}