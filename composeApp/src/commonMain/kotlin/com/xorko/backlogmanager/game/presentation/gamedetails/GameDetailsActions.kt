package com.xorko.backlogmanager.game.presentation.gamedetails

import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import kotlin.reflect.KClass

interface GameDetailsActions {
    data object OnBack : GameDetailsActions

    data object OnAddToBacklog : GameDetailsActions

    data object OnRemoveFromBacklog : GameDetailsActions

    data class OnStatusSelected(
        val status: KClass<out BackloggedGame>,
    ) : GameDetailsActions
}
