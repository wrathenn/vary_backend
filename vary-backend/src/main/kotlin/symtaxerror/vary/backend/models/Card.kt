package symtaxerror.vary.backend.models

import java.util.*

data class Card(
    val id: UUID,
    val name: String,
    val categoryId: UUID,
    val stats: Iterable<Stat>
)
