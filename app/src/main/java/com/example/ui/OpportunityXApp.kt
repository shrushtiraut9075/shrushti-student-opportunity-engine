package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentRepository
import com.example.model.ApplicationStatus
import com.example.model.OpportunityItem
import com.example.model.StudentProfile
import com.example.ui.components.OpportunityDetailSheet
import com.example.ui.components.OxLogo
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ApplicationTrackerScreen
import com.example.ui.screens.CopilotScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DeadlinesScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OpportunityComparisonScreen
import com.example.ui.screens.OpportunityPathScreen
import com.example.ui.screens.OpportunityRadarScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SkillGapScreen
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.OpportunityXTheme
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxIndigo
import kotlinx.coroutines.launch

enum class AppScreen(val label: String, val icon: ImageVector) {
    LANDING("Welcome", Icons.Default.Home),
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    EXPLORE("Explore", Icons.Default.Search),
    SKILL_GAP("Skill Gap", Icons.Default.CrisisAlert),
    CAREER_PATH("Career Path", Icons.Default.Route),
    RADAR("Radar", Icons.Default.Radar),
    COMPARISON("Comparison", Icons.Default.CompareArrows),
    TRACKER("Tracker", Icons.Default.AssignmentTurnedIn),
    DEADLINES("Deadlines", Icons.Default.HourglassBottom),
    COPILOT("Copilot", Icons.Default.Psychology),
    PROFILE("Student DNA", Icons.Default.Person),
    ADMIN("Admin Console", Icons.Default.AdminPanelSettings),
    ONBOARDING("Setup DNA", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpportunityXApp(repository: StudentRepository) {
    val isDarkMode by repository.isDarkMode.collectAsState()
    val studentProfile by repository.studentProfile.collectAsState()
    val opportunities by repository.opportunities.collectAsState()
    val applications by repository.applications.collectAsState()
    val comparisonIds by repository.comparisonIds.collectAsState()

    var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
    var selectedOpportunityForDetails by remember { mutableStateOf<OpportunityItem?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    OpportunityXTheme(darkTheme = isDarkMode) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerContainerColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        OxLogo(size = 40, showFullName = true, showTagline = true)
                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = studentProfile.fullName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = studentProfile.careerGoal,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Readiness: ${repository.getStudentDNA().overallReadinessScore}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = HighMatchColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))

                        val drawerItems = listOf(
                            AppScreen.DASHBOARD,
                            AppScreen.EXPLORE,
                            AppScreen.SKILL_GAP,
                            AppScreen.CAREER_PATH,
                            AppScreen.RADAR,
                            AppScreen.COMPARISON,
                            AppScreen.TRACKER,
                            AppScreen.DEADLINES,
                            AppScreen.COPILOT,
                            AppScreen.PROFILE,
                            AppScreen.ADMIN,
                            AppScreen.LANDING
                        )

                        drawerItems.forEach { screen ->
                            NavigationDrawerItem(
                                icon = { Icon(screen.icon, contentDescription = screen.label) },
                                label = { Text(screen.label) },
                                selected = currentScreen == screen,
                                onClick = {
                                    currentScreen = screen
                                    scope.launch { drawerState.close() }
                                },
                                badge = {
                                    if (screen == AppScreen.COMPARISON && comparisonIds.isNotEmpty()) {
                                        Badge { Text("${comparisonIds.size}") }
                                    } else if (screen == AppScreen.TRACKER && applications.isNotEmpty()) {
                                        Badge { Text("${applications.size}") }
                                    }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    if (currentScreen != AppScreen.LANDING && currentScreen != AppScreen.ONBOARDING) {
                        TopAppBar(
                            title = {
                                OxLogo(size = 32, showFullName = true, showTagline = false)
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier.testTag("drawer_menu_button")
                                ) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            },
                            actions = {
                                // Compare icon badge
                                IconButton(onClick = { currentScreen = AppScreen.COMPARISON }) {
                                    BadgedBox(badge = {
                                        if (comparisonIds.isNotEmpty()) {
                                            Badge { Text("${comparisonIds.size}") }
                                        }
                                    }) {
                                        Icon(
                                            Icons.Default.CompareArrows,
                                            contentDescription = "Compare",
                                            tint = if (comparisonIds.isNotEmpty()) OxCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                // Dark / Light Mode Toggle
                                IconButton(
                                    onClick = { repository.toggleDarkMode() },
                                    modifier = Modifier.testTag("theme_toggle_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                        contentDescription = "Toggle Theme",
                                        tint = OxCyan
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                },
                bottomBar = {
                    if (currentScreen != AppScreen.LANDING && currentScreen != AppScreen.ONBOARDING) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            val bottomNavItems = listOf(
                                AppScreen.DASHBOARD,
                                AppScreen.EXPLORE,
                                AppScreen.SKILL_GAP,
                                AppScreen.TRACKER,
                                AppScreen.COPILOT
                            )

                            bottomNavItems.forEach { screen ->
                                NavigationBarItem(
                                    selected = currentScreen == screen,
                                    onClick = { currentScreen = screen },
                                    icon = {
                                        Icon(screen.icon, contentDescription = screen.label)
                                    },
                                    label = { Text(screen.label, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (currentScreen) {
                        AppScreen.LANDING -> {
                            LandingScreen(
                                onGetStarted = { currentScreen = AppScreen.ONBOARDING },
                                onExplore = { currentScreen = AppScreen.EXPLORE },
                                onUseDemoAlexSharma = {
                                    repository.resetToDemoAlexSharma()
                                    currentScreen = AppScreen.DASHBOARD
                                }
                            )
                        }

                        AppScreen.ONBOARDING -> {
                            OnboardingScreen(
                                initialProfile = studentProfile,
                                onComplete = { updatedProfile ->
                                    repository.updateProfile(updatedProfile)
                                    currentScreen = AppScreen.DASHBOARD
                                },
                                onCancel = { currentScreen = AppScreen.DASHBOARD }
                            )
                        }

                        AppScreen.DASHBOARD -> {
                            DashboardScreen(
                                repository = repository,
                                student = studentProfile,
                                opportunities = opportunities,
                                onSelectOpportunity = { selectedOpportunityForDetails = it },
                                onNavigateToSkillGap = { currentScreen = AppScreen.SKILL_GAP },
                                onNavigateToCareerPath = { currentScreen = AppScreen.CAREER_PATH },
                                onNavigateToRadar = { currentScreen = AppScreen.RADAR },
                                onNavigateToTracker = { currentScreen = AppScreen.TRACKER },
                                onNavigateToCopilot = { currentScreen = AppScreen.COPILOT },
                                onNavigateToExplore = { currentScreen = AppScreen.EXPLORE },
                                onNavigateToDNA = { currentScreen = AppScreen.PROFILE }
                            )
                        }

                        AppScreen.EXPLORE -> {
                            ExploreScreen(
                                repository = repository,
                                opportunities = opportunities,
                                onSelectOpportunity = { selectedOpportunityForDetails = it }
                            )
                        }

                        AppScreen.SKILL_GAP -> {
                            SkillGapScreen(
                                repository = repository,
                                student = studentProfile,
                                opportunities = opportunities
                            )
                        }

                        AppScreen.CAREER_PATH -> {
                            OpportunityPathScreen(
                                student = studentProfile,
                                onStepAction = { step ->
                                    if (step.title.contains("Project", ignoreCase = true)) {
                                        currentScreen = AppScreen.SKILL_GAP
                                    } else if (step.title.contains("Internship", ignoreCase = true) || step.title.contains("Hackathon", ignoreCase = true)) {
                                        currentScreen = AppScreen.EXPLORE
                                    }
                                }
                            )
                        }

                        AppScreen.RADAR -> {
                            OpportunityRadarScreen(
                                repository = repository,
                                opportunities = opportunities,
                                onSelectOpportunity = { selectedOpportunityForDetails = it }
                            )
                        }

                        AppScreen.COMPARISON -> {
                            OpportunityComparisonScreen(
                                repository = repository,
                                opportunities = opportunities,
                                onSelectOpportunity = { selectedOpportunityForDetails = it },
                                onNavigateToExplore = { currentScreen = AppScreen.EXPLORE }
                            )
                        }

                        AppScreen.TRACKER -> {
                            ApplicationTrackerScreen(
                                repository = repository,
                                opportunities = opportunities,
                                onSelectOpportunity = { selectedOpportunityForDetails = it }
                            )
                        }

                        AppScreen.DEADLINES -> {
                            DeadlinesScreen(
                                repository = repository,
                                opportunities = opportunities,
                                onSelectOpportunity = { selectedOpportunityForDetails = it }
                            )
                        }

                        AppScreen.COPILOT -> {
                            CopilotScreen(
                                repository = repository,
                                student = studentProfile,
                                opportunities = opportunities
                            )
                        }

                        AppScreen.PROFILE -> {
                            ProfileScreen(
                                repository = repository,
                                student = studentProfile,
                                onEditProfile = { currentScreen = AppScreen.ONBOARDING }
                            )
                        }

                        AppScreen.ADMIN -> {
                            AdminScreen(
                                repository = repository,
                                opportunities = opportunities
                            )
                        }
                    }

                    // Opportunity Detail Modal Sheet
                    val currentDetail = selectedOpportunityForDetails
                    if (currentDetail != null) {
                        val match = repository.getMatchFor(currentDetail)
                        val appEntry = applications[currentDetail.id]
                        OpportunityDetailSheet(
                            opportunity = currentDetail,
                            matchResult = match,
                            isSaved = appEntry?.status == ApplicationStatus.SAVED,
                            isApplied = appEntry?.status == ApplicationStatus.APPLIED || appEntry?.status == ApplicationStatus.INTERVIEW,
                            isInComparison = comparisonIds.contains(currentDetail.id),
                            onDismiss = { selectedOpportunityForDetails = null },
                            onToggleSave = { repository.toggleSaveOpportunity(currentDetail.id) },
                            onApply = {
                                repository.setApplicationStatus(currentDetail.id, ApplicationStatus.APPLIED, "Applied via Opportunity Details")
                            },
                            onToggleCompare = { repository.toggleComparison(currentDetail.id) },
                            onAddToTrackerStatus = { st ->
                                repository.setApplicationStatus(currentDetail.id, st)
                            }
                        )
                    }
                }
            }
        }
    }
}
