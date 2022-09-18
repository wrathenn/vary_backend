package symtaxerror.vary.backend.services

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import symtaxerror.vary.backend.models.Category
import symtaxerror.vary.backend.repositories.CategoryRepository
import symtaxerror.vary.backend.services.exceptions.EntityAlreadyExistsException
import symtaxerror.vary.backend.services.exceptions.EntityNotFoundException
import java.util.*

internal class CategoryServiceTest {
    private val categoryRepository: CategoryRepository = mock()
    private val categoryService = CategoryService(categoryRepository = categoryRepository)

    @Test
    fun createCategorySimple() {
        val category = Category(
            id = UUID.randomUUID(),
            name = "text",
            description = "test category",
            versionCode = "1.0.0",
            difficulty = 0,
            cards = emptyList()
        )

        whenever(categoryRepository.existsById(any())).thenReturn(false)
        whenever(categoryRepository.save(category)).thenReturn(category)
        assertEquals(category, categoryService.createCategory(category))
    }

    @Test
    fun createCategoryExisted() {
        val category = Category(
            id = UUID.randomUUID(),
            name = "text",
            description = "test category",
            versionCode = "1.0.0",
            difficulty = 0,
            cards = emptyList()
        )

        whenever(categoryRepository.existsById(any())).thenReturn(true)
        try {
            categoryService.createCategory(category)
            assert(false)
        } catch (_: EntityAlreadyExistsException) {
        }
    }

    @Test
    fun editCategorySimple() {
        val category = Category(
            id = UUID.randomUUID(),
            name = "text",
            description = "test category",
            versionCode = "1.0.0",
            difficulty = 0,
            cards = emptyList()
        )

        whenever(categoryRepository.existsById(any())).thenReturn(true)
        whenever(categoryRepository.save(category)).thenReturn(category)
        assertEquals(category, categoryService.editCategory(category))
    }

    @Test
    fun editCategoryNotExisted() {
        val category = Category(
            id = UUID.randomUUID(),
            name = "text",
            description = "test category",
            versionCode = "1.0.0",
            difficulty = 0,
            cards = emptyList()
        )

        whenever(categoryRepository.existsById(any())).thenReturn(false)
        try {
            categoryService.editCategory(category)
            assert(false)
        } catch (_: EntityNotFoundException) {
        }
    }

    @Test
    fun deleteCategory() {
        val category = Category(
            id = UUID.randomUUID(),
            name = "text",
            description = "test category",
            versionCode = "1.0.0",
            difficulty = 0,
            cards = emptyList()
        )
        categoryService.deleteCategory(category)
    }
}