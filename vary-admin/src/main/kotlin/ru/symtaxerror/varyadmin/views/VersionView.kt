package views

import dao.VersionStore
import dto.CategoryNoIdNoCardsTemplate
import dto.VersionPropsTemplate
import ru.symtaxerror.varyadmin.exceptions.InputErrorException
import ru.symtaxerror.varyadmin.exceptions.ModelNotLoadedException
import java.sql.Timestamp
import java.util.*

class VersionView(
    val code: String,
    val verStore: VersionStore
) {
    companion object {
        fun start(code: String) {
            val it = VersionView(code, VersionStore(code))
            it.verStore.loadData()
            it.mainloop()
        }
    }

    private fun menu() = """
        "----- Версия $code -----"
        Выберите действие:
        info -- показать информацию о версии
        edit -- изменить информацию о версии
        cat_create -- создать новую категорию
        cat_edit -- изменить категорию
        cat_rm -- удалить категорию
        exit -- выход
    """.trimIndent()

    fun mainloop() {
        var cmd = ""
        while (cmd != "exit") {
            println(menu())
            cmd = readLine()!!
            try {
                when (cmd) {
                    "info" -> printInfo()
                    "edit" -> editIt()
                    "cat_create" -> createCategory()
                    "cat_edit" -> editCategory()
                    "cat_rm" -> deleteCategory()
                }
            } catch (e: ModelNotLoadedException) {
                println("ОШИБКА -- ${e.message}")
                verStore.loadData()
            } catch (e: InputErrorException) {
                println("ОШИБКА -- ${e.message}")
            }
        }
    }

    private fun printInfo() {
        println(verStore.version)
    }

    private fun editIt() {
        println("Старое описание: ${verStore.version?.description}\nВведите новое описание (enter, если изменять не надо):\n")
        val newDescription: String = readLine()!!
            .let { it.ifEmpty { verStore.version?.description } }
            ?: throw ModelNotLoadedException("Версия не загружена")
        println("Старая дата создания: ${verStore.version?.createdTs}\nВведите новую дату (enter, если изменять не надо):\n")
        val newDate: Timestamp = readLine()!!
            .let {
                if (it.isEmpty()) verStore.version?.createdTs
                else try {
                    Timestamp.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    throw InputErrorException("Некорректный ввод")
                }
            }
            ?: throw ModelNotLoadedException("Версия не загружена")
        val edited = VersionPropsTemplate(code, newDescription, newDate)
        verStore.editIt(edited)
    }

    private fun createCategory() {
        println("Введите название новой категории:")
        val name: String = readLine()!!
        println("Введите описание категории $name:")
        val desc: String = readLine()!!
        println("Введите сложность категории: $name")
        val difficulty: Int = try {
            readLine()!!.toInt()
        } catch (e: IllegalArgumentException) {
            throw InputErrorException("Некорректный ввод")
        }
        val category = CategoryNoIdNoCardsTemplate(name, desc, difficulty, code)
        verStore.createCategory(category)
    }

    private fun editCategory() {
        println("Введите айди категории, которую хотите редактировать:")
        val id: String = readLine()!!
        if (id == "exit") {
            return
        }
        println("Переходим к редактированию категории с id=$id")
        CategoryView.start(id)
        verStore.loadData()
    }

    private fun deleteCategory() {
        println("Введите айди категории, которую надо удалить:")
        val id: String = readLine()!!
        if (id == "exit") {
            return
        }
        verStore.deleteCategory(id)
    }
}

