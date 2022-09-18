package models

data class Application(
    val currentVersion: Version,
    val devVersion: Version?,
)