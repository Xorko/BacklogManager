package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.BackloggedGame
import com.xorko.backlogmanager.game.domain.Developer
import com.xorko.backlogmanager.game.domain.Game
import com.xorko.backlogmanager.game.domain.Publisher
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.reflect.KClass

data class BackloggedGameListState(
    val searchQuery: String = "",
    val searchResults: List<BackloggedGame> = backloggedGameList,
    val selectedFilter: KClass<out BackloggedGame>? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
)

// TODO: Remove this as it's only temporary while I don't have an API plugged in
val backloggedGameList =
    (1..100).map {
        val backloggedDate = randomDateTimeBetween()
        val startedDate =
            randomDateTimeBetween(
                start = backloggedDate.toInstant(TimeZone.currentSystemDefault()),
            )
        val finishedDate =
            randomDateTimeBetween(
                start = startedDate.toInstant(TimeZone.currentSystemDefault()),
            )

        when (it % 3) {
            0 ->
                BackloggedGame.ToPlay(
                    game = generateGame(it),
                    backloggedDate = randomDateTimeBetween(),
                )
            1 ->
                BackloggedGame.NowPlaying(
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                )
            else ->
                BackloggedGame.Finished(
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                    finishedDate = finishedDate,
                )
        }
    }

private fun generateGame(index: Int) =
    Game(
        id = index.toLong(),
        name = "Game $index",
        description = "Description $index",
        released = LocalDate(year = 2015, monthNumber = 7, dayOfMonth = 15),
        developers = listOf(Developer(id = index.toLong(), name = "Developer $index")),
        publishers = listOf(Publisher(id = index.toLong(), name = "Publisher $index")),
        coverUrl = "https://picsum.photos/200/300",
        rating = Random.nextInt(until = 6),
        ratingsCount = Random.nextInt(),
    )

fun randomDateTimeBetween(
    start: Instant =
        Clock.System
            .now()
            .minus(value = 5, unit = DateTimeUnit.YEAR, timeZone = TimeZone.currentSystemDefault()),
    end: Instant = Clock.System.now(), // Default: now
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDateTime {
    val randomEpochMillis = Random.nextLong(start.toEpochMilliseconds(), end.toEpochMilliseconds())
    return Instant.fromEpochMilliseconds(randomEpochMillis).toLocalDateTime(timeZone)
}
