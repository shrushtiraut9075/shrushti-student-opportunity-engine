package com.example.model

enum class OpportunityType(val displayName: String) {
    INTERNSHIP("Internship"),
    HACKATHON("Hackathon"),
    SCHOLARSHIP("Scholarship"),
    COMPETITION("Competition"),
    FELLOWSHIP("Fellowship"),
    RESEARCH("Research"),
    WORKSHOP("Workshop"),
    CERTIFICATION("Certification"),
    OPEN_SOURCE("Open Source"),
    PROJECT("Project")
}

enum class DomainType(val displayName: String) {
    AI_ML("AI / Machine Learning"),
    SOFTWARE_DEV("Software Development"),
    WEB_DEV("Web Development"),
    DATA_SCIENCE("Data Science"),
    CYBERSECURITY("Cybersecurity"),
    CLOUD("Cloud Computing"),
    BLOCKCHAIN("Blockchain"),
    ROBOTICS("Robotics"),
    UI_UX("UI / UX Design"),
    RESEARCH("Research & Science")
}

enum class WorkMode(val displayName: String) {
    REMOTE("Remote"),
    HYBRID("Hybrid"),
    ON_SITE("On-site")
}

enum class CompensationType(val displayName: String) {
    PAID("Paid"),
    UNPAID("Unpaid"),
    PRIZE("Prize / Grant")
}

enum class DifficultyLevel(val displayName: String) {
    BEGINNER("Beginner Friendly"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

data class OpportunityItem(
    val id: String,
    val title: String,
    val organization: String,
    val type: OpportunityType,
    val domain: DomainType,
    val description: String,
    val requiredSkills: List<String>,
    val preferredSkills: List<String> = emptyList(),
    val eligibility: String,
    val location: String,
    val workMode: WorkMode,
    val compensationType: CompensationType,
    val stipendOrPrize: String,
    val duration: String,
    val deadline: String, // e.g. "2026-09-25"
    val daysUntilDeadline: Int,
    val difficulty: DifficultyLevel,
    val careerValue: Int = 5, // 1-5
    val portfolioValue: Int = 5, // 1-5
    val learningValue: Int = 4, // 1-5
    val applicationProcess: String = "Submit application form with resume and portfolio link.",
    val preparationDaysRequired: Int = 5,
    val isDemo: Boolean = true
)

enum class ApplicationStatus(val displayName: String) {
    INTERESTED("Interested"),
    SAVED("Saved"),
    APPLIED("Applied"),
    SHORTLISTED("Shortlisted"),
    INTERVIEW("Interview"),
    SELECTED("Selected"),
    REJECTED("Rejected")
}

data class ApplicationEntry(
    val opportunityId: String,
    val status: ApplicationStatus,
    val appliedDate: String = "",
    val notes: String = ""
)

data class ProjectRecommendationItem(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: DifficultyLevel,
    val estimatedDuration: String,
    val targetDomain: DomainType,
    val skillsGained: List<String>,
    val careerRelevanceStars: Int = 5,
    val isCompleted: Boolean = false
)

data class CareerStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val status: String, // "COMPLETED", "IN_PROGRESS", "UPCOMING", "TARGET"
    val actionLabel: String
)
