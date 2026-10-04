package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_retry
import goodspocket.composeapp.generated.resources.error_operation_failed

@Composable
fun FeatureFeedback(isLoading: Boolean, hasFailure: Boolean, onRetry: () -> Unit) {
    if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
    if (hasFailure) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(tr(Res.string.error_operation_failed), Modifier.weight(1f))
            TextButton(onClick = onRetry) { Text(tr(Res.string.action_retry)) }
        }
    }
}
