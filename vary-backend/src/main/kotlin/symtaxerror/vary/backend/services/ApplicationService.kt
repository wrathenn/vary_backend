package symtaxerror.vary.backend.services

import org.springframework.stereotype.Service
import symtaxerror.vary.backend.models.*
import symtaxerror.vary.backend.repositories.*
import symtaxerror.vary.backend.services.exceptions.ApplicationException
import java.sql.Timestamp
import java.util.*

@Service
class ApplicationService(
    val applicationRepository: ApplicationRepository,
    val versionRepository: VersionRepository,
    val categoryRepository: CategoryRepository,
    val cardRepository: CardRepository,
    val statRepository: StatRepository
) {
    fun startDevVersion(code: String): Version {
        if (applicationRepository.getDevVersionCode() != null) {
            throw ApplicationException("Development version has already started")
        }
        val currentVersion = applicationRepository.get().currentVersion

        val newVersion = Version(
            code,
            currentVersion.description,
            Timestamp(System.currentTimeMillis()),
            currentVersion.categories.map { category ->
                Category(
                    UUID.randomUUID(),
                    category.name,
                    category.description,
                    category.difficulty,
                    code, // links to new version
                    category.cards.map { card ->
                        Card(
                            card.id,
                            card.name,
                            category.id, // links to new version
                            card.stats.map { stat ->
                                Stat(
                                    stat.id,
                                    stat.time,
                                    stat.penaltyType,
                                    stat.isSkipped,
                                    card.id // links to new version
                                )
                            })
                    })
            })

        versionRepository.save(newVersion)
        categoryRepository.saveAll(newVersion.categories)
        cardRepository.saveAll(newVersion.categories.flatMap { it.cards })
        statRepository.saveAll(newVersion.categories.flatMap { category -> category.cards.flatMap { it.stats } })
        applicationRepository.setDevVersionCode(newVersion.code)
        return newVersion
    }

    fun endDevVersion(): Application {
        val devVersionCode: String =
            applicationRepository.getDevVersionCode() ?: throw ApplicationException("No version in development")

        applicationRepository.setCurrentVersionCode(devVersionCode)
        applicationRepository.setDevVersionCode(null)
        return applicationRepository.get()
    }

    fun getAppInfo(): Application =
        applicationRepository.get()

    fun getCurVersion(): Version =
        versionRepository.findById(applicationRepository.getCurrentVersionCode())!!

    fun getDevVersion(): Version? {
        return versionRepository.findById(applicationRepository.getDevVersionCode() ?: return null)
    }
}