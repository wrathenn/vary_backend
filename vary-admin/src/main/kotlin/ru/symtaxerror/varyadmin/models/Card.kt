package models

import java.util.*

data class Card(
    val id: UUID,
    val name: String,
    val categoryId: UUID,
    val stats: List<Stat>
)
