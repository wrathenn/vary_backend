package symtaxerror.vary.backend.models

import java.util.*

data class Category(
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
    val cards: Iterable<Card>
)
