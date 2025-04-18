package com.xorko.backlogmanager.core.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.core.domain.logging.Logger
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
            Logger.e("An unknown error occurred during the request")
            return DataError.Remote.UNKNOWN.left()
        }

    return responseToEither(response)
}

suspend inline fun <reified T> responseToEither(response: HttpResponse): Either<DataError.Remote, T> =
    when (response.status.value) {
        in 200..299 -> {
            try {
                response.body<T>().right()
            } catch (e: NoTransformationFoundException) {
                Logger.e("Error parsing response: ${e.message}", throwable = e)
                DataError.Remote.SERIALIZATION.left()
            }
        }

        408 -> {
            Logger.e("Request timed out")
            DataError.Remote.REQUEST_TIMEOUT.left()
        }
        429 -> {
            Logger.w("Too many requests sent to server in a short period")
            DataError.Remote.TOO_MANY_REQUEST.left()
        }
        in 500..599 -> {
            Logger.e("Server error: ${response.status.value}")
            DataError.Remote.SERVER.left()
        }
        else -> {
            Logger.e("An unknown error occurred during the request")
            DataError.Remote.UNKNOWN.left()
        }
    }
