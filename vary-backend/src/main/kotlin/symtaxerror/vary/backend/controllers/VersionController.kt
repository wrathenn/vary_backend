package symtaxerror.vary.backend.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import symtaxerror.vary.backend.dto.VersionPropsTemplate
import symtaxerror.vary.backend.dto.VersionTemplate
import symtaxerror.vary.backend.dto.VersionSimpleTemplate
import symtaxerror.vary.backend.services.VersionService
import java.util.*

@RestController
@RequestMapping("version")
class VersionController(
    private val versionService: VersionService
) {
    @GetMapping("{id}")
    fun getVersion(@PathVariable(name = "id") id: String): VersionTemplate? =
        versionService.getVersion(id)?.let { VersionTemplate.fromModel(it) }

    @PutMapping
    fun editVersion(@RequestBody version: VersionPropsTemplate): VersionTemplate =
        versionService.editVersion(version.toModel())
            .let { VersionTemplate.fromModel(it) }
}