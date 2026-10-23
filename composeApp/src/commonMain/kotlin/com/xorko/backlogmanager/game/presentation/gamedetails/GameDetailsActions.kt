package com.xorko.backlogmanager.game.presentation.gamedetails

interface GameDetailsActions {
    data object OnBack : GameDetailsActions

    data object OnAddToBacklog : GameDetailsActions

    data object OnRemoveFromBacklog : GameDetailsActions
}
