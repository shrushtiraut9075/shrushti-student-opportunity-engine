package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.model.DomainType
import com.example.model.OpportunityItem
import com.example.model.OpportunityType
import com.example.ui.components.OpportunityCard
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.MidMatchColor
import com.example.ui.theme.OxAmber
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo
import com.example.ui.theme.OxViolet

data class RadarCluster(
    val title: String,
    val count: Int,
    val avgMatch: Int,
    val icon: ImageVector,
    val color: Color,
    val filterType: OpportunityType? = null,
    val filterDomain: DomainType? = null
)

@Composable
fun OpportunityRadarScreen(
    repository: StudentRepository,
    opportunities: List<OpportunityItem>,
    onSelectOpportunity: (OpportunityItem) -> Unit
) {
    val applications = repository.applications.value
    val comparisonIds = repository.comparisonIds.value

    val clusters = remember(opportunities, repository.studentProfile.value) {
        listOf(
            RadarCluster(
                title = "AI / Machine Learning",
                count = opportunities.count { it.domain == DomainType.AI_ML },
                avgMatch = 88,
                icon = Icons.Default.Psychology,
                color = OxCyan,
                filterDomain = DomainType.AI_ML
            ),
            RadarCluster(
                title = "Software & Web Development",
                count = opportunities.count { it.domain == DomainType.SOFTWARE_DEV || it.domain == DomainType.WEB_DEV },
                avgMatch = 84,
                icon = Icons.Default.Code,
                color = OxIndigo,
                filterDomain = DomainType.SOFTWARE_DEV
            ),
            RadarCluster(
                title = "Hackathons & Sprints",
                count = opportunities.count { it.type == OpportunityType.HACKATHON },
                avgMatch = 86,
                icon = Icons.Default.Star,
                color = Color(0xFFF97316),
                filterType = OpportunityType.HACKATHON
            ),
            RadarCluster(
                title = "Scholarships & Grants",
                count = opportunities.count { it.type == OpportunityType.SCHOLARSHIP },
                avgMatch = 80,
                icon = Icons.Default.School,
                color = OxAmber,
                filterType = OpportunityType.SCHOLARSHIP
            ),
            RadarCluster(
                title = "Open Source Programs",
                count = opportunities.count { it.type == OpportunityType.OPEN_SOURCE },
                avgMatch = 85,
                icon = Icons.Default.Computer,
                color = HighMatchColor,
                filterType = OpportunityType.OPEN_SOURCE
            ),
            RadarCluster(
                title = "Cybersecurity & Systems",
                count = opportunities.count { it.domain == DomainType.CYBERSECURITY },
                avgMatch = 65,
                icon = Icons.Default.Lock,
                color = MidMatchColor,
                filterDomain = DomainType.CYBERSECURITY
            )
        )
    }

    var selectedCluster by remember { mutableStateOf(clusters.first()) }

    val matchingInCluster = remember(selectedCluster, opportunities) {
        opportunities.filter { opp ->
            if (selectedCluster.filterType != null) opp.type == selectedCluster.filterType
            else if (selectedCluster.filterDomain != null) {
                if (selectedCluster.filterDomain == DomainType.SOFTWARE_DEV) {
                    opp.domain == DomainType.SOFTWARE_DEV || opp.domain == DomainType.WEB_DEV
                } else opp.domain == selectedCluster.filterDomain
            } else true
        }.map { opp ->
            opp to repository.getMatchFor(opp)
        }.sortedByDescending { it.second.finalScore }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("opportunity_radar_screen")
    ) {
        // Top Header
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
                        .background(OxIndigo.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Radar, contentDescription = null, tint = OxCyan, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Opportunity Radar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Multi-domain landscape & active student clusters",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Radar cluster tiles (2-column grid)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadarTile(clusters[0], isSelected = selectedCluster == clusters[0], onClick = { selectedCluster = clusters[0] }, modifier = Modifier.weight(1f))
                    RadarTile(clusters[1], isSelected = selectedCluster == clusters[1], onClick = { selectedCluster = clusters[1] }, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadarTile(clusters[2], isSelected = selectedCluster == clusters[2], onClick = { selectedCluster = clusters[2] }, modifier = Modifier.weight(1f))
                    RadarTile(clusters[3], isSelected = selectedCluster == clusters[3], onClick = { selectedCluster = clusters[3] }, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadarTile(clusters[4], isSelected = selectedCluster == clusters[4], onClick = { selectedCluster = clusters[4] }, modifier = Modifier.weight(1f))
                    RadarTile(clusters[5], isSelected = selectedCluster == clusters[5], onClick = { selectedCluster = clusters[5] }, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "${selectedCluster.title} Opportunities (${matchingInCluster.size}):",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // List of items in selected cluster
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(matchingInCluster, key = { it.first.id }) { (opp, match) ->
                val appEntry = applications[opp.id]
                OpportunityCard(
                    opportunity = opp,
                    matchResult = match,
                    isSaved = appEntry?.status == ApplicationStatus.SAVED,
                    isApplied = appEntry?.status == ApplicationStatus.APPLIED || appEntry?.status == ApplicationStatus.INTERVIEW,
                    isInComparison = comparisonIds.contains(opp.id),
                    onViewDetails = { onSelectOpportunity(opp) },
                    onToggleSave = { repository.toggleSaveOpportunity(opp.id) },
                    onApply = { repository.setApplicationStatus(opp.id, ApplicationStatus.APPLIED, "Applied via Radar") },
                    onToggleCompare = { repository.toggleComparison(opp.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun RadarTile(
    cluster: RadarCluster,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) cluster.color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.5.dp,
            if (isSelected) cluster.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(cluster.icon, contentDescription = null, tint = cluster.color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = cluster.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = "${cluster.count} opps • ${cluster.avgMatch}% avg",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
