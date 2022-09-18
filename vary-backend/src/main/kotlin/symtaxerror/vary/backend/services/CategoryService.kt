package symtaxerror.vary.backend.services

import org.springframework.stereotype.Service
import symtaxerror.vary.backend.models.Category
import symtaxerror.vary.backend.repositories.CategoryRepository
import symtaxerror.vary.backend.services.exceptions.EntityAlreadyExistsException
import symtaxerror.vary.backend.services.exceptions.EntityNotFoundException
import java.util.*

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategory(id: UUID) : Category? {
        return categoryRepository.findById(id)
    }

    fun createCategory(category: Category): Category {
        if (categoryRepository.existsById(category.id)) {
            throw EntityAlreadyExistsException("Category with id=${category.id} already exists")
        }
        return categoryRepository.save(category)
    }

    fun editCategory(change: Category): Category {
        if (!categoryRepository.existsById(change.id)) {
            throw EntityNotFoundException("Category with id=$change.id doesn't exist")
        }
        return categoryRepository.save(change)
    }

    fun deleteCategory(category: Category) =
        categoryRepository.delete(category)
}