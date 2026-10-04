package goods.pocket.app.presentation.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_retry
import goodspocket.composeapp.generated.resources.events_load_failed
import goodspocket.composeapp.generated.resources.events_loading
import goodspocket.composeapp.generated.resources.events_loading_hint
import goodspocket.composeapp.generated.resources.events_retry_hint

@Composable
internal fun EventsLoadFeedback(isLoading: Boolean, onRetry: () -> Unit) {
    val orange = Color(GoodsPocketVisualTokens.PRIMARY)
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = EventsReferenceMetrics.ScreenHorizontalPadding)
            .padding(bottom = 16.dp).semantics { liveRegion = LiveRegionMode.Polite },
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFEFBF8),
        border = BorderStroke(1.dp, Color(0xFFEFEDEC)),
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 35.dp, bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = tr(
                    if (isLoading) {
                        Res.string.events_loading
                    } else {
                        Res.string.events_load_failed
                    },
                ),
                color = Color(GoodsPocketVisualTokens.INK),
                fontSize = 15.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = tr(
                    if (isLoading) {
                        Res.string.events_loading_hint
                    } else {
                        Res.string.events_retry_hint
                    },
                ),
                color = Color(GoodsPocketVisualTokens.MUTED_INK),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(9.dp))
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp),
                    color = orange,
                    trackColor = Color(GoodsPocketVisualTokens.PRIMARY_CONTAINER),
                )
            } else {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = orange),
                ) {
                    Text(
                        tr(Res.string.action_retry),
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        letterSpacing = 0.sp,
                    )
                }
            }
        }
    }
}
