package ru.symtaxerror.varyadmin.views

import dao.AppStore
import ru.symtaxerror.varyadmin.exceptions.InputErrorException
import ru.symtaxerror.varyadmin.exceptions.ModelNotLoadedException
import views.VersionView

class AppView(
    private val appStore: AppStore
) {
    companion object {
        fun start() {
            val it = AppView(AppStore())
            it.appStore.loadData()
            it.mainloop()
        }
    }

    private fun menu() = """
        ----- Приложение -----
        Выберите действие:
        info -- показать информацию о приложении (текущая версия и версия в разработке)
        startdev -- начать разработку новой версии
        editdev -- изменить свойства новой версии
        release -- выпустить новую версию
        exit -- выход
    """.trimIndent()

    fun mainloop() {
        var cmd = ""
        while (cmd != "exit") {
            println(menu())
            cmd = readLine()!!
            try {
                when (cmd) {
                    "info" -> showInfo()
                    "startdev" -> startDev()
                    "editdev" -> editDev()
                    "release" -> release()
                }
            } catch (e: ModelNotLoadedException) {
                println("ОШИБКА -- ${e.message}")
                appStore.loadData()
            } catch (e: InputErrorException) {
                println("ОШИБКА -- ${e.message}")
            }
        }
    }

    private fun showInfo() {
        val curVersion = appStore.curVersion
        val devVersion = appStore.devVersion
        println("Текущая версия:")
        println(curVersion)
        println("Версия в разработке:")
        println(devVersion)
    }

    private fun startDev() {
        println("Введите код новой версии:") // TODO: поймать ошибку
        val code: String = readLine()!!
        appStore.startDev(code)
    }

    private fun editDev() {
        println("Переходим к редактированию дев-версии.")
        val devCode: String? = appStore.devVersion?.code
        if (devCode == null) {
            println("ОШИБКА -- дев-версия еще не начата")
            return
        }
        VersionView.start(devCode)
        appStore.loadData()
    }

    private fun release() {
        println("Выпускаем новую версию.")
        appStore.endDev()
        println("Новая версия выпущена.")
    }
}


