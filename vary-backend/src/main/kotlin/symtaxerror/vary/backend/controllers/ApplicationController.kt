package symtaxerror.vary.backend.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import symtaxerror.vary.backend.dto.ApplicationFullTemplate
import symtaxerror.vary.backend.dto.ApplicationTemplate
import symtaxerror.vary.backend.dto.VersionTemplate
import symtaxerror.vary.backend.services.ApplicationService

@RestController
@RequestMapping("application")
class ApplicationController(
    private val applicationService: ApplicationService
) {
    @PostMapping("start")
    fun startDevVersion(@RequestParam(name="devVersionCode") code: String): VersionTemplate =
        applicationService.startDevVersion(code).let { VersionTemplate.fromModel(it) }

    @PostMapping("release")
    fun endDevVersion(): ApplicationTemplate {
        return applicationService.endDevVersion().let { ApplicationTemplate.fromModel(it) }
    }

    @GetMapping
    fun getAppInfo(): ApplicationFullTemplate {
        return applicationService.getAppInfo().let { ApplicationFullTemplate.fromModel(it) }
    }

    @GetMapping("current")
    fun getCurVersion() : VersionTemplate =
        applicationService.getCurVersion().let { VersionTemplate.fromModel(it) }

    @GetMapping("dev")
    fun getDevVersion() : VersionTemplate? =
        applicationService.getDevVersion()?.let { VersionTemplate.fromModel(it) }
}
