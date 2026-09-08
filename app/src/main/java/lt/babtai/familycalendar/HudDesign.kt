package lt.babtai.familycalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Shared visual foundation for the Family Calendar HUD-style dark interface. */
object FamilyHud {
    val Background = Color(0xFF0B0D10)
    val Panel = Color(0xFF15191F)
    val Primary = Color(0xFF7DD3FC)
    val Text = Color(0xFFF2F4F7)
    val Muted = Color(0xFF9AA3AF)
}

@Composable
fun HudBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF17212A), FamilyHud.Background),
                    radius = 1200f
                )
            )
    ) { content() }
}

fun Modifier.hudPanel() = this
    .background(FamilyHud.Panel, RoundedCornerShape(18.dp))
    .border(1.dp, FamilyHud.Primary.copy(alpha = 0.16f), RoundedCornerShape(18.dp))
