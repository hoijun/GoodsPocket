package goods.pocket.app.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_cancel

@Composable
internal fun DeleteConfirmationDialog(
    title: String,
    body: String,
    confirmLabel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val orange = Color(0xFFFF7445)
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(288.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 0.5.dp,
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 23.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(title, color = Color(0xFF202838), fontSize = 18.sp, lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text(body, modifier = Modifier.width(168.dp), color = Color(0xFF8A8F9B),
                    fontSize = 13.sp, lineHeight = 17.sp, minLines = 2, textAlign = TextAlign.Center)
                Row(Modifier.fillMaxWidth().padding(top = 13.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFC8CDD6)),
                    ) {
                        Text(tr(Res.string.action_cancel), color = Color(0xFF8A8F9B), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = orange),
                    ) {
                        Text(confirmLabel, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
