package symtaxerror.vary.backend.services

import org.junit.jupiter.api.Test

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.models.*
import symtaxerror.vary.backend.repositories.*
import symtaxerror.vary.backend.services.exceptions.ApplicationException
import java.sql.Timestamp
import java.util.*

internal class ApplicationServiceTest {
    private val applicationRepository: ApplicationRepository = mock()
    private val versionRepository: VersionRepository = mock()
    private val categoryRepository: CategoryRepository = mock()
    private val cardRepository: CardRepository = mock()
    private val statRepository: StatRepository = mock()

    private val applicationService = ApplicationService(
        applicationRepository, versionRepository, categoryRepository, cardRepository, statRepository
    )

    // Чтобы не сравнивать ID-поля, которые изменяются при копировании
    private fun compareVersionClone(ver1: Version, ver2: Version): Boolean {
        ver1.categories.zip(ver2.categories).forEach { categoryPair ->
            run {
                val c1: Category = categoryPair.first
                val c2: Category = categoryPair.second
                if (c1.name != c2.name ||
                    c1.description != c2.description ||
                    c1.difficulty != c2.difficulty
                ) {
                    return false
                }

                c1.cards.zip(c2.cards).forEach { cardPair ->
                    run {
                        val card1: Card = cardPair.first
                        val card2: Card = cardPair.second
                        if (card1.name != card2.name) {
                            return false
                        }

                        card1.stats.zip(card2.stats).forEach { statPair ->
                            val stat1: Stat = statPair.first
                            val stat2: Stat = statPair.second
                            run {
                                if (stat1.time != stat2.time ||
                                    stat1.penaltyType != stat2.penaltyType ||
                                    stat1.isSkipped != stat2.isSkipped
                                ) {
                                    return false
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    @Test
    fun startDevVersion() {
        val cardIdList = List(20) { UUID.randomUUID() }
        val categoryIdList = List(3) { UUID.randomUUID() }
        val statIdList = List(10) { UUID.randomUUID() }

        val application = Application(
            currentVersion = Version(
                "1.0.0",
                "Animals",
                Timestamp.valueOf("2001-12-13 00:00:00"),
                listOf(
                    Category(
                        categoryIdList[0],
                        "Cats",
                        "Cats category",
                        0,
                        "1.0.0",
                        listOf(
                            Card(
                                cardIdList[0],
                                "Gray cat",
                                categoryIdList[0],
                                emptyList()
                            ),
                            Card(
                                cardIdList[1],
                                "Sussy cat",
                                categoryIdList[0],
                                listOf(
                                    Stat(
                                        statIdList[0],
                                        10,
                                        0,
                                        false,
                                        cardIdList[1]
                                    ),
                                    Stat(
                                        statIdList[1],
                                        11,
                                        0,
                                        true,
                                        cardIdList[1]
                                    )
                                )
                            )
                        )
                    ),
                    Category(
                        categoryIdList[1],
                        "Empty",
                        "Empty category",
                        0,
                        "1.0.0",
                        emptyList()
                    )
                )
            ),
            devVersion = null
        )
        whenever(applicationRepository.getDevVersionCode()).thenReturn(null)
        whenever(applicationRepository.get()).thenReturn(application)
        assert(compareVersionClone(application.currentVersion, applicationService.startDevVersion("1.0.1")))
    }

    @Test
    fun startDevVersionAlreadyStarted() {
        whenever(applicationRepository.getDevVersionCode()).thenReturn("1.0.1")

        try {
            applicationService.startDevVersion("1.1.1")
            assert(false)
        } catch (_: ApplicationException) {
        }
    }

    @Test
    fun endDevVersionNoDevVersion() {
        whenever(applicationRepository.getDevVersionCode()).thenReturn(null)

        try {
            applicationService.endDevVersion()
            assert(false)
        } catch (_: ApplicationException) {
        }
    }
}