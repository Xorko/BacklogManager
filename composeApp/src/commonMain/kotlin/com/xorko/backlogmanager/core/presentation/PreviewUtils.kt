package com.xorko.backlogmanager.core.presentation

import com.xorko.backlogmanager.game.domain.models.Company.Developer
import com.xorko.backlogmanager.game.domain.models.Company.Publisher
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.domain.models.OnlineRating
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

internal fun generateGame(index: Int) =
    Game(
        id = index.toLong(),
        name = "Game $index",
        description = "Description $index",
        initialReleaseDate = LocalDate(year = 2015, month = 7, day = 15),
        developers = listOf(Developer(id = index.toLong(), name = "Developer $index")),
        publishers = listOf(Publisher(id = index.toLong(), name = "Publisher $index")),
        coverUrl = "https://picsum.photos/200/300",
        criticsRating =
            OnlineRating(
                value = Random.nextDouble(until = 5.0),
                count = Random.nextInt(until = 6),
            ),
        usersRating =
            OnlineRating(
                value = Random.nextDouble(until = 5.0),
                count = Random.nextInt(until = 6),
            ),
        platforms = emptyList(),
        artworksUrls = emptyList(),
        genres = emptyList(),
    )

internal fun randomDateTimeBetween(
    start: Instant =
        Clock.System
            .now()
            .minus(value = 5, unit = DateTimeUnit.YEAR, timeZone = TimeZone.UTC),
    end: Instant = Clock.System.now(), // Default: now
    timeZone: TimeZone = TimeZone.UTC,
): LocalDateTime {
    val randomEpochMillis = Random.nextLong(start.toEpochMilliseconds(), end.toEpochMilliseconds())
    return Instant.fromEpochMilliseconds(randomEpochMillis).toLocalDateTime(timeZone)
}