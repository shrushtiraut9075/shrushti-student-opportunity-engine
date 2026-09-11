package com.example.model

data class StudentProfile(
    val fullName: String = "Alex Sharma",
    val educationLevel: String = "Undergraduate",
    val college: String = "Apex Institute of Technology",
    val degree: String = "B.E. Computer Engineering",
    val branch: String = "Computer Engineering",
    val currentYear: String = "Second Year",
    val graduationYear: String = "2028",
    val technicalSkills: List<String> = listOf("Python", "Java", "React", "SQL", "Git", "HTML", "CSS"),
    val softSkills: List<String> = listOf("Problem Solving", "Teamwork", "Communication"),
    val interests: List<String> = listOf("AI/ML", "Web Development", "Data Science"),
    val careerGoal: String = "Software Developer / AI Engineer",
    val preferredWorkMode: WorkMode = WorkMode.REMOTE,
    val preferencePaidOnly: Boolean = true,
    val preferredLocation: String = "Anywhere / Remote",
    val preferredOpportunityTypes: List<OpportunityType> = listOf(
        OpportunityType.INTERNSHIP,
        OpportunityType.HACKATHON,
        OpportunityType.OPEN_SOURCE
    ),
    val completedProjects: List<String> = listOf(
        "Full-Stack Task Manager (React, Node, SQLite)",
        "Data Analysis of Student Grades (Python, Pandas)"
    ),
    val experienceNotes: String = "6 months web dev club lead; contributor to open-source student portal."
)

data class StudentDnaMetrics(
    val aiMlStrength: Int = 90,
    val developmentStrength: Int = 80,
    val dataScienceStrength: Int = 70,
    val cybersecurityStrength: Int = 40,
    val cloudStrength: Int = 55,
    val technicalStrengthScore: Int = 85,
    val careerAlignmentScore: Int = 92,
    val experienceLevelScore: Int = 70,
    val projectStrengthScore: Int = 75,
    val learningPotentialScore: Int = 95,
    val overallReadinessScore: Int = 78,
    val diagnosticSummary: String = "Your profile is strong for software development and AI opportunities. Adding one machine-learning project could significantly improve your profile."
)

data class MatchCalculationResult(
    val opportunityId: String,
    val finalScore: Int,
    val skillMatchScore: Int,
    val careerGoalMatchScore: Int,
    val interestMatchScore: Int,
    val educationMatchScore: Int,
    val projectMatchScore: Int,
    val preferenceMatchScore: Int,
    val deadlineMatchScore: Int,
    val matchLevelLabel: String, // "Excellent Match", "Strong Match", "Good Match", "Moderate Match"
    val matchedSkills: List<String>,
    val missingSkills: List<String>,
    val applicationReadinessPct: Int,
    val whyThisMatches: List<String>,
    val potentialConcerns: List<String>,
    val nextActionRecommendation: String
)

data class NextBestMoveItem(
    val priority: Int,
    val action: String,
    val targetName: String,
    val reason: String,
    val expectedBenefit: String,
    val estimatedPreparationTime: String,
    val relatedOpportunityId: String? = null
)
