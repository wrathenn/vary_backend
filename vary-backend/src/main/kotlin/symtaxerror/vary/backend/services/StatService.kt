package symtaxerror.vary.backend.services

import org.springframework.stereotype.Service
import symtaxerror.vary.backend.models.Card
import symtaxerror.vary.backend.models.Stat
import symtaxerror.vary.backend.repositories.StatRepository

@Service
class StatService(
    private val statRepository: StatRepository
) {
    fun addStat(stat: Stat): Stat =
        statRepository.save(stat)

    fun addStats(stats: Iterable<Stat>): Iterable<Stat> =
        statRepository.saveAll(stats)

    fun getStatsForCard(card: Card) =
        statRepository.findAllByCardId(card.id)

    fun getStatsForAllCards(cards: Iterable<Card>) =
        cards.map { statRepository.findAllByCardId(it.id) }

    fun deleteStat(stat: Stat) {
        statRepository.delete(stat)
    }

    fun deleteStats(stats: Iterable<Stat>) {
        statRepository.deleteAll(stats)
    }
}