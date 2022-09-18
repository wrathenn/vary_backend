package symtaxerror.vary.backend.repositories

import io.ebean.Database
import io.ebean.ExpressionList
import io.ebean.Query
import io.ebean.SqlQuery
import io.ebean.SqlRow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

import symtaxerror.vary.backend.entities.DbInsertableVersion
import symtaxerror.vary.backend.entities.DbVersion
import symtaxerror.vary.backend.models.Version
import java.sql.Timestamp

internal class VersionRepositoryImplTest {
    private val db: Database = mock()
    private val versionRepository = VersionRepositoryImpl(db)

    @Test
    fun save() {
        val insVersion = DbInsertableVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"))
        var version: DbVersion? = null

        whenever(db.save(insVersion)).then {
            version = DbVersion(
                "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            )
            return@then Unit
        }

        whenever(db.find(DbVersion::class.java, "1.0.0")).then { version }

        val expected = Version(
            "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
        )

        assertEquals(
            expected,
            versionRepository.save(Version("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()))
        )
    }

    @Test
    fun saveAll() {
        val versions: MutableList<DbVersion> = mutableListOf()

        val insVersions = listOf(
            DbInsertableVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00")),
            DbInsertableVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"))
        )

        whenever(db.saveAll(insVersions)).then {
            versions.addAll(
                listOf(
                    DbVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
                    DbVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
                )
            )
            return@then 2
        }

        val queryMock: Query<DbVersion> = mock()
        val exprListMock: ExpressionList<DbVersion> = mock()
        whenever(db.find(DbVersion::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("code", listOf("1.0.0", "1.0.1"))).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { versions }

        val expected = listOf(
            Version("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
            Version("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
        )

        assertEquals(
            expected, versionRepository.saveAll(
                listOf(
                    Version("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
                    Version("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
                )
            )
        )
    }

    @Test
    fun delete() {
        val dbVersion = DbInsertableVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"))
        val version = Version(
            "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
        )
        whenever(db.delete(dbVersion)).thenReturn(true)
        versionRepository.delete(version)
    }

    @Test
    fun deleteAll() {
        val versions = listOf(
            Version("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
            Version("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
        )
        val insVersions = listOf(
            DbInsertableVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00")),
            DbInsertableVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"))
        )
        whenever(db.deleteAll(insVersions)).thenReturn(2)
        versionRepository.deleteAll(versions)
    }

    @Test
    fun deleteById() {
        val code = "1.0.0"

        whenever(db.delete(DbInsertableVersion::class.java, code)).thenReturn(1)
        versionRepository.deleteById(code)
    }

    @Test
    fun deleteAllById() {
        val codes = listOf("1.0.0", "1.0.1")
        whenever(db.deleteAll(DbInsertableVersion::class.java, codes)).thenReturn(2)
        versionRepository.deleteAllById(codes)
    }

    @Test
    fun findAll() {
        val queryMock: Query<DbVersion> = mock()
        whenever(db.find(DbVersion::class.java)).thenReturn(queryMock)
        whenever(queryMock.findList()).thenReturn(
            listOf(
                DbVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
                DbVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
            )
        )

        val expected = listOf(
            Version("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
            Version("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
        )
        assertEquals(expected, versionRepository.findAll())
    }

    @Test
    fun findById() {
        whenever(db.find(DbVersion::class.java, "1.0.1")).thenReturn(
            DbVersion(
                "1.0.1",
                "Animals2",
                Timestamp.valueOf("2001-12-14 00:00:00"),
                emptyList()
            )
        )
        val expected = Version("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
        assertEquals(expected, versionRepository.findById("1.0.1"))
    }

    @Test
    fun findAllById() {
        val queryMock: Query<DbVersion> = mock()
        val exprListMock: ExpressionList<DbVersion> = mock()
        whenever(db.find(DbVersion::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("code", listOf("1.0.0", "1.0.1").asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then {
            listOf(
                DbVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
                DbVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
            )
        }

        val expected = listOf(
            Version("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
            Version("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"), emptyList())
        )
        assertEquals(expected, versionRepository.findAllById(listOf("1.0.0", "1.0.1")))
    }

    @Test
    fun existsById() {
        whenever(db.find(DbInsertableVersion::class.java, "1.0.1")).thenReturn(
            DbInsertableVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"))
        )
        assert(versionRepository.existsById("1.0.1"))
    }

    @Test
    fun existsAllById() {
        val queryMock: Query<DbInsertableVersion> = mock()
        val exprListMock: ExpressionList<DbInsertableVersion> = mock()
        whenever(db.find(DbInsertableVersion::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("code", listOf("1.0.0", "1.0.1").asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then {
            listOf(
                DbInsertableVersion("1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00")),
                DbInsertableVersion("1.0.1", "Animals2", Timestamp.valueOf("2001-12-14 00:00:00"))
            )
        }
        assert(versionRepository.existsAllById(listOf("1.0.0", "1.0.1")))
    }

    @Test
    fun count() {
        val sqlQueryMock: SqlQuery = mock()
        val sqlRowMock: SqlRow = mock()
        whenever(db.sqlQuery("select count(*) from public.versions")).thenReturn(sqlQueryMock)
        whenever(sqlQueryMock.findOne()).thenReturn(sqlRowMock)
        whenever(sqlRowMock.get("count")).thenReturn(5L)
        assertEquals(5L, versionRepository.count())
    }
}