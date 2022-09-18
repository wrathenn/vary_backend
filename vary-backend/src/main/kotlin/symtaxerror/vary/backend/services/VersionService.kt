package symtaxerror.vary.backend.services

import symtaxerror.vary.backend.repositories.VersionRepository
import org.springframework.stereotype.Service
import symtaxerror.vary.backend.models.Version
import symtaxerror.vary.backend.services.exceptions.EntityNotFoundException
import java.util.*

@Service
class VersionService(
    private val versionRepository: VersionRepository,
) {
    fun getVersion(id: String): Version? =
        versionRepository.findById(id)
    fun editVersion(version: Version): Version {
        if (!versionRepository.existsById(version.code)) {
            throw EntityNotFoundException("Version with code=${version.code} doesn't exist")
        }
        return versionRepository.save(version)
    }
}