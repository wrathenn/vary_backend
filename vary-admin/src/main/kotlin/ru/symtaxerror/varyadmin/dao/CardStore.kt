package ru.symtaxerror.varyadmin.dao

import dao.Connection
import dao.HOST
import dto.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import models.Card
import models.Stat
import ru.symtaxerror.varyadmin.exceptions.InputErrorException

class CardStore(
    val id: String
) {
    var card: Card? = null

    fun loadData() {
        val cardT: CardTemplate
        runBlocking {
            cardT = Connection.client.get("${HOST}/cards/${id}").body()
            card = cardT.toModel()
        }
    }

    fun editIt(card: CardPropsTemplate) {
        val newCard: Card
        runBlocking {
            newCard = Connection.client.put("${HOST}/card") {
                contentType(ContentType.Application.Json)
                setBody(card)
            }.body<CardTemplate>().toModel()
        }
        this.card = newCard
    }

    fun createStat(stat: StatNoIdTemplate) {
        runBlocking {
            Connection.client.post("${HOST}/stat") {
                contentType(ContentType.Application.Json)
                setBody(stat)
            }
        }
        loadData()
    }

    fun deleteStat(id: String) {
        val delete: Stat =
            card?.stats?.first { id == it.id.toString() }
                ?: throw InputErrorException("Статистика с таким id не найдена")
        runBlocking {
            Connection.client.delete("${HOST}/stat") {
                contentType(ContentType.Application.Json)
                setBody(StatPropsTemplate.fromModel(delete))
            }
        }
        loadData()
    }
}