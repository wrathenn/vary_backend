package dao

import dto.CategoryNoIdNoCardsTemplate
import dto.CategoryPropsTemplate
import dto.VersionPropsTemplate
import dto.VersionTemplate
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.symtaxerror.varyadmin.models.Category
import models.Version
import ru.symtaxerror.varyadmin.exceptions.InputErrorException

class VersionStore(
    val code: String
) {
    var version: Version? = null

    fun loadData() {
        val versionT: VersionTemplate
        runBlocking {
            versionT = Connection.client.get("${HOST}/version/${code}").body()
            version = versionT.toModel()
        }
    }

    fun editIt(version: VersionPropsTemplate) {
        val newVersion: Version
        runBlocking {
            newVersion = Connection.client.put("${HOST}/version") {
                contentType(ContentType.Application.Json)
                setBody(version)
            }.body<VersionTemplate>().toModel()
        }
        this.version = newVersion
    }

    fun createCategory(category: CategoryNoIdNoCardsTemplate) {
        runBlocking {
            Connection.client.post("${HOST}/category") {
                contentType(ContentType.Application.Json)
                setBody(category)
            }
        }
        loadData()
    }

    fun deleteCategory(id: String) {
        val delete: Category =
            version?.categories?.first { id == it.id.toString() }
                ?: throw InputErrorException("Категория с id=$id не найдена")
        runBlocking {
            Connection.client.delete("${HOST}/category") {
                contentType(ContentType.Application.Json)
                setBody(CategoryPropsTemplate.fromModel(delete))
            }
        }
        loadData()
    }
}