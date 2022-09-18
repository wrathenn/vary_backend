package symtaxerror.vary.backend.services

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.models.Card
import symtaxerror.vary.backend.repositories.CardRepository
import symtaxerror.vary.backend.services.exceptions.EntityAlreadyExistsException
import symtaxerror.vary.backend.services.exceptions.EntityNotFoundException
import java.util.*

internal class CardServiceTest {
    private val cardRepository: CardRepository = mock()
    private val cardService: CardService = CardService(cardRepository = cardRepository)

    @Test
    fun createCardSimple() {
        val cardId: UUID = UUID.randomUUID()
        val categoryId: UUID = UUID.randomUUID()
        whenever(cardRepository.existsById(any())).thenReturn(false)
        whenever(cardRepository.save(any())).thenReturn(Card(cardId, "testCard", categoryId, emptyList()))

        val input = Card(cardId, "testCard", categoryId, emptyList());
        val expected = Card(cardId, "testCard", categoryId, emptyList())


        assertEquals(expected, cardService.createCard(input))
    }

    @Test
    fun createCardExisted() {
        val cardId: UUID = UUID.randomUUID()
        val categoryId: UUID = UUID.randomUUID()
        whenever(cardRepository.existsById(any())).thenReturn(true)
        whenever(cardRepository.save(any())).thenReturn(Card(cardId, "testCard", categoryId, emptyList()))

        val input = Card(cardId, "testCard", categoryId, emptyList())

        try {
            cardService.createCard(input)
            assert(false)
        } catch (_: EntityAlreadyExistsException) {
        }
    }

    @Test
    fun createCardsSimple() {
        val categoryId = UUID.randomUUID()
        val c1 = Card(UUID.randomUUID(), "card1", categoryId, emptyList())
        val c2 = Card(UUID.randomUUID(), "card2", categoryId, emptyList())
        val cardList = listOf(c1, c2);
        whenever(cardRepository.existsById(any())).thenReturn(false)
        whenever(cardRepository.saveAll(cardList)).thenReturn(cardList)

        assertEquals(cardList, cardService.createCards(cardList))
    }

    @Test
    fun createCardsSomeExists() {
        val categoryId = UUID.randomUUID()
        val c1 = Card(UUID.randomUUID(), "card1", categoryId, emptyList())
        val c2 = Card(UUID.randomUUID(), "card2", categoryId, emptyList())
        val cardList = listOf(c1, c2);
        whenever(cardRepository.existsById(c1.id)).thenReturn(false)
        whenever(cardRepository.existsById(c2.id)).thenReturn(true)
        whenever(cardRepository.saveAll(cardList)).thenReturn(cardList)

        try {
            cardService.createCards(cardList)
            assert(false)
        } catch (_: EntityAlreadyExistsException) {
        }
    }

    @Test
    fun createCardsAllExists() {
        val categoryId = UUID.randomUUID()
        val c1 = Card(UUID.randomUUID(), "card1", categoryId, emptyList())
        val c2 = Card(UUID.randomUUID(), "card2", categoryId, emptyList())
        val cardList = listOf(c1, c2);
        whenever(cardRepository.existsById(any())).thenReturn(true)
        whenever(cardRepository.saveAll(cardList)).thenReturn(cardList)

        try {
            cardService.createCards(cardList)
            assert(false)
        } catch (_: EntityAlreadyExistsException) {
        }
    }

    @Test
    fun editCardSimple() {
        val cardId: UUID = UUID.randomUUID()
        val categoryId: UUID = UUID.randomUUID()
        whenever(cardRepository.existsById(any())).thenReturn(true)
        whenever(cardRepository.save(any())).thenReturn(Card(cardId, "testCard", categoryId, emptyList()))

        val input = Card(cardId, "testCard", categoryId, emptyList());
        val expected = Card(cardId, "testCard", categoryId, emptyList())

        assertEquals(expected, cardService.editCard(input))
    }

    @Test
    fun editCardNotExisted() {
        val cardId: UUID = UUID.randomUUID()
        val categoryId: UUID = UUID.randomUUID()
        whenever(cardRepository.existsById(any())).thenReturn(false)
        whenever(cardRepository.save(any())).thenReturn(Card(cardId, "testCard", categoryId, emptyList()))

        val input = Card(cardId, "testCard", categoryId, emptyList())

        try {
            cardService.editCard(input)
            assert(false)
        } catch (_: EntityNotFoundException) {
        }
    }

    @Test
    fun editCardsSimple() {
        val categoryId = UUID.randomUUID()
        val c1 = Card(UUID.randomUUID(), "card1", categoryId, emptyList())
        val c2 = Card(UUID.randomUUID(), "card2", categoryId, emptyList())
        val cardList = listOf(c1, c2);
        whenever(cardRepository.existsAllById(any())).thenReturn(true)
        whenever(cardRepository.saveAll(cardList)).thenReturn(cardList)

        assertEquals(cardList, cardService.editCards(cardList))
    }

    @Test
    fun editCardsSomeNotExists() {
        val categoryId = UUID.randomUUID()
        val c1 = Card(UUID.randomUUID(), "card1", categoryId, emptyList())
        val c2 = Card(UUID.randomUUID(), "card2", categoryId, emptyList())
        val cardList = listOf(c1, c2);
        whenever(cardRepository.existsAllById(any())).thenReturn(false)
        whenever(cardRepository.saveAll(cardList)).thenReturn(cardList)

        try {
            cardService.editCards(cardList)
            assert(false)
        } catch (_: EntityNotFoundException) {
        }
    }

    @Test
    fun deleteCard() {
        val c1 = Card(UUID.randomUUID(), "card1", UUID.randomUUID(), emptyList())
        cardService.deleteCard(c1)
    }

    @Test
    fun deleteCards() {
        val categoryId = UUID.randomUUID()
        val c1 = Card(UUID.randomUUID(), "card1", categoryId, emptyList())
        val c2 = Card(UUID.randomUUID(), "card2", categoryId, emptyList())
        val cardList = listOf(c1, c2);
        cardService.deleteCards(cardList)
    }
}