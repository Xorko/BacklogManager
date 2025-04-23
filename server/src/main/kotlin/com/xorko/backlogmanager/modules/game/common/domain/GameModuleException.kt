package com.xorko.backlogmanager.modules.game.common.domain

import com.xorko.backlogmanager.core.domain.exceptions.ModuleException
import io.ktor.http.HttpStatusCode

sealed class GameModuleException(
    message: String,
    statusCode: HttpStatusCode,
) : ModuleException(message = message, statusCode = statusCode) {
    class GameNotFoundException :
        GameModuleException(
            message = "Game not found",
            statusCode = HttpStatusCode.NotFound,
        )
}
