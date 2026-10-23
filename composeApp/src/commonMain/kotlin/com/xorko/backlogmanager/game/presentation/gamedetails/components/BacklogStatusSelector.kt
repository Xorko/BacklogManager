package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Playing
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.ToPlay
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.getStatus
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BacklogStatusSelector(
    selectedStatus: KClass<out BackloggedGame>,
    onStatusSelected: (KClass<out BackloggedGame>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statuses = listOf(ToPlay::class, Playing::class, Finished::class)
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth(),
    ) {
        statuses.forEachIndexed { index, status ->
            SegmentedButton(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = statuses.size),
            ) {
                Text(text = status.getStatus().asString())
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        BacklogStatusSelector(
            selectedStatus = ToPlay::class,
            onStatusSelected = {},
        )
    }
}
