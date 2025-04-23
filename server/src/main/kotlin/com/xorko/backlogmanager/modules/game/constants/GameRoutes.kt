package com.xorko.backlogmanager.modules.game.constants

object GameRoutes {
    const val BASE_ROUTE = "/game"

    object Params {
        const val LIMIT = "limit"
        const val OFFSET = "offset"
    }

    object Popular {
        const val ROUTE = "$BASE_ROUTE/popular"

        object Params {
            const val LIMIT = "limit"
            const val OFFSET = "offset"
        }
    }
}
