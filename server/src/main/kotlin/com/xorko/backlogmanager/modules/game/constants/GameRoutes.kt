package com.xorko.backlogmanager.modules.game.constants

import com.xorko.backlogmanager.modules.game.constants.GameRoutes.ById.Params.GAME_ID

object GameRoutes {
    const val BASE_ROUTE = "/game"

    object QueryParams {
        const val LIMIT = "limit"
        const val OFFSET = "offset"
    }

    object ById {
        const val ROUTE = "/{${GAME_ID}}"

        object Params {
            const val GAME_ID = "gameId"
        }
    }

    object Popular {
        const val ROUTE = "/popular"

        object QueryParams {
            const val LIMIT = "limit"
            const val OFFSET = "offset"
        }
    }
}
