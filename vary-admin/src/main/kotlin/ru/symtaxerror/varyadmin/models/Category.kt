package ru.symtaxerror.varyadmin.models

import models.Card
import java.util.*

data class Category(
    val id: UUID,
    val name: String,
    val description: String,
    val difficulty: Int,
    val versionCode: String,
    val cards: List<Card>
)
