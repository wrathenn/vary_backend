package symtaxerror.vary.backend.controllers

import org.springframework.web.bind.annotation.*
import symtaxerror.vary.backend.dto.CardNoIdTemplate
import symtaxerror.vary.backend.dto.CardPropsTemplate
import symtaxerror.vary.backend.dto.CardTemplate
import symtaxerror.vary.backend.models.Card
import symtaxerror.vary.backend.services.CardService
import java.util.*

@RestController
class CardController(
    private val cardService: CardService
) {
    @GetMapping("cards/{id}")
    fun getCard(@PathVariable id: UUID): CardTemplate? =
        cardService.getCard(id)?.let { CardTemplate.fromModel(it) }

    @PostMapping("card")
    fun createCard(@RequestBody card: CardNoIdTemplate): CardTemplate {
        val created: Card = cardService.createCard(card.toModel(UUID.randomUUID()))
        return CardTemplate.fromModel(created)
    }

    @PostMapping("cards")
    fun createCards(@RequestBody cards: Iterable<CardNoIdTemplate>): Iterable<CardTemplate> {
        return cardService.createCards(cards.map { it.toModel(UUID.randomUUID()) })
            .map { CardTemplate.fromModel(it) }
    }

    @PutMapping("card")
    fun editCard(@RequestBody card: CardPropsTemplate): CardTemplate {
        return cardService.editCard(card.toModel()).let { CardTemplate.fromModel(it) }
    }

    @PutMapping("cards")
    fun editCards(@RequestBody cards: Iterable<CardPropsTemplate>): Iterable<CardTemplate> {
        return cardService.editCards(cards.map { it.toModel() })
            .map { CardTemplate.fromModel(it) }
    }

    @DeleteMapping("card")
    fun deleteCard(@RequestBody card: CardPropsTemplate) {
        cardService.deleteCard(card.toModel())
    }

    @DeleteMapping("cards")
    fun deleteCards(@RequestBody cards: Iterable<CardPropsTemplate>) {
        cardService.deleteCards(cards.map { it.toModel() })
    }
}