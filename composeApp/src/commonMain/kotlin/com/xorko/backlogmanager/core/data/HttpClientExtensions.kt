package com.xorko.backlogmanager.core.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.xorko.backlogmanager.core.domain.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Either<DataError.Remote, T> {
    val response =
        try {
            execute()
        } catch (_: SocketTimeoutException) {
            return DataError.Remote.REQUEST_TIMEOUT.left()
        } catch (_: UnresolvedAddressException) {
            return DataError.Remote.NO_INTERNET.left()
        } catch (_: Exception) {
            coroutineContext.ensureActive() // Avoids CancellationException to be catch
            return DataError.Remote.UNKNOWN.left()
        }

    return responseToEither(response)
}

suspend inline fun <reified T> responseToEither(response: HttpResponse): Either<DataError.Remote, T> =
    when (response.status.value) {
        in 200..299 -> {
            try {
                println("TTT - ${response.body<String>()}")
                response.body<T>().right()
            } catch (e: NoTransformationFoundException) {
                println(e.cause)
                println(e.message)
                DataError.Remote.SERIALIZATION.left()
            }
        }

        408 -> DataError.Remote.REQUEST_TIMEOUT.left()
        429 -> DataError.Remote.TOO_MANY_REQUEST.left()
        in 500..599 -> {
            println("Server error: ${response.status.value}")
            println(response.body<String>())
            DataError.Remote.SERVER.left()
        }
        else -> DataError.Remote.UNKNOWN.left()
    }
