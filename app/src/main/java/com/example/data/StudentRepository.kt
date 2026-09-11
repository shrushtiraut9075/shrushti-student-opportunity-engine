package com.example.data

import android.content.Context
import com.example.engine.RecommendationEngine
import com.example.engine.StudentDNAEngine
import com.example.model.ApplicationEntry
import com.example.model.ApplicationStatus
import com.example.model.MatchCalculationResult
import com.example.model.OpportunityItem
import com.example.model.OpportunityType
import com.example.model.ProjectRecommendationItem
import com.example.model.StudentDnaMetrics
import com.example.model.StudentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentRepository(private val context: Context? = null) {

    private val _studentProfile = MutableStateFlow(StudentProfile())
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    private val _opportunities = MutableStateFlow(DemoOpportunities.list)
    val opportunities: StateFlow<List<OpportunityItem>> = _opportunities.asStateFlow()

    private val _applications = MutableStateFlow<Map<String, ApplicationEntry>>(
        mapOf(
            "opp_1" to ApplicationEntry(opportunityId = "opp_1", status = ApplicationStatus.SAVED, appliedDate = "2026-09-08", notes = "Top priority AI internship. Target Scikit-learn project first."),
            "opp_2" to ApplicationEntry(opportunityId = "opp_2", status = ApplicationStatus.APPLIED, appliedDate = "2026-09-09", notes = "Registered team with 3 classmates."),
            "opp_3" to ApplicationEntry(opportunityId = "opp_3", status = ApplicationStatus.INTERESTED, notes = "Need to review full-stack fellowship syllabus."),
            "opp_16" to ApplicationEntry(opportunityId = "opp_16", status = ApplicationStatus.INTERVIEW, appliedDate = "2026-09-01", notes = "Proposal accepted for stage 2 mentoring.")
        )
    )
    val applications: StateFlow<Map<String, ApplicationEntry>> = _applications.asStateFlow()

    private val _projects = MutableStateFlow(DemoProjects.list)
    val projects: StateFlow<List<ProjectRecommendationItem>> = _projects.asStateFlow()

    private val _comparisonIds = MutableStateFlow<Set<String>>(setOf("opp_1", "opp_3"))
    val comparisonIds: StateFlow<Set<String>> = _comparisonIds.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true) // Default to sleek futuristic dark mode
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.update { !it }
    }

    fun updateProfile(newProfile: StudentProfile) {
        _studentProfile.value = newProfile
    }

    fun resetToDemoAlexSharma() {
        _studentProfile.value = StudentProfile()
    }

    fun setApplicationStatus(opportunityId: String, status: ApplicationStatus, notes: String = "") {
        _applications.update { current ->
            val existing = current[opportunityId]
            val updated = existing?.copy(status = status, notes = if (notes.isNotBlank()) notes else existing.notes)
                ?: ApplicationEntry(opportunityId = opportunityId, status = status, appliedDate = "2026-09-10", notes = notes)
            current + (opportunityId to updated)
        }
    }

    fun removeApplication(opportunityId: String) {
        _applications.update { current -> current - opportunityId }
    }

    fun toggleSaveOpportunity(opportunityId: String) {
        _applications.update { current ->
            val existing = current[opportunityId]
            if (existing?.status == ApplicationStatus.SAVED) {
                current - opportunityId
            } else {
                val updated = existing?.copy(status = ApplicationStatus.SAVED)
                    ?: ApplicationEntry(opportunityId = opportunityId, status = ApplicationStatus.SAVED, appliedDate = "2026-09-10")
                current + (opportunityId to updated)
            }
        }
    }

    fun toggleProjectCompleted(projectId: String) {
        _projects.update { current ->
            current.map { proj ->
                if (proj.id == projectId) {
                    val newState = !proj.isCompleted
                    // If newly completed, add skills to student profile
                    if (newState) {
                        val currentSkills = _studentProfile.value.technicalSkills
                        val newSkills = (currentSkills + proj.skillsGained).distinct()
                        val currentProjects = _studentProfile.value.completedProjects
                        val newProjects = (currentProjects + proj.title).distinct()
                        _studentProfile.update { it.copy(technicalSkills = newSkills, completedProjects = newProjects) }
                    }
                    proj.copy(isCompleted = newState)
                } else proj
            }
        }
    }

    fun toggleComparison(opportunityId: String) {
        _comparisonIds.update { current ->
            if (current.contains(opportunityId)) {
                current - opportunityId
            } else {
                if (current.size >= 3) {
                    // Maximum 3 for side-by-side comparison
                    (current.drop(1) + opportunityId).toSet()
                } else {
                    current + opportunityId
                }
            }
        }
    }

    fun addOpportunity(item: OpportunityItem) {
        _opportunities.update { listOf(item) + it }
    }

    fun deleteOpportunity(opportunityId: String) {
        _opportunities.update { it.filterNot { opp -> opp.id == opportunityId } }
        _applications.update { it - opportunityId }
        _comparisonIds.update { it - opportunityId }
    }

    fun getStudentDNA(): StudentDnaMetrics {
        return StudentDNAEngine.computeDNA(_studentProfile.value)
    }

    fun getMatchFor(opportunity: OpportunityItem): MatchCalculationResult {
        return RecommendationEngine.calculateMatch(_studentProfile.value, opportunity)
    }
}
