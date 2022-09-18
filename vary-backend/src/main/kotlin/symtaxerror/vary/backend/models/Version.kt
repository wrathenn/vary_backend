package symtaxerror.vary.backend.models

import java.sql.Timestamp

data class Version(
    val code: String,
    val description: String?,
    val createdTs: Timestamp,
    val categories: List<Category>
)