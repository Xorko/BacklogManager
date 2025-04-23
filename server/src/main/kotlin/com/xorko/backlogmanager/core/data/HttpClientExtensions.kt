package com.xorko.backlogmanager.core.data

import com.xorko.backlogmanager.core.domain.ApiException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

suspend inline fun <reified T> handleApiRequest(execute: () -> HttpResponse): T =
    try {
        val response = execute()

        if (response.status.isSuccess()) {
            response.body()
        } else {
            throw ApiException(
                message = "Failed to fetch games",
                statusCode = response.status,
            )
        }
    } catch (e: ClientRequestException) {
        throw ApiException(
            message = "Client request failed: ${e.message}",
            statusCode = HttpStatusCode.InternalServerError,
        )
    } catch (e: Exception) {
        throw ApiException(
            message = "Unexpected error while fetching games: ${e.message}",
            statusCode = HttpStatusCode.InternalServerError,
        )
    }
