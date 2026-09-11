package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentRepository
import com.example.model.OpportunityItem
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.MidMatchColor
import com.example.ui.theme.OxAmber
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo
import com.example.ui.theme.UrgentColor

@Composable
fun DeadlinesScreen(
    repository: StudentRepository,
    opportunities: List<OpportunityItem>,
    onSelectOpportunity: (OpportunityItem) -> Unit
) {
    val applyToday = opportunities.filter { it.daysUntilDeadline <= 2 }
    val closingSoon = opportunities.filter { it.daysUntilDeadline in 3..5 }
    val thisWeek = opportunities.filter { it.daysUntilDeadline in 6..7 }
    val upcoming = opportunities.filter { it.daysUntilDeadline >= 8 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("deadlines_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF97316).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = Color(0xFFF97316), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Deadline Intelligence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Preparation feasibility alerts & countdown timer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Apply Today Section
            if (applyToday.isNotEmpty()) {
                item {
                    DeadlineHeader("🔥 APPLY TODAY (≤ 2 DAYS)", UrgentColor)
                }
                items(applyToday, key = { it.id }) { opp ->
                    DeadlineOpportunityCard(
                        opportunity = opp,
                        matchScore = repository.getMatchFor(opp).finalScore,
                        isUrgent = true,
                        alertText = if (opp.daysUntilDeadline < opp.preparationDaysRequired)
                            "⚠ Estimated preparation time (${opp.preparationDaysRequired}d) exceeds remaining days (${opp.daysUntilDeadline}d). Fast-track submission!"
                        else "Registration closes soon. Submit your application today.",
                        onSelect = { onSelectOpportunity(opp) }
                    )
                }
            }

            // Closing Soon Section
            if (closingSoon.isNotEmpty()) {
                item {
                    DeadlineHeader("⚡ CLOSING SOON (3–5 DAYS)", OxAmber)
                }
                items(closingSoon, key = { it.id }) { opp ->
                    DeadlineOpportunityCard(
                        opportunity = opp,
                        matchScore = repository.getMatchFor(opp).finalScore,
                        isUrgent = false,
                        alertText = "Requires ${opp.preparationDaysRequired} days preparation. Start your portfolio demo now.",
                        onSelect = { onSelectOpportunity(opp) }
                    )
                }
            }

            // This Week Section
            if (thisWeek.isNotEmpty()) {
                item {
                    DeadlineHeader("📅 THIS WEEK (6–7 DAYS)", OxIndigo)
                }
                items(thisWeek, key = { it.id }) { opp ->
                    DeadlineOpportunityCard(
                        opportunity = opp,
                        matchScore = repository.getMatchFor(opp).finalScore,
                        isUrgent = false,
                        alertText = "Good window to review prerequisites and polish code samples.",
                        onSelect = { onSelectOpportunity(opp) }
                    )
                }
            }

            // Upcoming
            if (upcoming.isNotEmpty()) {
                item {
                    DeadlineHeader("⏳ UPCOMING (8+ DAYS)", HighMatchColor)
                }
                items(upcoming.take(6), key = { it.id }) { opp ->
                    DeadlineOpportunityCard(
                        opportunity = opp,
                        matchScore = repository.getMatchFor(opp).finalScore,
                        isUrgent = false,
                        alertText = "Comfortable preparation schedule. Target related skill-gap project first.",
                        onSelect = { onSelectOpportunity(opp) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun DeadlineHeader(title: String, color: Color) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = color,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun DeadlineOpportunityCard(
    opportunity: OpportunityItem,
    matchScore: Int,
    isUrgent: Boolean,
    alertText: String,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (isUrgent) UrgentColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = opportunity.organization,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = (if (isUrgent) UrgentColor else OxAmber).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "⏱ ${opportunity.daysUntilDeadline} Days Left",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isUrgent) UrgentColor else OxAmber
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = opportunity.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Preparation Feasibility Alert
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isUrgent) Icons.Default.Warning else Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = if (isUrgent) UrgentColor else OxIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = alertText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$matchScore% Match • ${opportunity.stipendOrPrize}",
                    style = MaterialTheme.typography.bodySmall,
                    color = HighMatchColor,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = onSelect,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Apply & View", fontSize = 12.sp)
                }
            }
        }
    }
}
