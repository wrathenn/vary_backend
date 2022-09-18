package symtaxerror.vary.backend.services

import symtaxerror.vary.backend.models.Card
import symtaxerror.vary.backend.repositories.CardRepository
import org.springframework.stereotype.Service
import symtaxerror.vary.backend.services.exceptions.EntityAlreadyExistsException
import symtaxerror.vary.backend.services.exceptions.EntityNotFoundException
import java.util.*

@Service
class CardService(
    private val cardRepository: CardRepository
) {
    fun getCard(id: UUID): Card? =
        cardRepository.findById(id)

    fun createCard(card: Card): Card {
        if (cardRepository.existsById(card.id)) {
            throw EntityAlreadyExistsException("Card with id=${card.id} already exists")
        }
        return cardRepository.save(card)
    }

    fun createCards(cards: Iterable<Card>): Iterable<Card> {
        if (cards.any { cardRepository.existsById(it.id) }) {
            throw EntityAlreadyExistsException("Some of these cards already exists")
        }
        return cardRepository.saveAll(cards)
    }

    fun editCard(change: Card): Card {
        if (!cardRepository.existsById(change.id)) {
            throw EntityNotFoundException("Card with id=${change.id} doesn't exist")
        }
        return cardRepository.save(change)
    }

    fun editCards(changedCards: Iterable<Card>): Iterable<Card> {
        if (!cardRepository.existsAllById(changedCards.map { it.id })) {
            throw EntityNotFoundException("Some cards were not found")
        }
        return cardRepository.saveAll(changedCards)
    }

    fun deleteCard(card: Card) {
        cardRepository.delete(card)
    }

    fun deleteCards(cards: Iterable<Card>) {
        cardRepository.deleteAll(cards)
    }
}