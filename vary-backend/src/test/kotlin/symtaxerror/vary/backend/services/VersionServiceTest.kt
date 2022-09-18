package symtaxerror.vary.backend.services

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.models.Version
import symtaxerror.vary.backend.repositories.VersionRepository
import symtaxerror.vary.backend.services.exceptions.EntityNotFoundException
import java.sql.Timestamp

internal class VersionServiceTest {
    private val versionRepository: VersionRepository = mock()
    private val versionService = VersionService(versionRepository = versionRepository)

    @Test
    fun editVersionSimple() {
        whenever(versionRepository.existsById(any())).thenReturn(true)
        val version = Version(
            code = "1.0.0",
            description = "test version",
            createdTs = Timestamp.valueOf("2001-12-13 00:00:00"),
            categories = emptyList()
        )

        versionService.editVersion(version)
    }

    @Test
    fun editVersionNotExisted() {
        whenever(versionRepository.existsById(any())).thenReturn(false)
        val version = Version(
            code = "1.0.0",
            description = "test version",
            createdTs = Timestamp.valueOf("2001-12-13 00:00:00"),
            categories = emptyList()
        )
        try {
            versionService.editVersion(version)
            assert(false)
        } catch (_: EntityNotFoundException) {
        }
    }
}