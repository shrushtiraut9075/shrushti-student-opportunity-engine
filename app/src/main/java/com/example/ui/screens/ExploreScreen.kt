package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentRepository
import com.example.model.ApplicationStatus
import com.example.model.CompensationType
import com.example.model.DomainType
import com.example.model.OpportunityItem
import com.example.model.OpportunityType
import com.example.model.WorkMode
import com.example.ui.components.OpportunityCard

enum class SortOption(val displayName: String) {
    HIGHEST_MATCH("Best Match"),
    DEADLINE_URGENT("Closing Soon"),
    HIGHEST_PAY("Highest Pay / Prize")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    repository: StudentRepository,
    opportunities: List<OpportunityItem>,
    onSelectOpportunity: (OpportunityItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<OpportunityType?>(null) }
    var selectedDomain by remember { mutableStateOf<DomainType?>(null) }
    var selectedWorkMode by remember { mutableStateOf<WorkMode?>(null) }
    var selectedCompensation by remember { mutableStateOf<CompensationType?>(null) }
    var sortOption by remember { mutableStateOf(SortOption.HIGHEST_MATCH) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val applications = repository.applications.value
    val comparisonIds = repository.comparisonIds.value

    // Calculate match for each
    val matchedList = remember(opportunities, repository.studentProfile.value) {
        opportunities.map { opp ->
            opp to repository.getMatchFor(opp)
        }
    }

    // Filter and Sort
    val filtered = matchedList.filter { (opp, _) ->
        val matchesSearch = searchQuery.isBlank() ||
            opp.title.contains(searchQuery, ignoreCase = true) ||
            opp.organization.contains(searchQuery, ignoreCase = true) ||
            opp.requiredSkills.any { it.contains(searchQuery, ignoreCase = true) } ||
            opp.description.contains(searchQuery, ignoreCase = true)

        val matchesType = selectedType == null || opp.type == selectedType
        val matchesDomain = selectedDomain == null || opp.domain == selectedDomain
        val matchesMode = selectedWorkMode == null || opp.workMode == selectedWorkMode
        val matchesComp = selectedCompensation == null || opp.compensationType == selectedCompensation

        matchesSearch && matchesType && matchesDomain && matchesMode && matchesComp
    }.let { list ->
        when (sortOption) {
            SortOption.HIGHEST_MATCH -> list.sortedByDescending { it.second.finalScore }
            SortOption.DEADLINE_URGENT -> list.sortedBy { it.first.daysUntilDeadline }
            SortOption.HIGHEST_PAY -> list.sortedByDescending { it.first.careerValue }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("explore_screen")
    ) {
        // Search & Filter Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("explore_search_field"),
                placeholder = { Text("Search internships, hackathons, AI, skills...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Sort & Filter row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SortOption.values().forEach { option ->
                        FilterChip(
                            selected = sortOption == option,
                            onClick = { sortOption = option },
                            label = { Text(option.displayName, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { showFilterSheet = true },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filters", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Filters", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Results count and active filter indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${filtered.size} of ${opportunities.size} opportunities",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (selectedType != null || selectedDomain != null || selectedWorkMode != null || selectedCompensation != null) {
                    Button(
                        onClick = {
                            selectedType = null
                            selectedDomain = null
                            selectedWorkMode = null
                            selectedCompensation = null
                        },
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text("Reset Filters", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // List
        if (filtered.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No opportunities match your filter criteria.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    searchQuery = ""
                    selectedType = null
                    selectedDomain = null
                    selectedWorkMode = null
                    selectedCompensation = null
                }) {
                    Text("Clear All Filters")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered, key = { it.first.id }) { (opp, match) ->
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
                            repository.setApplicationStatus(opp.id, ApplicationStatus.APPLIED, "Applied via Explore")
                        },
                        onToggleCompare = { repository.toggleComparison(opp.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    // Comprehensive Filter Modal Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filter Opportunities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedButton(onClick = {
                        selectedType = null
                        selectedDomain = null
                        selectedWorkMode = null
                        selectedCompensation = null
                    }) {
                        Text("Reset All", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Type
                Text("Opportunity Type", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OpportunityType.values().forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = if (selectedType == type) null else type },
                            label = { Text(type.displayName, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Domain
                Text("Domain / Field", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    DomainType.values().forEach { domain ->
                        FilterChip(
                            selected = selectedDomain == domain,
                            onClick = { selectedDomain = if (selectedDomain == domain) null else domain },
                            label = { Text(domain.displayName, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Work Mode
                Text("Work Mode", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    WorkMode.values().forEach { mode ->
                        FilterChip(
                            selected = selectedWorkMode == mode,
                            onClick = { selectedWorkMode = if (selectedWorkMode == mode) null else mode },
                            label = { Text(mode.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showFilterSheet = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Filters (${filtered.size} results)")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
