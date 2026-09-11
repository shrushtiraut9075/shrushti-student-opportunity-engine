package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentRepository
import com.example.model.ApplicationStatus
import com.example.model.OpportunityItem
import com.example.model.OpportunityType
import com.example.model.StudentProfile
import com.example.ui.components.DailyBriefCard
import com.example.ui.components.NextBestMoveCard
import com.example.ui.components.OpportunityCard
import com.example.ui.components.StudentDnaRadar
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.OxAmber
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo
import com.example.ui.theme.UrgentColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    repository: StudentRepository,
    student: StudentProfile,
    opportunities: List<OpportunityItem>,
    onSelectOpportunity: (OpportunityItem) -> Unit,
    onNavigateToSkillGap: () -> Unit,
    onNavigateToCareerPath: () -> Unit,
    onNavigateToRadar: () -> Unit,
    onNavigateToTracker: () -> Unit,
    onNavigateToCopilot: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToDNA: () -> Unit
) {
    val studentDNA = repository.getStudentDNA()
    val applications = repository.applications.value
    val comparisonIds = repository.comparisonIds.value

    // Sorted opportunities by match score
    val rankedOpportunities = remember(student, opportunities) {
        opportunities.map { opp ->
            opp to repository.getMatchFor(opp)
        }.sortedByDescending { it.second.finalScore }
    }

    val topMatch = rankedOpportunities.firstOrNull()

    var selectedFilterType by remember { mutableStateOf<OpportunityType?>(null) }

    val filteredList = if (selectedFilterType == null) {
        rankedOpportunities
    } else {
        rankedOpportunities.filter { it.first.type == selectedFilterType }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("dashboard_screen")
    ) {
        // Welcome Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome back, ${student.fullName}! 👋",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${student.degree} • ${student.careerGoal}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Profile Completion Bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Profile Completion", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text("92%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HighMatchColor)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { 0.92f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = HighMatchColor,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Stats Row (Section 5)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DashboardStatTile(
                number = "${rankedOpportunities.count { it.second.finalScore >= 80 }}",
                label = "Top Matches",
                icon = Icons.Default.AutoAwesome,
                color = HighMatchColor,
                modifier = Modifier.weight(1f)
            )
            DashboardStatTile(
                number = "${opportunities.count { it.daysUntilDeadline <= 4 }}",
                label = "Closing Soon",
                icon = Icons.Default.Schedule,
                color = UrgentColor,
                modifier = Modifier.weight(1f)
            )
            DashboardStatTile(
                number = "${applications.count { it.value.status == ApplicationStatus.APPLIED || it.value.status == ApplicationStatus.INTERVIEW }}",
                label = "In Progress",
                icon = Icons.Default.Work,
                color = OxCyan,
                modifier = Modifier.weight(1f)
            )
            DashboardStatTile(
                number = "AI/ML",
                label = "Target Goal",
                icon = Icons.Default.Route,
                color = OxIndigo,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today's Opportunity Brief
        DailyBriefCard(
            studentName = student.fullName.split(" ").firstOrNull() ?: "Alex",
            onNextAction = {
                if (topMatch != null) onSelectOpportunity(topMatch.first)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // NEXT BEST MOVE CARD (The Core Innovation)
        NextBestMoveCard(
            topOpportunity = topMatch?.first,
            matchResult = topMatch?.second,
            onViewOpportunity = {
                if (topMatch != null) onSelectOpportunity(topMatch.first)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Student DNA Summary Card
        StudentDnaRadar(
            dna = studentDNA,
            isExpanded = true,
            modifier = Modifier.clickable { onNavigateToDNA() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Navigation Engine Shortcuts
        Text(
            text = "AI INTELLIGENCE SUITE",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShortcutCard(
                title = "Skill Gap Analysis",
                subtitle = "Readiness % & Projects",
                icon = Icons.Default.CrisisAlert,
                color = OxIndigo,
                onClick = onNavigateToSkillGap,
                modifier = Modifier.weight(1f)
            )
            ShortcutCard(
                title = "Career Path",
                subtitle = "Where I Am ➔ Goal",
                icon = Icons.Default.Route,
                color = OxCyan,
                onClick = onNavigateToCareerPath,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShortcutCard(
                title = "Opportunity Radar",
                subtitle = "Multi-domain Explorer",
                icon = Icons.Default.Radar,
                color = HighMatchColor,
                onClick = onNavigateToRadar,
                modifier = Modifier.weight(1f)
            )
            ShortcutCard(
                title = "AI Copilot",
                subtitle = "24/7 Career Advisor",
                icon = Icons.Default.Psychology,
                color = Color(0xFFF97316),
                onClick = onNavigateToCopilot,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recommended Opportunities Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Recommended For You",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Ranked by 7-Factor Weighted Match Algorithm",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = onNavigateToExplore,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("View All", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedFilterType == null,
                onClick = { selectedFilterType = null },
                label = { Text("All (${rankedOpportunities.size})") }
            )
            listOf(
                OpportunityType.INTERNSHIP,
                OpportunityType.HACKATHON,
                OpportunityType.FELLOWSHIP,
                OpportunityType.SCHOLARSHIP,
                OpportunityType.OPEN_SOURCE
            ).forEach { type ->
                FilterChip(
                    selected = selectedFilterType == type,
                    onClick = { selectedFilterType = if (selectedFilterType == type) null else type },
                    label = { Text(type.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Top 5 Opportunity Cards
        filteredList.take(5).forEach { (opp, match) ->
            val appEntry = applications[opp.id]
            OpportunityCard(
                opportunity = opp,
                matchResult = match,
                isSaved = appEntry?.status == ApplicationStatus.SAVED,
                isApplied = appEntry?.status == ApplicationStatus.APPLIED || appEntry?.status == ApplicationStatus.INTERVIEW,
                isInComparison = comparisonIds.contains(opp.id),
                onViewDetails = { onSelectOpportunity(opp) },
                onToggleSave = { repository.toggleSaveOpportunity(opp.id) },
                onApply = {
                    repository.setApplicationStatus(opp.id, ApplicationStatus.APPLIED, "Applied via OpportunityX")
                },
                onToggleCompare = { repository.toggleComparison(opp.id) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DashboardStatTile(
    number: String,
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = number,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
