package dao

import dto.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import models.Card
import ru.symtaxerror.varyadmin.exceptions.InputErrorException
import ru.symtaxerror.varyadmin.models.Category

class CategoryStore(
    val id: String
) {
    var category: Category? = null

    fun loadData() {
        val categoryT: CategoryTemplate
        runBlocking {
            categoryT = Connection.client.get("${HOST}/category/${id}").body()
            category = categoryT.toModel()
        }
    }

    fun editIt(category: CategoryPropsTemplate) {
        val newCategory: Category
        runBlocking {
            newCategory = Connection.client.put("${HOST}/category") {
                contentType(ContentType.Application.Json)
                setBody(category)
            }.body<CategoryTemplate>().toModel()
        }
        this.category = newCategory
    }

    fun createCard(card: CardNoIdTemplate) {
        runBlocking {
            Connection.client.post("${HOST}/card") {
                contentType(ContentType.Application.Json)
                setBody(card)
            }
        }
        loadData()
    }

    fun deleteCard(id: String) {
        val delete: Card =
            category?.cards?.first { id == it.id.toString() }
                ?: throw InputErrorException("Карточка с id=$id не найдена")
        runBlocking {
            Connection.client.delete("${HOST}/card") {
                contentType(ContentType.Application.Json)
                setBody(CardPropsTemplate.fromModel(delete))
            }
        }
        loadData()
    }
}