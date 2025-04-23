package com.xorko.backlogmanager.modules.game.games.data.api

object IgdbRoutes {
    const val BASE_URL = "https://api.igdb.com/v4/"

    object TokenRetrieval {
        const val URL = "https://id.twitch.tv/oauth2/token"

        object QueryParams {
            const val CLIENT_ID = "client_id"
            const val CLIENT_SECRET = "client_secret"
            const val GRANT_TYPE = "grant_type"

            object Values {
                const val CLIENT_CREDENTIALS = "client_credentials"
            }
        }
    }

    object Games {
        const val ROUTE = "${BASE_URL}games"
    }

    object Popularity {
        const val ROUTE = "${BASE_URL}popularity_primitives"
    }

    object Headers {
        const val CLIENT_ID = "Client-ID"
    }
}
