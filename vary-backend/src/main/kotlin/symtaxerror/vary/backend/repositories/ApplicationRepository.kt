package symtaxerror.vary.backend.repositories

import io.ebean.Database
import org.springframework.stereotype.Repository
import symtaxerror.vary.backend.entities.DbApplication
import symtaxerror.vary.backend.entities.DbInsertableApplication
import symtaxerror.vary.backend.models.Application

interface ApplicationRepository {
    fun setDevVersionCode(code: String?): Application
    fun getDevVersionCode(): String?
    fun setCurrentVersionCode(code: String): Application
    fun getCurrentVersionCode(): String
    fun get(): Application
}

@Repository
class ApplicationRepositoryImpl(
    private val db: Database
) : ApplicationRepository {
    private fun getInsertableApp() = db.find(DbInsertableApplication::class.java, 0)
        ?: throw RepoEntityNotFoundException("Application doesn't exist in database")
    private fun getApp() = db.find(DbApplication::class.java, 0)
        ?: throw RepoEntityNotFoundException("Application doesn't exist in database")

    override fun setDevVersionCode(code: String?): Application {
        val app: DbInsertableApplication = getInsertableApp()
        db.update(DbInsertableApplication(app.id, app.currentVersionCode, code))
        return getApp().toModel()
    }

    override fun getDevVersionCode(): String? =
        getInsertableApp().devVersionCode

    override fun setCurrentVersionCode(code: String): Application {
        val app: DbInsertableApplication = getInsertableApp()
        db.update(DbInsertableApplication(app.id, code, app.devVersionCode))
        return getApp().toModel()
    }

    override fun getCurrentVersionCode(): String =
        getInsertableApp().currentVersionCode

    override fun get(): Application =
        getApp().toModel()
}
