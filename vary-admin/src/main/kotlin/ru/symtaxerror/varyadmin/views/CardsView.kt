package views

import ru.symtaxerror.varyadmin.dao.CardStore
import dto.StatNoIdTemplate
import ru.symtaxerror.varyadmin.exceptions.InputErrorException
import ru.symtaxerror.varyadmin.exceptions.ModelNotLoadedException
import java.util.*

class CardsView(
    val id: String,
    val cardStore: CardStore
) {
    companion object {
        fun start(id: String) {
            val it = CardsView(id, CardStore(id))
            it.cardStore.loadData()
            it.mainloop()
        }
    }

    private fun menu() = """
        "----- Карточка ${cardStore.card?.name} -- $id -----"
        Выберите действие:
        info -- показать информацию о карточке
        stat_create -- создать новую статистику
        stat_rm -- удалить карточку
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
                    "stat_create" -> createStat()
                    "stat_rm" -> deleteStat()
                }
            } catch (e: ModelNotLoadedException) {
                println("ОШИБКА -- ${e.message}")
                cardStore.loadData()
            } catch (e: InputErrorException) {
                println("ОШИБКА -- ${e.message}")
            }
        }
    }

    private fun printInfo() {
        println(cardStore.card)
    }

    private fun createStat() {
        println("Введите время новой статистики в секундах (целое число):")
        val time: Int = try {
            readLine()!!.toInt()
        }
        catch (e: NumberFormatException) {
            throw InputErrorException("Некорректный ввод")
        }
        println("Введите тип штрафа новой статистики (целое число):")
        val penalty_type: Int = try {
            readLine()!!.toInt()
        } catch (e: NumberFormatException) {
            throw InputErrorException("Некорректный ввод")
        }

        println("Карточку пропустили? (y/n):")
        val isSkipped: Boolean = readLine()!!.let {
            when (it) {
                "y" -> true
                "n" -> false
                else -> {
                    throw InputErrorException("Некорректный ввод")
                }
            }
        }
        val stat = StatNoIdTemplate(time, penalty_type, isSkipped, UUID.fromString(id))
        cardStore.createStat(stat)
    }

    private fun deleteStat() {
        println("Введите айди статистики, которую надо удалить: ")
        val id: String = readLine()!!
        if (id == "exit") {
            return
        }
        cardStore.deleteStat(id)
    }
}