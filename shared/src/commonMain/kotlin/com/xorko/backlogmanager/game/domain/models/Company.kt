package com.xorko.backlogmanager.game.domain.models

sealed interface Company {
    val id: Long
    val name: String

    data class Developer(
        override val id: Long,
        override val name: String,
    ) : Company

    data class Publisher(
        override val id: Long,
        override val name: String,
    ) : Company
}
