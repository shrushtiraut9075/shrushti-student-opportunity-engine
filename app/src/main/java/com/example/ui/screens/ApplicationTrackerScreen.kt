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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentRepository
import com.example.model.ApplicationEntry
import com.example.model.ApplicationStatus
import com.example.model.OpportunityItem
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.MidMatchColor
import com.example.ui.theme.OxAmber
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo
import com.example.ui.theme.UrgentColor

@Composable
fun ApplicationTrackerScreen(
    repository: StudentRepository,
    opportunities: List<OpportunityItem>,
    onSelectOpportunity: (OpportunityItem) -> Unit
) {
    val applications = repository.applications.value
    var selectedTabStatus by remember { mutableStateOf<ApplicationStatus?>(null) }

    val oppMap = remember(opportunities) { opportunities.associateBy { it.id } }

    val filteredApplications = remember(applications, selectedTabStatus) {
        if (selectedTabStatus == null) applications.values.toList()
        else applications.values.filter { it.status == selectedTabStatus }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("tracker_screen")
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
                        .background(OxCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = OxCyan, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Application Tracker",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Kanban workflow: Interested ➔ Applied ➔ Interview ➔ Selected",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Status Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedTabStatus == null,
                    onClick = { selectedTabStatus = null },
                    label = { Text("All (${applications.size})") }
                )
                ApplicationStatus.values().forEach { status ->
                    val count = applications.values.count { it.status == status }
                    FilterChip(
                        selected = selectedTabStatus == status,
                        onClick = { selectedTabStatus = if (selectedTabStatus == status) null else status },
                        label = { Text("${status.displayName} ($count)", fontSize = 12.sp) }
                    )
                }
            }
        }

        if (filteredApplications.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No applications in this category.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredApplications, key = { it.opportunityId }) { app ->
                    val opp = oppMap[app.opportunityId]
                    if (opp != null) {
                        TrackerCard(
                            opportunity = opp,
                            entry = app,
                            onStatusChange = { newStatus ->
                                repository.setApplicationStatus(opp.id, newStatus, app.notes)
                            },
                            onNotesChange = { newNotes ->
                                repository.setApplicationStatus(opp.id, app.status, newNotes)
                            },
                            onRemove = {
                                repository.removeApplication(opp.id)
                            },
                            onViewDetails = { onSelectOpportunity(opp) }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun TrackerCard(
    opportunity: OpportunityItem,
    entry: ApplicationEntry,
    onStatusChange: (ApplicationStatus) -> Unit,
    onNotesChange: (String) -> Unit,
    onRemove: () -> Unit,
    onViewDetails: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var isEditingNotes by remember { mutableStateOf(false) }
    var notesText by remember { mutableStateOf(entry.notes) }

    val statusColor = when (entry.status) {
        ApplicationStatus.INTERESTED -> OxCyan
        ApplicationStatus.SAVED -> OxAmber
        ApplicationStatus.APPLIED -> OxIndigo
        ApplicationStatus.SHORTLISTED -> Color(0xFFF97316)
        ApplicationStatus.INTERVIEW -> Color(0xFF8B5CF6)
        ApplicationStatus.SELECTED -> HighMatchColor
        ApplicationStatus.REJECTED -> UrgentColor
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row 1: Org & Status badge & overflow menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = opportunity.organization,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = entry.status.displayName,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            Text("Move to Status:", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            ApplicationStatus.values().forEach { st ->
                                DropdownMenuItem(
                                    text = { Text(st.displayName, fontSize = 13.sp) },
                                    onClick = {
                                        onStatusChange(st)
                                        showMenu = false
                                    }
                                )
                            }
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Remove from Tracker", color = UrgentColor, fontSize = 13.sp) },
                                onClick = {
                                    onRemove()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = opportunity.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Deadline & Stipend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = OxAmber, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Deadline: ${opportunity.daysUntilDeadline}d left (${opportunity.deadline})", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(opportunity.stipendOrPrize, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = HighMatchColor)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Notes Section
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Personal Notes & Strategy:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { isEditingNotes = !isEditingNotes }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(14.dp))
                        }
                    }

                    if (isEditingNotes) {
                        OutlinedTextField(
                            value = notesText,
                            onValueChange = { notesText = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Completed assignment, waiting for interview schedule...") },
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(onClick = {
                            onNotesChange(notesText)
                            isEditingNotes = false
                        }) {
                            Text("Save Note", fontSize = 11.sp)
                        }
                    } else {
                        Text(
                            text = if (entry.notes.isNotBlank()) entry.notes else "No notes added yet. Tap edit icon to add notes or checklist.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Status Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("View Opportunity", fontSize = 12.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (entry.status != ApplicationStatus.APPLIED) {
                        Button(
                            onClick = { onStatusChange(ApplicationStatus.APPLIED) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Mark Applied", fontSize = 11.sp)
                        }
                    }
                    if (entry.status != ApplicationStatus.INTERVIEW && entry.status == ApplicationStatus.APPLIED) {
                        Button(
                            onClick = { onStatusChange(ApplicationStatus.INTERVIEW) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Got Interview", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
