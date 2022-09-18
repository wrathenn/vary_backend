package models

import ru.symtaxerror.varyadmin.models.Category
import java.sql.Timestamp

data class Version(
    val code: String,
    val description: String?,
    val createdTs: Timestamp,
    val categories: List<Category>
)