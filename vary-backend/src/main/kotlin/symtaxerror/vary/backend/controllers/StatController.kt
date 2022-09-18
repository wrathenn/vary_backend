package symtaxerror.vary.backend.controllers

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import symtaxerror.vary.backend.dto.StatNoIdTemplate
import symtaxerror.vary.backend.dto.StatPropsTemplate
import symtaxerror.vary.backend.dto.StatTemplate
import symtaxerror.vary.backend.services.StatService
import java.util.*

@RestController
class StatController(
    private val statService: StatService
) {
    @PostMapping("stat")
    fun addStat(@RequestBody stat: StatNoIdTemplate) {
        statService.addStat(stat.toModel(UUID.randomUUID()))
    }

    @PostMapping("stats")
    fun addStats(@RequestBody stats: Iterable<StatNoIdTemplate>) {
        statService.addStats(stats.map { it.toModel(UUID.randomUUID()) })
    }

    @DeleteMapping("stat")
    fun deleteStat(@RequestBody stat: StatPropsTemplate) {
        statService.deleteStat(stat.toModel());
    }

    @DeleteMapping("stats")
    fun deleteStats(@RequestBody stats: Iterable<StatPropsTemplate>) {
        statService.deleteStats(stats.map { it.toModel() })
    }
}