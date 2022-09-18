package symtaxerror.vary.backend.repositories

import io.ebean.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.entities.DbCard
import symtaxerror.vary.backend.entities.DbInsertableCard
import symtaxerror.vary.backend.models.Card
import java.util.*

internal class CardRepositoryImplTest {
    private val db: Database = mock()
    private val cardRepository = CardRepositoryImpl(db)

    @Test
    fun save() {
        val id = UUID.randomUUID()
        val cid = UUID.randomUUID()
        val insCard = DbInsertableCard(id, "Brown cat", cid)
        var card: DbCard? = null

        whenever(db.save(insCard)).then {
            card = DbCard(
                id, "Brown cat", cid, listOf()
            )
            return@then Unit
        }

        whenever(db.find(DbCard::class.java, insCard.id)).then { card }

        val expected = Card(
            id, "Brown cat", cid, listOf()
        )

        assertEquals(
            expected,
            Card(
                id, "Brown cat", cid, listOf()
            )
        )
    }

    @Test
    fun saveAll() {
        val cards: MutableList<DbCard> = mutableListOf()
        val ids = List(2) { UUID.randomUUID() }
        val cids = List(2) { UUID.randomUUID() }

        val insCards = listOf(
            DbInsertableCard(ids[0], "Brown cat", cids[0]),
            DbInsertableCard(ids[1], "Yellow cat", cids[1])
        )

        whenever(db.saveAll(insCards)).then {
            cards.addAll(
                listOf(
                    DbCard(ids[0], "Brown cat", cids[0], listOf()),
                    DbCard(ids[1], "Yellow cat", cids[1], listOf())
                )
            )
            return@then 2
        }

        val queryMock: Query<DbCard> = mock()
        val exprListMock: ExpressionList<DbCard> = mock()
        whenever(db.find(DbCard::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", ids)).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then { cards }

        val expected = listOf(
            Card(ids[0], "Brown cat", cids[0], listOf()),
            Card(ids[1], "Yellow cat", cids[1], listOf())
        )

        assertEquals(
            expected,
            cardRepository.saveAll(
                listOf(
                    Card(ids[0], "Brown cat", cids[0], listOf()),
                    Card(ids[1], "Yellow cat", cids[1], listOf())
                )
            )
        )
    }

    @Test
    fun delete() {
        val card = Card(UUID.randomUUID(), "Brown cat", UUID.randomUUID(), listOf())
        val insCard = DbInsertableCard(UUID.randomUUID(), "Brown cat", UUID.randomUUID())

        whenever(db.delete(insCard)).thenReturn(true)
        cardRepository.delete(card)
    }

    @Test
    fun deleteAll() {
        val ids = List(2) { UUID.randomUUID() }
        val cids = List(2) { UUID.randomUUID() }
        val cards = listOf(
            Card(ids[0], "Brown cat", cids[0], listOf()),
            Card(ids[1], "Yellow cat", cids[1], listOf())
        )
        val insCards = listOf(
            DbInsertableCard(ids[0], "Brown cat", cids[0]),
            DbInsertableCard(ids[1], "Yellow cat", cids[1])
        )
        whenever(db.deleteAll(insCards)).thenReturn(2)
        cardRepository.deleteAll(cards)
    }

    @Test
    fun deleteById() {
        val id = UUID.randomUUID()
        whenever(db.delete(DbInsertableCard::class.java, id)).thenReturn(1)
        cardRepository.deleteById(id)
    }

    @Test
    fun deleteAllById() {
        val ids = List(2) { UUID.randomUUID() }
        whenever(db.deleteAll(ids)).thenReturn(2)
        cardRepository.deleteAllById(ids)
    }

    @Test
    fun findAll() {
        val ids = List(2) { UUID.randomUUID() }
        val cids = List(2) { UUID.randomUUID() }
        val queryMock: Query<DbCard> = mock()
        whenever(db.find(DbCard::class.java)).thenReturn(queryMock)
        whenever(queryMock.findList()).thenReturn(
            listOf(
                DbCard(ids[0], "Brown cat", cids[0], listOf()),
                DbCard(ids[1], "Yellow cat", cids[1], listOf())
            )
        )

        val expected = listOf(
            Card(ids[0], "Brown cat", cids[0], listOf()),
            Card(ids[1], "Yellow cat", cids[1], listOf())
        )
        assertEquals(expected, cardRepository.findAll())
    }

    @Test
    fun findById() {
        val id = UUID.randomUUID()
        val cid = UUID.randomUUID()
        whenever(db.find(DbCard::class.java, id)).thenReturn(
            DbCard(id, "Brown cat", cid, listOf())
        )
        val expected = Card(id, "Brown cat", cid, listOf())
        assertEquals(expected, cardRepository.findById(id))
    }

    @Test
    fun findAllById() {
        val ids = List(2) { UUID.randomUUID() }
        val cids = List(2) { UUID.randomUUID() }
        val queryMock: Query<DbCard> = mock()
        val exprListMock: ExpressionList<DbCard> = mock()
        whenever(db.find(DbCard::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", ids.asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then {
            listOf(
                DbCard(ids[0], "Brown cat", cids[0], listOf()),
                DbCard(ids[1], "Yellow cat", cids[1], listOf())
            )
        }

        val expected = listOf(
            Card(ids[0], "Brown cat", cids[0], listOf()),
            Card(ids[1], "Yellow cat", cids[1], listOf())
        )
        assertEquals(expected, cardRepository.findAllById(ids))
    }

    @Test
    fun existsById() {
        val id = UUID.randomUUID()
        whenever(db.find(DbInsertableCard::class.java, id)).then {
            DbCard(id, "Brown cat", UUID.randomUUID(), listOf())
        }
        assert(cardRepository.existsById(id))
    }

    @Test
    fun existsAllById() {
        val ids = List(2) { UUID.randomUUID() }
        val queryMock: Query<DbInsertableCard> = mock()
        val exprListMock: ExpressionList<DbInsertableCard> = mock()
        whenever(db.find(DbInsertableCard::class.java)).thenReturn(queryMock)
        whenever(queryMock.where()).thenReturn(exprListMock)
        whenever(exprListMock.`in`("id", ids.asIterable())).thenReturn(exprListMock)
        whenever(exprListMock.findList()).then {
            listOf(
                DbInsertableCard(ids[0], "Brown cat", UUID.randomUUID()),
                DbInsertableCard(ids[1], "Yellow cat", UUID.randomUUID())
            )
        }
        assert(cardRepository.existsAllById(ids))
    }

    @Test
    fun count() {
        val sqlQueryMock: SqlQuery = mock()
        val sqlRowMock: SqlRow = mock()
        whenever(db.sqlQuery("select count(*) from public.cards")).thenReturn(sqlQueryMock)
        whenever(sqlQueryMock.findOne()).thenReturn(sqlRowMock)
        whenever(sqlRowMock.get("count")).thenReturn(5L)
        assertEquals(5L, cardRepository.count())
    }
}