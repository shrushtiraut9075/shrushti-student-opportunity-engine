package com.example.engine

import com.example.model.DifficultyLevel
import com.example.model.MatchCalculationResult
import com.example.model.NextBestMoveItem
import com.example.model.OpportunityItem
import com.example.model.StudentProfile
import kotlin.math.roundToInt

object RecommendationEngine {

    /**
     * Calculates the exact match score breakdown based on:
     * - Skill Match: 35%
     * - Career Goal Match: 20%
     * - Interest Match: 15%
     * - Education Match: 10%
     * - Project/Experience Match: 10%
     * - Preference Match: 5%
     * - Deadline Feasibility: 5%
     */
    fun calculateMatch(
        student: StudentProfile,
        opportunity: OpportunityItem
    ): MatchCalculationResult {
        val studentSkillsUpper = student.technicalSkills.map { it.trim().uppercase() }.toSet()
        val studentSoftSkillsUpper = student.softSkills.map { it.trim().uppercase() }.toSet()
        val allStudentSkills = studentSkillsUpper + studentSoftSkillsUpper

        val reqSkills = opportunity.requiredSkills
        val matchedReqSkills = reqSkills.filter { skill ->
            val sUpper = skill.trim().uppercase()
            allStudentSkills.any { it.contains(sUpper) || sUpper.contains(it) }
        }
        val missingReqSkills = reqSkills.filter { skill ->
            val sUpper = skill.trim().uppercase()
            allStudentSkills.none { it.contains(sUpper) || sUpper.contains(it) }
        }

        // 1. Skill Match (35% weight)
        val skillFraction = if (reqSkills.isNotEmpty()) {
            matchedReqSkills.size.toFloat() / reqSkills.size.toFloat()
        } else 1.0f
        val skillRawScore = (skillFraction * 100f).roundToInt()
        val skillWeighted = skillFraction * 35.0f

        // 2. Career Goal Match (20% weight)
        val studentGoalUpper = student.careerGoal.uppercase()
        val domainUpper = opportunity.domain.name.uppercase()
        val titleUpper = opportunity.title.uppercase()
        val careerMatchFraction = when {
            studentGoalUpper.contains("AI") && (domainUpper.contains("AI") || titleUpper.contains("AI") || titleUpper.contains("DATA")) -> 1.0f
            studentGoalUpper.contains("SOFTWARE") && (domainUpper.contains("SOFTWARE") || domainUpper.contains("WEB") || domainUpper.contains("CLOUD")) -> 1.0f
            studentGoalUpper.contains("DATA") && (domainUpper.contains("DATA") || domainUpper.contains("AI")) -> 1.0f
            studentGoalUpper.contains("CYBER") && domainUpper.contains("CYBER") -> 1.0f
            studentGoalUpper.contains("CLOUD") && domainUpper.contains("CLOUD") -> 1.0f
            studentGoalUpper.contains("DESIGN") || studentGoalUpper.contains("UI") && domainUpper.contains("UI_UX") -> 1.0f
            else -> 0.65f
        }
        val careerGoalRawScore = (careerMatchFraction * 100f).roundToInt()
        val careerWeighted = careerMatchFraction * 20.0f

        // 3. Interest Match (15% weight)
        val studentInterestsUpper = student.interests.map { it.uppercase() }
        val interestFraction = when {
            studentInterestsUpper.any { domainUpper.contains(it) || it.contains(domainUpper) } -> 1.0f
            studentInterestsUpper.any { titleUpper.contains(it) || it.contains(opportunity.domain.displayName.uppercase()) } -> 0.9f
            else -> 0.5f
        }
        val interestRawScore = (interestFraction * 100f).roundToInt()
        val interestWeighted = interestFraction * 15.0f

        // 4. Education Match (10% weight)
        val eduFraction = when {
            opportunity.eligibility.contains("Undergraduate", ignoreCase = true) ||
            opportunity.eligibility.contains("All", ignoreCase = true) ||
            opportunity.eligibility.contains("2nd", ignoreCase = true) ||
            opportunity.eligibility.contains("Enrolled", ignoreCase = true) -> 1.0f
            opportunity.eligibility.contains("Master", ignoreCase = true) -> 0.8f
            else -> 0.9f
        }
        val educationRawScore = (eduFraction * 100f).roundToInt()
        val educationWeighted = eduFraction * 10.0f

        // 5. Project/Experience Match (10% weight)
        val projectFraction = when {
            student.completedProjects.isNotEmpty() && (skillFraction >= 0.5f) -> 0.95f
            student.completedProjects.isNotEmpty() -> 0.8f
            else -> 0.5f
        }
        val projectRawScore = (projectFraction * 100f).roundToInt()
        val projectWeighted = projectFraction * 10.0f

        // 6. Preference Match (5% weight)
        var prefPoints = 0.0f
        if (opportunity.workMode == student.preferredWorkMode) prefPoints += 0.5f else prefPoints += 0.25f
        if (!student.preferencePaidOnly || opportunity.compensationType != com.example.model.CompensationType.UNPAID) prefPoints += 0.5f
        val prefFraction = prefPoints.coerceIn(0f, 1f)
        val preferenceRawScore = (prefFraction * 100f).roundToInt()
        val preferenceWeighted = prefFraction * 5.0f

        // 7. Deadline Feasibility (5% weight)
        val deadlineFraction = when {
            opportunity.daysUntilDeadline >= opportunity.preparationDaysRequired -> 1.0f
            opportunity.daysUntilDeadline >= (opportunity.preparationDaysRequired / 2) -> 0.6f
            else -> 0.3f
        }
        val deadlineRawScore = (deadlineFraction * 100f).roundToInt()
        val deadlineWeighted = deadlineFraction * 5.0f

        val rawTotal = (skillWeighted + careerWeighted + interestWeighted + educationWeighted + projectWeighted + preferenceWeighted + deadlineWeighted).roundToInt()
        val finalScore = rawTotal.coerceIn(25, 99)

        val matchLabel = when {
            finalScore >= 90 -> "Excellent Match"
            finalScore >= 80 -> "Strong Match"
            finalScore >= 70 -> "Good Match"
            else -> "Moderate Match"
        }

        // Calculate Application Readiness (skills + experience base)
        val readinessPct = ((skillFraction * 0.7f + projectFraction * 0.3f) * 100f).roundToInt().coerceIn(35, 98)

        // Explainable "Why This Match"
        val whyList = mutableListOf<String>()
        if (matchedReqSkills.isNotEmpty()) {
            whyList.add("${matchedReqSkills.size} of ${reqSkills.size} required skills matched (${matchedReqSkills.take(3).joinToString(", ")})")
        }
        if (careerMatchFraction >= 0.8f) {
            whyList.add("Your career goal (${student.careerGoal}) is strongly aligned")
        }
        if (interestFraction >= 0.8f) {
            whyList.add("Directly matches your interest in ${opportunity.domain.displayName}")
        }
        if (eduFraction >= 0.9f) {
            whyList.add("You meet the education & degree eligibility criteria")
        }
        if (opportunity.workMode == student.preferredWorkMode) {
            whyList.add("Your preferred work mode (${opportunity.workMode.displayName}) is satisfied")
        }
        if (student.completedProjects.isNotEmpty()) {
            whyList.add("Your existing completed projects increase candidate relevance")
        }

        // Potential Concerns / Caveats
        val concernsList = mutableListOf<String>()
        if (missingReqSkills.isNotEmpty()) {
            concernsList.add("You don't currently list experience with ${missingReqSkills.joinToString(", ")}.")
        }
        if (opportunity.preferredSkills.isNotEmpty()) {
            val missingPreferred = opportunity.preferredSkills.filter { pref ->
                allStudentSkills.none { it.contains(pref.uppercase()) }
            }
            if (missingPreferred.isNotEmpty()) {
                concernsList.add("Preferred bonus skills like ${missingPreferred.take(2).joinToString(", ")} could give competitors an edge.")
            }
        }
        if (opportunity.daysUntilDeadline < opportunity.preparationDaysRequired) {
            concernsList.add("Estimated prep time (${opportunity.preparationDaysRequired}d) exceeds deadline (${opportunity.daysUntilDeadline}d). Start immediately!")
        }
        if (opportunity.difficulty == DifficultyLevel.ADVANCED && student.currentYear.contains("First", ignoreCase = true)) {
            concernsList.add("This is an advanced opportunity that often favors senior students.")
        }

        val nextAction = if (missingReqSkills.isEmpty()) {
            "Submit your application and highlight your relevant project experience."
        } else {
            "Complete a mini project to demonstrate ${missingReqSkills.first()} before applying."
        }

        return MatchCalculationResult(
            opportunityId = opportunity.id,
            finalScore = finalScore,
            skillMatchScore = skillRawScore,
            careerGoalMatchScore = careerGoalRawScore,
            interestMatchScore = interestRawScore,
            educationMatchScore = educationRawScore,
            projectMatchScore = projectRawScore,
            preferenceMatchScore = preferenceRawScore,
            deadlineMatchScore = deadlineRawScore,
            matchLevelLabel = matchLabel,
            matchedSkills = matchedReqSkills,
            missingSkills = missingReqSkills,
            applicationReadinessPct = readinessPct,
            whyThisMatches = whyList,
            potentialConcerns = concernsList,
            nextActionRecommendation = nextAction
        )
    }

    /**
     * Generates the Next Best Moves prioritized for the student
     */
    fun generateNextBestMoves(
        student: StudentProfile,
        opportunities: List<OpportunityItem>
    ): List<NextBestMoveItem> {
        val matches = opportunities.map { calculateMatch(student, it) }
            .sortedByDescending { it.finalScore }
        val topMatch = matches.firstOrNull()
        val topOpp = opportunities.firstOrNull { it.id == topMatch?.opportunityId }

        val moves = mutableListOf<NextBestMoveItem>()

        // 1. Project action to close gap
        val missingSkills = topMatch?.missingSkills.orEmpty()
        val missingSkillName = missingSkills.firstOrNull() ?: "Machine Learning"
        moves.add(
            NextBestMoveItem(
                priority = 1,
                action = "Build a Portfolio Project",
                targetName = "Complete an ML / $missingSkillName project",
                reason = "Closes the ${topMatch?.missingSkills?.take(2)?.joinToString(" & ") ?: "skill"} gap for your top match.",
                expectedBenefit = "+18% Application Readiness & demonstrable proof on GitHub.",
                estimatedPreparationTime = "7–10 days",
                relatedOpportunityId = topOpp?.id
            )
        )

        // 2. Profile & GitHub polish
        moves.add(
            NextBestMoveItem(
                priority = 2,
                action = "Update GitHub & Resume",
                targetName = "Add deployed demo links and README benchmarks",
                reason = "Recruiters and evaluators verify repository commit history and documentation.",
                expectedBenefit = "Doubles chances of shortlisting from screening rounds.",
                estimatedPreparationTime = "1–2 days"
            )
        )

        // 3. Top match application
        if (topOpp != null && topMatch != null) {
            moves.add(
                NextBestMoveItem(
                    priority = 3,
                    action = "Apply for Top Match",
                    targetName = "Apply for ${topOpp.title} at ${topOpp.organization}",
                    reason = "${topMatch.finalScore}% match score based on your Python and web foundations.",
                    expectedBenefit = "Direct interview consideration before priority deadline closes.",
                    estimatedPreparationTime = "2–3 hours",
                    relatedOpportunityId = topOpp.id
                )
            )
        }

        // 4. Hackathon participation
        val hackathon = opportunities.firstOrNull { it.type == com.example.model.OpportunityType.HACKATHON }
        if (hackathon != null) {
            moves.add(
                NextBestMoveItem(
                    priority = 4,
                    action = "Join Team Hackathon",
                    targetName = "Register for ${hackathon.title}",
                    reason = "Accelerates prototype delivery and builds high-speed teamwork credentials.",
                    expectedBenefit = "Portfolio-grade live project + prize pool eligibility.",
                    estimatedPreparationTime = "48 hours sprint",
                    relatedOpportunityId = hackathon.id
                )
            )
        }

        // 5. Tool mastery (Docker / Cloud / Git)
        moves.add(
            NextBestMoveItem(
                priority = 5,
                action = "Learn Containerization",
                targetName = "Master Docker & Basic Container Deployment",
                reason = "Over 65% of top software & AI roles cite containerization as preferred skill.",
                expectedBenefit = "Future-proofs backend and MLOps deployment capability.",
                estimatedPreparationTime = "3–5 days"
            )
        )

        return moves
    }
}
