package views

import dao.CategoryStore
import dto.CardNoIdTemplate
import dto.CategoryPropsTemplate
import ru.symtaxerror.varyadmin.exceptions.InputErrorException
import ru.symtaxerror.varyadmin.exceptions.ModelNotLoadedException
import java.util.*

class CategoryView(
    val id: String,
    val catStore: CategoryStore
) {
    companion object {
        fun start(id: String) {
            val it = CategoryView(id, CategoryStore(id))
            it.catStore.loadData()
            it.mainloop()
        }
    }

    private fun menu() = """
        "----- Категория ${catStore.category?.name ?: "asd"} -- $id -----"
        Выберите действие:
        info -- показать информацию о категории
        edit -- изменить информацию о категории
        card_create -- создать новую карточку
        card_edit -- редактировать карточку
        card_rm -- удалить карточку
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
                    "card_create" -> createCard()
                    "card_edit" -> editCard()
                    "card_rm" -> deleteCard()
                }
            } catch (e: ModelNotLoadedException) {
                println("ОШИБКА -- ${e.message}")
                catStore.loadData()
            } catch (e: InputErrorException) {
                println("ОШИБКА -- ${e.message}")
            }
        }
    }

    private fun printInfo() {
        println(catStore.category)
    }

    private fun editIt() {
        println("Старое название: ${catStore.category?.name}\nВведите новое название (enter, если изменять не надо):")
        val newName: String = readLine()!!
            .let { it.ifEmpty { catStore.category?.name } }
            ?: throw ModelNotLoadedException("Категория не загружена")
        println("Старое описание: ${catStore.category?.description}\nВведите новое описание (enter, если изменять не надо):")
        val newDescription: String = readLine()!!
            .let { it.ifEmpty { catStore.category?.description } }
            ?: throw ModelNotLoadedException("Категория не загружена")
        println("Старая сложность: ${catStore.category?.difficulty}\nВведите новую сложность (целое число) (enter, если изменять не надо):")
        val newDifficulty: Int = readLine()!!
            .let {
                if (it.isEmpty()) catStore.category?.difficulty
                else try {
                    it.toInt()
                } catch (e: NumberFormatException) {
                    throw InputErrorException("Некорректный ввод")
                }
            }
            ?: throw ModelNotLoadedException("Категория не загружена")
        val edited = catStore.category?.versionCode?.let {
            CategoryPropsTemplate(UUID.fromString(id), newName, newDescription, newDifficulty, it)
        }
            ?: throw ModelNotLoadedException("Категория не загружена")
        catStore.editIt(edited)
    }

    private fun createCard() {
        println("Введите название новой карточки:")
        val name: String = readLine()!!
        if (name == "exit") {
            return
        }
        val card = CardNoIdTemplate(name, UUID.fromString(id))
        catStore.createCard(card)
    }

    private fun editCard() {
        println("Введите айди карточки, которую хотите редактировать: ")
        val id: String = readLine()!!
        if (id == "exit") {
            return
        }
        CardsView.start(id)
    }

    private fun deleteCard() {
        println("Введите айди карточки, которую надо удалить: ")
        val id: String = readLine()!!
        if (id == "exit") {
            return
        }
        catStore.deleteCard(id)
    }
}
