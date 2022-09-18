package symtaxerror.vary.backend.services

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.models.Card
import symtaxerror.vary.backend.models.Stat
import symtaxerror.vary.backend.repositories.StatRepository
import java.util.UUID

internal class StatServiceTest {
    private val statRepository: StatRepository = mock()
    private val statService = StatService(statRepository = statRepository)

    @Test
    fun addStat() {
        val stat = Stat(
            id = UUID.randomUUID(),
            time = 10,
            penaltyType = 0,
            isSkipped = false,
            cardId = UUID.randomUUID()
        )

        whenever(statRepository.save(stat)).thenReturn(stat)
        assertEquals(stat, statService.addStat(stat))
    }

    @Test
    fun addStats() {
        val stat1 = Stat(
            id = UUID.randomUUID(),
            time = 10,
            penaltyType = 0,
            isSkipped = false,
            cardId = UUID.randomUUID()
        )
        val stat2 = Stat(
            id = UUID.randomUUID(),
            time = 11,
            penaltyType = 0,
            isSkipped = false,
            cardId = UUID.randomUUID()
        )
        val stats = listOf(stat1, stat2)

        whenever(statRepository.saveAll(stats)).thenReturn(stats)
        assertEquals(stats, statService.addStats(stats))
    }

    @Test
    fun getStatsForCard() {
        val card = Card(UUID.randomUUID(), "test card", UUID.randomUUID(), emptyList())
        val stat1 = Stat(
            id = UUID.randomUUID(),
            time = 10,
            penaltyType = 0,
            isSkipped = false,
            cardId = card.id
        )
        val stat2 = Stat(
            id = UUID.randomUUID(),
            time = 11,
            penaltyType = 0,
            isSkipped = false,
            cardId = card.id
        )
        val stats = listOf(stat1, stat2)

        whenever(statRepository.findAllByCardId(card.id)).thenReturn(stats)
        assertEquals(stats, statService.getStatsForCard(card))
    }

    @Test
    fun getStatsForAllCards() {
        val card1 = Card(UUID.randomUUID(), "test card", UUID.randomUUID(), emptyList())
        val stat1 = Stat(
            id = UUID.randomUUID(),
            time = 10,
            penaltyType = 0,
            isSkipped = false,
            cardId = card1.id
        )
        val card2 = Card(UUID.randomUUID(), "test card", UUID.randomUUID(), emptyList())
        val stat2 = Stat(
            id = UUID.randomUUID(),
            time = 11,
            penaltyType = 0,
            isSkipped = false,
            cardId = card2.id
        )

        whenever(statRepository.findAllByCardId(card1.id)).thenReturn(listOf(stat1))
        whenever(statRepository.findAllByCardId(card2.id)).thenReturn(listOf(stat2))

        val expected = listOf(listOf(stat1), listOf(stat2))
        assertEquals(expected, statService.getStatsForAllCards(listOf(card1, card2)))
    }

    @Test
    fun deleteStat() {
        val stat = Stat(
            id = UUID.randomUUID(),
            time = 10,
            penaltyType = 0,
            isSkipped = false,
            cardId = UUID.randomUUID()
        )
        statService.deleteStat(stat)
    }
}