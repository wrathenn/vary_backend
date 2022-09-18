package symtaxerror.vary.backend.models

import java.util.*

data class Stat(
    val id: UUID,
    val time: Int,
    val penaltyType: Int,
    val isSkipped: Boolean,
    val cardId: UUID
)
