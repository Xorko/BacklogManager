package com.xorko.backlogmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.theme.Background
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Company.Developer
import com.xorko.backlogmanager.game.domain.models.Company.Publisher
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.domain.models.OnlineRating
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListScreen
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListState
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

@Preview
@Composable
private fun SearchBarPreview() {
    BacklogManagerTheme {
        Column(
            modifier = Modifier.background(Background),
            verticalArrangement = spacedBy(10.dp),
        ) {
            GameSearchBar(
                searchQuery = "",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier.fillMaxWidth(),
            )
            GameSearchBar(
                searchQuery = "Caves of Qud",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
fun BackloggedGameListPreview() {
    BacklogManagerTheme {
        BackloggedGameListScreen(
            state =
                BackloggedGameListState(
                    searchResults = backloggedGameList,
                ),
        ) { }
    }
}

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
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = randomDateTimeBetween(),
                )

            1 ->
                BackloggedGame.NowPlaying(
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                )

            else ->
                BackloggedGame.Finished(
                    id = it.toString(),
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
        initialReleaseDate = LocalDate(year = 2015, monthNumber = 7, dayOfMonth = 15),
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
