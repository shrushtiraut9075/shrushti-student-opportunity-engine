package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.ui.theme.OxAmber
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo

@Composable
fun OpportunityComparisonScreen(
    repository: StudentRepository,
    opportunities: List<OpportunityItem>,
    onSelectOpportunity: (OpportunityItem) -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val comparisonIds = repository.comparisonIds.value
    val selectedOpps = remember(comparisonIds, opportunities) {
        opportunities.filter { comparisonIds.contains(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("comparison_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(OxIndigo.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CompareArrows, contentDescription = null, tint = OxCyan, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Opportunity Comparison",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Side-by-side evaluation & AI recommendation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            OutlinedButton(onClick = onNavigateToExplore) {
                Text("Add More", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedOpps.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CompareArrows, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No opportunities selected for comparison.", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Tap the Compare icon on any opportunity card in Explore or Dashboard to compare up to 3 side-by-side.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateToExplore) {
                        Text("Browse Opportunities")
                    }
                }
            }
        } else {
            // AI Recommendation Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, OxCyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = OxCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OPPORTUNITYX AI VERDICT", fontSize = 11.sp, fontWeight = FontWeight.Black, color = OxCyan)
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    val topOpp = selectedOpps.maxByOrNull { repository.getMatchFor(it).finalScore } ?: selectedOpps.first()
                    val match = repository.getMatchFor(topOpp)

                    Text(
                        text = "⭐ Best Choice For You: ${topOpp.title} at ${topOpp.organization}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "With a ${match.finalScore}% match score, ${topOpp.title} aligns directly with your ${repository.studentProfile.value.careerGoal} goal and Python foundations. It offers the strongest ROI for your current semester preparation timeline.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontally scrollable comparison table
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                selectedOpps.forEach { opp ->
                    val oppMatch = repository.getMatchFor(opp)
                    Card(
                        modifier = Modifier.width(260.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Close button & Match Pill
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = HighMatchColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${oppMatch.finalScore}% Match",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = HighMatchColor
                                    )
                                }

                                IconButton(
                                    onClick = { repository.toggleComparison(opp.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = opp.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = opp.organization,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            ComparisonRow("Type", opp.type.displayName)
                            ComparisonRow("Domain", opp.domain.displayName)
                            ComparisonRow("Work Mode", opp.workMode.displayName)
                            ComparisonRow("Stipend / Prize", opp.stipendOrPrize)
                            ComparisonRow("Duration", opp.duration)
                            ComparisonRow("Deadline", "${opp.daysUntilDeadline}d left")
                            ComparisonRow("Career Value", "${opp.careerValue}/5 ★")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Required Skills:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(opp.requiredSkills.joinToString(", "), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Missing Skills:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OxAmber)
                            Text(
                                if (oppMatch.missingSkills.isEmpty()) "None! Full match" else oppMatch.missingSkills.joinToString(", "),
                                fontSize = 11.sp,
                                color = if (oppMatch.missingSkills.isEmpty()) HighMatchColor else OxAmber
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { onSelectOpportunity(opp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("View Details", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ComparisonRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}
