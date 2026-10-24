package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.randomDateTimeBetween
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Game
import kotlin.reflect.KClass

@Composable
fun GameDetailsSheet(
    game: Game,
    backloggedGame: BackloggedGame?,
    onStatusSelected: (KClass<out BackloggedGame>) -> Unit,
    onAddToBacklogPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(start = Dimens.paddingMedium, end = Dimens.paddingMedium)
                    .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GameDetailsHeader(
                game = game,
                modifier = Modifier.padding(top = Dimens.paddingMedium),
            )
            Spacer(Modifier.size(Dimens.paddingMedium))
            BacklogSection(
                backloggedGame = backloggedGame,
                onStatusSelected = onStatusSelected,
                onAddToBacklogPressed = onAddToBacklogPressed,
            )
            Spacer(Modifier.size(Dimens.spacingLarge))
            Text(
                text = game.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = Dimens.paddingMedium),
            )

            Spacer(Modifier.size(Dimens.spacingLarge))
            GameDetailsSection(game = game)
        }
    }
}

@Preview
@Composable
private fun NotInBacklogPreview() {
    BacklogManagerTheme {
        GameDetailsSheet(
            game = generateGame(1),
            backloggedGame = null,
            onStatusSelected = {},
            onAddToBacklogPressed = {},
        )
    }
}

@Preview
@Composable
private fun InBacklogPreview() {
    BacklogManagerTheme {
        val game = generateGame(1)
        val backloggedGame = BackloggedGame.ToPlay(
            id = "1",
            game = game,
            backloggedDate = randomDateTimeBetween(),
        )

        GameDetailsSheet(
            game = game,
            backloggedGame = backloggedGame,
            onStatusSelected = {},
            onAddToBacklogPressed = {},
        )
    }
}