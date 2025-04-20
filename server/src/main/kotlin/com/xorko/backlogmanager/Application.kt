package com.xorko.backlogmanager

import com.xorko.backlogmanager.di.appModules
import com.xorko.backlogmanager.igdb.proxy.routes.igdbProxyRoute
import com.xorko.backlogmanager.igdb.proxy.services.IgdbProxyService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(appModules)
    }

    // TODO: Improve DI
    val proxyService by inject<IgdbProxyService>()

    routing {
        get("/") {
            call.respondText("Server is up and running!")
        }
        igdbProxyRoute(proxyService = proxyService)
    }
}
