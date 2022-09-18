package symtaxerror.vary.backend.repositories

import io.ebean.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.entities.DbInsertableStat
import symtaxerror.vary.backend.entities.DbStat
import symtaxerror.vary.backend.models.Stat
import java.util.*

internal class StatRepositoryImplTest {
    private val db: Database = mock()
    private val statRepository = StatRepositoryImpl(db)

    @Test
    fun save() {
        val insStat = DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        var stat: DbStat? = null

        whenever(db.find(DbStat::class.java, insStat.id)).then { stat }
        whenever(db.save(insStat)).then {
            stat = DbStat(insStat.id, insStat.time, insStat.penaltyType, insStat.isSkipped, insStat.cardId)
            return@then Unit
        }

        val expected = Stat(insStat.id, insStat.time, insStat.penaltyType, insStat.isSkipped, insStat.cardId)
        assertEquals(
            expected,
            statRepository.save(Stat(insStat.id, insStat.time, insStat.penaltyType, insStat.isSkipped, insStat.cardId))
        )
    }

    @Test
    fun saveAll() {
        val insStats = listOf(
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID()),
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        )
        val stat1 = insStats[0]
        val stat2 = insStats[1]

        val stats: MutableList<DbStat> = mutableListOf()
        whenever(db.saveAll(insStats)).then {
            stats.addAll(
                listOf(
                    DbStat(stat1.id, stat1.time, stat1.penaltyType, stat1.isSkipped, stat1.cardId),
                    DbStat(stat2.id, stat2.time, stat2.penaltyType, stat2.isSkipped, stat2.cardId)
                )
            )
            return@then 2
        }

        val queryMock: Query<DbStat> = mock()
        val exprListMock: ExpressionList<DbStat> = mock()
        whenever(db.find(DbStat::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", listOf(stat1.id, stat2.id))).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { stats }

        val expected = listOf(
            Stat(stat1.id, stat1.time, stat1.penaltyType, stat1.isSkipped, stat1.cardId),
            Stat(stat2.id, stat2.time, stat2.penaltyType, stat2.isSkipped, stat2.cardId)
        )

        assertEquals(
            expected, statRepository.saveAll(
                listOf(
                    Stat(stat1.id, stat1.time, stat1.penaltyType, stat1.isSkipped, stat1.cardId),
                    Stat(stat2.id, stat2.time, stat2.penaltyType, stat2.isSkipped, stat2.cardId)
                )
            )
        )
    }

    @Test
    fun delete() {
        val dbStat = DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        val stat = Stat(dbStat.id, 10, 1, true, dbStat.cardId)
        whenever(db.delete(dbStat)).thenReturn(true)
        statRepository.delete(stat)
        assert(true)
    }

    @Test
    fun deleteAll() {
        val insStats = listOf(
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID()),
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        )
        val stat1 = insStats[0]
        val stat2 = insStats[1]

        whenever(db.deleteAll(insStats)).thenReturn(2)

        val stats = listOf(
            Stat(stat1.id, stat1.time, stat1.penaltyType, stat1.isSkipped, stat1.cardId),
            Stat(stat2.id, stat2.time, stat2.penaltyType, stat2.isSkipped, stat2.cardId)
        )

        statRepository.deleteAll(stats)
    }

    @Test
    fun deleteById() {
        val id = UUID.randomUUID()
        whenever(db.delete(DbInsertableStat::class.java, id)).thenReturn(1)
        statRepository.deleteById(id)
    }

    @Test
    fun deleteAllById() {
        val ids = List(2) { UUID.randomUUID() }
        whenever(db.deleteAll(DbInsertableStat::class.java, ids)).thenReturn(ids.size)
        statRepository.deleteAllById(ids)
    }

    @Test
    fun findAll() {
        val insStats = listOf(
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID()),
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        )
        val stat1 = insStats[0]
        val stat2 = insStats[1]
        val queryMock: Query<DbStat> = mock()
        whenever(db.find(DbStat::class.java)).thenReturn(queryMock)
        whenever(queryMock.findList()).thenReturn(
            listOf(
                DbStat(stat1.id, stat1.time, stat1.penaltyType, stat1.isSkipped, stat1.cardId),
                DbStat(stat2.id, stat2.time, stat2.penaltyType, stat2.isSkipped, stat2.cardId)
            )
        )

        val expected = listOf(
            Stat(stat1.id, stat1.time, stat1.penaltyType, stat1.isSkipped, stat1.cardId),
            Stat(stat2.id, stat2.time, stat2.penaltyType, stat2.isSkipped, stat2.cardId)
        )
        assertEquals(expected, statRepository.findAll())
    }

    @Test
    fun findById() {
        val dbStat = DbStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        whenever(db.find(DbStat::class.java, dbStat.id)).thenReturn(
            DbStat(dbStat.id, 10, 1, true, dbStat.cardId)
        )
        val expected = Stat(dbStat.id, 10, 1, true, dbStat.cardId)
        assertEquals(expected, statRepository.findById(dbStat.id))
    }

    @Test
    fun findAllById() {
        val stats = listOf(
            DbStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID()),
            DbStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        )
        val stat1 = stats[0]
        val stat2 = stats[1]

        val queryMock: Query<DbStat> = mock()
        val exprListMock: ExpressionList<DbStat> = mock()
        whenever(db.find(DbStat::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", listOf(stat1.id, stat2.id).asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { stats }

        val expected = listOf(
            Stat(stat1.id, 10, 1, true, stat1.cardId),
            Stat(stat2.id, 10, 1, true, stat2.cardId)
        )

        assertEquals(
            expected, statRepository.findAllById(
                listOf(stat1.id, stat2.id)
            )
        )
    }

    @Test
    fun findAllByCardId() {
        val stats = listOf(
            DbStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID()),
            DbStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        )
        val stat1 = stats[0]

        val queryMock: Query<DbStat> = mock()
        val exprListMock: ExpressionList<DbStat> = mock()
        whenever(db.find(DbStat::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.idEq(stat1.id)).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { listOf(stat1) }

        val expected = listOf(
            Stat(stat1.id, 10, 1, true, stat1.cardId)
        ).asIterable()

        assertEquals(
            expected, statRepository.findAllByCardId(
                stat1.id
            )
        )
    }

    @Test
    fun existsById() {
        val id = UUID.randomUUID()
        whenever(db.find(DbInsertableStat::class.java, id)).thenReturn(
            DbInsertableStat(id, 10, 1, true, UUID.randomUUID())
        )
        assert(statRepository.existsById(id))
    }

    @Test
    fun existsAllById() {
        val stats = listOf(
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID()),
            DbInsertableStat(UUID.randomUUID(), 10, 1, true, UUID.randomUUID())
        )
        val stat1 = stats[0]
        val stat2 = stats[1]

        val queryMock: Query<DbInsertableStat> = mock()
        val exprListMock: ExpressionList<DbInsertableStat> = mock()
        whenever(db.find(DbInsertableStat::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", listOf(stat1.id, stat2.id).asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { stats }
        assert(statRepository.existsAllById(listOf(stat1.id, stat2.id)))
    }

    @Test
    fun count() {
        val sqlQueryMock: SqlQuery = mock()
        val sqlRowMock: SqlRow = mock()
        whenever(db.sqlQuery("select count(*) from public.stats")).thenReturn(sqlQueryMock)
        whenever(sqlQueryMock.findOne()).thenReturn(sqlRowMock)
        whenever(sqlRowMock.get("count")).thenReturn(5L)
        assertEquals(5L, statRepository.count())
    }
}