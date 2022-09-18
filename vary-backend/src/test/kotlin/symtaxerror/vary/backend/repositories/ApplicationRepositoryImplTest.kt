package symtaxerror.vary.backend.repositories

import io.ebean.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.entities.DbApplication
import symtaxerror.vary.backend.entities.DbInsertableApplication
import symtaxerror.vary.backend.entities.DbVersion
import symtaxerror.vary.backend.models.*
import java.sql.Timestamp

internal class ApplicationRepositoryImplTest {
    private val database: Database = mock();
    private val applicationRepository = ApplicationRepositoryImpl(database)

    private var application: Application = Application(
        currentVersion = Version(
            "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
        ),
        devVersion = null
    )

    init {
        whenever(database.find(DbInsertableApplication::class.java, 0)).thenReturn(
            DbInsertableApplication.fromModel(application)
        )
        whenever(database.find(DbApplication::class.java, 0)).thenReturn(
            DbApplication.fromModel(application)
        )
    }

    @Test
    fun setDevVersionCode() {
        var application = DbApplication(
            currentVersion = DbVersion(
                "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            ),
            devVersion = null
        )
        val insApplication = DbInsertableApplication(
            currentVersionCode = "1.0.0",
            devVersionCode = null
        )
        val new = DbInsertableApplication(insApplication.id, insApplication.currentVersionCode, "1.0.1")

        whenever(database.find(DbInsertableApplication::class.java, 0)).then { insApplication }
        whenever(database.find(DbApplication::class.java, 0)).then { application }
        whenever(database.update(new)).then {
            application = DbApplication(
                application.id,
                application.currentVersion,
                DbVersion("1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList())
            )
            return@then Unit
        }

        val expected = Application(
            currentVersion = Version(
                "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            ),
            devVersion = Version(
                "1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            )
        )

        assertEquals(expected, applicationRepository.setDevVersionCode("1.0.1"))
    }

    @Test
    fun getDevVersionCodeNull() {
        val insApplication = DbInsertableApplication(
            currentVersionCode = "1.0.0",
            devVersionCode = null
        )
        whenever(database.find(DbInsertableApplication::class.java, 0)).then { insApplication }

        assertEquals(null, applicationRepository.getDevVersionCode())
    }

    @Test
    fun getDevVersionCode() {
        val insApplication = DbInsertableApplication(
            currentVersionCode = "1.0.0",
            devVersionCode = "1.0.1"
        )
        whenever(database.find(DbInsertableApplication::class.java, 0)).then { insApplication }

        assertEquals("1.0.1", applicationRepository.getDevVersionCode())
    }

    @Test
    fun setCurrentVersionCode() {
        var application = DbApplication(
            currentVersion = DbVersion(
                "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            ),
            devVersion = DbVersion(
                "1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            )
        )
        val insApplication = DbInsertableApplication(
            currentVersionCode = "1.0.0",
            devVersionCode = "1.0.1"
        )
        val new = DbInsertableApplication(insApplication.id, "1.0.1", "1.0.1")

        whenever(database.find(DbInsertableApplication::class.java, 0)).then { insApplication }
        whenever(database.find(DbApplication::class.java, 0)).then { application }
        whenever(database.update(new)).then {
            application = DbApplication(
                application.id,
                DbVersion("1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()),
                application.devVersion
            )
            return@then Unit
        }

        val expected = Application(
            currentVersion = Version(
                "1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            ),
            devVersion = Version(
                "1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            )
        )

        assertEquals(expected, applicationRepository.setCurrentVersionCode("1.0.1"))
    }

    @Test
    fun getCurrentVersionCode() {
        val insApplication = DbInsertableApplication(
            currentVersionCode = "1.0.0",
            devVersionCode = "1.0.1"
        )
        whenever(database.find(DbInsertableApplication::class.java, 0)).then { insApplication }

        assertEquals("1.0.0", applicationRepository.getCurrentVersionCode())
    }

    @Test
    fun get() {
        val application = DbApplication(
            currentVersion = DbVersion(
                "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            ),
            devVersion = DbVersion(
                "1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            )
        )

        whenever(database.find(DbApplication::class.java, 0)).then { application }

        val expected = Application(
            currentVersion = Version(
                "1.0.0", "Animals", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            ),
            devVersion = Version(
                "1.0.1", "test", Timestamp.valueOf("2001-12-13 00:00:00"), emptyList()
            )
        )

        assertEquals(expected, applicationRepository.get())
    }
}