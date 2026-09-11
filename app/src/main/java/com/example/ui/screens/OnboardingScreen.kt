package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OpportunityType
import com.example.model.StudentProfile
import com.example.model.WorkMode
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    initialProfile: StudentProfile,
    onComplete: (StudentProfile) -> Unit,
    onCancel: () -> Unit
) {
    var step by remember { mutableStateOf(1) }

    // Form fields
    var fullName by remember { mutableStateOf(initialProfile.fullName) }
    var college by remember { mutableStateOf(initialProfile.college) }
    var degree by remember { mutableStateOf(initialProfile.degree) }
    var branch by remember { mutableStateOf(initialProfile.branch) }
    var currentYear by remember { mutableStateOf(initialProfile.currentYear) }
    var graduationYear by remember { mutableStateOf(initialProfile.graduationYear) }

    val selectedTechSkills = remember { mutableStateListOf(*initialProfile.technicalSkills.toTypedArray()) }
    val selectedSoftSkills = remember { mutableStateListOf(*initialProfile.softSkills.toTypedArray()) }
    val selectedInterests = remember { mutableStateListOf(*initialProfile.interests.toTypedArray()) }

    var careerGoal by remember { mutableStateOf(initialProfile.careerGoal) }
    var preferredWorkMode by remember { mutableStateOf(initialProfile.preferredWorkMode) }
    var preferencePaidOnly by remember { mutableStateOf(initialProfile.preferencePaidOnly) }
    var preferredLocation by remember { mutableStateOf(initialProfile.preferredLocation) }
    val selectedOppTypes = remember { mutableStateListOf(*initialProfile.preferredOpportunityTypes.toTypedArray()) }

    fun loadAlexSharma() {
        fullName = "Alex Sharma"
        college = "Apex Institute of Technology"
        degree = "B.E. Computer Engineering"
        branch = "Computer Engineering"
        currentYear = "Second Year"
        graduationYear = "2028"
        selectedTechSkills.clear()
        selectedTechSkills.addAll(listOf("Python", "Java", "React", "SQL", "Git", "HTML", "CSS"))
        selectedSoftSkills.clear()
        selectedSoftSkills.addAll(listOf("Problem Solving", "Teamwork", "Communication"))
        selectedInterests.clear()
        selectedInterests.addAll(listOf("AI/ML", "Web Development", "Data Science"))
        careerGoal = "Software Developer / AI Engineer"
        preferredWorkMode = WorkMode.REMOTE
        preferencePaidOnly = true
        preferredLocation = "Anywhere / Remote"
        selectedOppTypes.clear()
        selectedOppTypes.addAll(listOf(OpportunityType.INTERNSHIP, OpportunityType.HACKATHON, OpportunityType.OPEN_SOURCE))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Step Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (step > 1) step-- else onCancel()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Student DNA Setup",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Step $step of 5",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Quick Demo Fill Action
                    OutlinedButton(
                        onClick = { loadAlexSharma() },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null, tint = OxCyan, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Fill Demo", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { step / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        // Body Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            when (step) {
                1 -> {
                    Text(
                        text = "1. Personal & Academic Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "OpportunityX tailors eligibility to your academic level and university course.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_full_name"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = college,
                        onValueChange = { college = it },
                        label = { Text("College / University") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = degree,
                        onValueChange = { degree = it },
                        label = { Text("Degree (e.g. B.Tech / B.E. / BCA / M.Tech)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("Branch / Major (e.g. Computer Science, AI, ECE)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = currentYear,
                            onValueChange = { currentYear = it },
                            label = { Text("Current Year") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = graduationYear,
                            onValueChange = { graduationYear = it },
                            label = { Text("Grad Year") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }

                2 -> {
                    Text(
                        text = "2. Skills & Technical Knowledge",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap to select your core skills or add customized capabilities.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Technical Skills (35% Match Weight)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val availableTech = listOf(
                        "Python", "Java", "C++", "React", "JavaScript", "TypeScript",
                        "SQL", "Git", "HTML", "CSS", "Machine Learning", "Docker",
                        "Cloud Computing", "Cybersecurity", "Blockchain", "UI/UX"
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        availableTech.forEach { skill ->
                            val isSelected = selectedTechSkills.contains(skill)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedTechSkills.remove(skill)
                                    else selectedTechSkills.add(skill)
                                },
                                label = { Text(skill, fontSize = 12.sp) },
                                leadingIcon = if (isSelected) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                } else null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Soft & Communication Skills",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val availableSoft = listOf(
                        "Problem Solving", "Teamwork", "Communication",
                        "Leadership", "Critical Thinking", "Adaptability"
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        availableSoft.forEach { skill ->
                            val isSelected = selectedSoftSkills.contains(skill)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedSoftSkills.remove(skill)
                                    else selectedSoftSkills.add(skill)
                                },
                                label = { Text(skill, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                3 -> {
                    Text(
                        text = "3. Interests & Passions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Select what areas you are most excited to build in.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val interestsList = listOf(
                        "AI/ML", "Web Development", "Data Science",
                        "Cybersecurity", "Cloud Computing", "Blockchain",
                        "Robotics", "UI/UX Design", "Open Source"
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        interestsList.forEach { interest ->
                            val isSelected = selectedInterests.contains(interest)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedInterests.remove(interest)
                                    else selectedInterests.add(interest)
                                },
                                label = { Text(interest, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                modifier = Modifier.height(40.dp)
                            )
                        }
                    }
                }

                4 -> {
                    Text(
                        text = "4. Career Goal & Ambitions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "What is your target role upon graduation?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = careerGoal,
                        onValueChange = { careerGoal = it },
                        label = { Text("Target Career Goal") },
                        placeholder = { Text("e.g. Software Developer / AI Engineer") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val sampleGoals = listOf(
                        "Software Developer / AI Engineer",
                        "Full-Stack Web Engineer",
                        "Data Scientist / ML Researcher",
                        "Cybersecurity Analyst",
                        "Cloud & DevOps Architect"
                    )

                    Text(
                        text = "Quick Select:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    sampleGoals.forEach { g ->
                        OutlinedButton(
                            onClick = { careerGoal = g },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(g, fontSize = 12.sp)
                        }
                    }
                }

                5 -> {
                    Text(
                        text = "5. Opportunity Preferences",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Set your preferred work modes and opportunity types.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Preferred Opportunity Types:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        OpportunityType.values().forEach { type ->
                            val isSelected = selectedOppTypes.contains(type)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedOppTypes.remove(type)
                                    else selectedOppTypes.add(type)
                                },
                                label = { Text(type.displayName, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Work Mode:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WorkMode.values().forEach { mode ->
                            FilterChip(
                                selected = preferredWorkMode == mode,
                                onClick = { preferredWorkMode = mode },
                                label = { Text(mode.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Paid Opportunities Only", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text("Only highlight roles offering stipends, salaries, or cash grants", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = preferencePaidOnly,
                            onCheckedChange = { preferencePaidOnly = it }
                        )
                    }
                }
            }
        }

        // Bottom Actions Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { step-- },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Back")
                    }
                } else {
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                }

                Button(
                    onClick = {
                        if (step < 5) {
                            step++
                        } else {
                            val finalProfile = initialProfile.copy(
                                fullName = fullName.ifBlank { "Alex Sharma" },
                                college = college,
                                degree = degree,
                                branch = branch,
                                currentYear = currentYear,
                                graduationYear = graduationYear,
                                technicalSkills = if (selectedTechSkills.isEmpty()) listOf("Python", "React", "SQL") else selectedTechSkills.toList(),
                                softSkills = selectedSoftSkills.toList(),
                                interests = if (selectedInterests.isEmpty()) listOf("AI/ML", "Web Development") else selectedInterests.toList(),
                                careerGoal = careerGoal.ifBlank { "Software Developer / AI Engineer" },
                                preferredWorkMode = preferredWorkMode,
                                preferencePaidOnly = preferencePaidOnly,
                                preferredLocation = preferredLocation,
                                preferredOpportunityTypes = if (selectedOppTypes.isEmpty()) listOf(OpportunityType.INTERNSHIP, OpportunityType.HACKATHON) else selectedOppTypes.toList()
                            )
                            onComplete(finalProfile)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("onboarding_continue_btn")
                ) {
                    Text(if (step == 5) "Generate Student DNA" else "Next Step")
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
