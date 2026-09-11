package com.example.engine

import com.example.model.StudentDnaMetrics
import com.example.model.StudentProfile
import kotlin.math.roundToInt

object StudentDNAEngine {

    fun computeDNA(student: StudentProfile): StudentDnaMetrics {
        val techSkillsUpper = student.technicalSkills.map { it.uppercase() }.toSet()

        // Domain Strengths
        val aiSkillsCount = techSkillsUpper.count { it.contains("PYTHON") || it.contains("AI") || it.contains("MACHINE") || it.contains("LEARNING") }
        val devSkillsCount = techSkillsUpper.count { it.contains("REACT") || it.contains("JAVA") || it.contains("JAVASCRIPT") || it.contains("HTML") || it.contains("CSS") || it.contains("GIT") }
        val dsSkillsCount = techSkillsUpper.count { it.contains("SQL") || it.contains("PYTHON") || it.contains("DATA") }
        val cyberSkillsCount = techSkillsUpper.count { it.contains("CYBER") || it.contains("SECURITY") || it.contains("C++") }
        val cloudSkillsCount = techSkillsUpper.count { it.contains("CLOUD") || it.contains("DOCKER") || it.contains("LINUX") }

        val aiMlStrength = (aiSkillsCount * 30 + 30).coerceIn(20, 95)
        val developmentStrength = (devSkillsCount * 18 + 25).coerceIn(25, 95)
        val dataScienceStrength = (dsSkillsCount * 25 + 20).coerceIn(15, 90)
        val cybersecurityStrength = (cyberSkillsCount * 20 + 20).coerceIn(10, 85)
        val cloudStrength = (cloudSkillsCount * 25 + 25).coerceIn(15, 85)

        val technicalStrengthScore = ((aiMlStrength + developmentStrength + dataScienceStrength) / 3).coerceIn(30, 98)

        // Career Alignment
        val careerAlignment = if (student.careerGoal.contains("AI", ignoreCase = true) && aiMlStrength >= 60) 92
        else if (student.careerGoal.contains("Software", ignoreCase = true) && developmentStrength >= 60) 90
        else 82

        // Experience Level
        val yearWeight = when {
            student.currentYear.contains("First", ignoreCase = true) -> 55
            student.currentYear.contains("Second", ignoreCase = true) -> 70
            student.currentYear.contains("Third", ignoreCase = true) -> 85
            else -> 92
        }

        // Project Strength
        val projectScore = (student.completedProjects.size * 35 + 15).coerceIn(40, 95)

        // Learning Potential
        val softBonus = student.softSkills.size * 8
        val learningPotential = (80 + softBonus).coerceIn(75, 98)

        // Overall Opportunity Readiness Score (e.g. 78/100)
        val overallReadiness = (
            technicalStrengthScore * 0.35f +
            careerAlignment * 0.25f +
            yearWeight * 0.15f +
            projectScore * 0.15f +
            learningPotential * 0.10f
        ).roundToInt().coerceIn(30, 96)

        val advice = if (aiMlStrength >= 75 && projectScore < 85) {
            "Your profile is strong for software development and AI opportunities. Adding one machine-learning project could significantly improve your profile."
        } else if (overallReadiness >= 85) {
            "Outstanding readiness! Your profile is primed for competitive tech fellowships and Tier-1 engineering internships."
        } else {
            "Strong fundamentals. Complete target project recommendations and publish code repos to boost your profile readiness above 85%."
        }

        return StudentDnaMetrics(
            aiMlStrength = aiMlStrength,
            developmentStrength = developmentStrength,
            dataScienceStrength = dataScienceStrength,
            cybersecurityStrength = cybersecurityStrength,
            cloudStrength = cloudStrength,
            technicalStrengthScore = technicalStrengthScore,
            careerAlignmentScore = careerAlignment,
            experienceLevelScore = yearWeight,
            projectStrengthScore = projectScore,
            learningPotentialScore = learningPotential,
            overallReadinessScore = overallReadiness,
            diagnosticSummary = advice
        )
    }
}
