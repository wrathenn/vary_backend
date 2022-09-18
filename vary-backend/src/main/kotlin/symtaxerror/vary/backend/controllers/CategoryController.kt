package symtaxerror.vary.backend.controllers

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import symtaxerror.vary.backend.dto.CategoryNoIdNoCardsTemplate
import symtaxerror.vary.backend.dto.CategoryPropsTemplate
import symtaxerror.vary.backend.dto.CategorySimpleTemplate
import symtaxerror.vary.backend.dto.CategoryTemplate
import symtaxerror.vary.backend.models.Category
import symtaxerror.vary.backend.services.CategoryService
import java.util.*

@RestController
@RequestMapping("category")
class CategoryController(
    val categoryService: CategoryService
) {
    @GetMapping("{id}")
    fun getCategory(@PathVariable(name = "id") id: UUID): CategoryTemplate? =
        categoryService.getCategory(id)?.let { CategoryTemplate.fromModel(it) }

    @PostMapping
    fun createCategory(@RequestBody category: CategoryNoIdNoCardsTemplate): CategoryTemplate =
        categoryService.createCategory(category.toModel(UUID.randomUUID()))
            .let { CategoryTemplate.fromModel(it) }

    @PutMapping
    fun editCategory(@RequestBody changed: CategoryPropsTemplate): CategoryTemplate =
        categoryService.editCategory(changed.toModel())
            .let { CategoryTemplate.fromModel(it) }

    @DeleteMapping
    fun deleteCategory(@RequestBody category: CategoryPropsTemplate) {
        categoryService.deleteCategory(category.toModel())
    }
}