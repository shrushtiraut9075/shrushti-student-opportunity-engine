package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxCyanLight
import com.example.ui.theme.OxIndigo
import com.example.ui.theme.OxViolet

@Composable
fun OxLogo(
    modifier: Modifier = Modifier,
    size: Int = 36,
    showTagline: Boolean = false,
    showFullName: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // OX Icon Badge
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape((size * 0.28).dp))
                .background(
                    Brush.linearGradient(
                        listOf(OxCyan, OxIndigo, OxViolet)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "OX",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = (size * 0.42).sp,
                letterSpacing = (-0.5).sp
            )
        }

        if (showFullName) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Opportunity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "X",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = OxCyanLight
                    )
                }
                if (showTagline) {
                    Text(
                        text = "AI-Powered Student Engine",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
