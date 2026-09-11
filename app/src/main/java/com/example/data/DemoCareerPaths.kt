package com.example.data

import com.example.model.CareerStep

object DemoCareerPaths {
    fun getPathForGoal(goal: String): List<CareerStep> {
        return when {
            goal.contains("AI", ignoreCase = true) || goal.contains("Machine", ignoreCase = true) -> listOf(
                CareerStep(
                    stepNumber = 1,
                    title = "Learn Core Python & Data Structures",
                    description = "Master OOP, algorithmic complexity, list comprehensions, and NumPy foundations.",
                    status = "COMPLETED",
                    actionLabel = "Review Mastery (100%)"
                ),
                CareerStep(
                    stepNumber = 2,
                    title = "Build Machine Learning Project",
                    description = "Implement an end-to-end model with Scikit-learn, Pandas, and evaluation metrics.",
                    status = "IN_PROGRESS",
                    actionLabel = "Start 'AI Resume Analyzer' Project"
                ),
                CareerStep(
                    stepNumber = 3,
                    title = "Join an AI / Innovation Hackathon",
                    description = "Compete in a 48-hour sprint to build a collaborative prototype with LLM integration.",
                    status = "UPCOMING",
                    actionLabel = "Register for Generative AI Agent Hackathon"
                ),
                CareerStep(
                    stepNumber = 4,
                    title = "Apply for AI Innovation Internship",
                    description = "Target high-growth startups and labs (92% profile match on OpportunityX).",
                    status = "TARGET",
                    actionLabel = "Apply to DeepCore Labs"
                ),
                CareerStep(
                    stepNumber = 5,
                    title = "Publish Research / Open Source Kernels",
                    description = "Contribute tensor parallelism patches or co-author empirical research benchmarks.",
                    status = "NEXT",
                    actionLabel = "Explore HyperFlow Open Source"
                ),
                CareerStep(
                    stepNumber = 6,
                    title = "Career Milestone: AI/ML Engineer",
                    description = "Deploy production inference pipelines and design autonomous model workflows.",
                    status = "GOAL",
                    actionLabel = "Career Destination"
                )
            )
            else -> listOf(
                CareerStep(
                    stepNumber = 1,
                    title = "Master Foundational Programming",
                    description = "Solid grasp of Java, Python, Git version control, and data structures.",
                    status = "COMPLETED",
                    actionLabel = "Foundation Verified"
                ),
                CareerStep(
                    stepNumber = 2,
                    title = "Ship Full-Stack Production App",
                    description = "Build a complete web application with authentication, SQL database, and responsive UI.",
                    status = "IN_PROGRESS",
                    actionLabel = "Continue Kanban Web Project"
                ),
                CareerStep(
                    stepNumber = 3,
                    title = "Contribute to Open Source Software",
                    description = "Merge pull requests into active developer tooling repositories.",
                    status = "UPCOMING",
                    actionLabel = "View Open Source Programs"
                ),
                CareerStep(
                    stepNumber = 4,
                    title = "Secure Software Engineering Internship",
                    description = "Pass algorithmic interviews and deliver features in high-scale distributed teams.",
                    status = "TARGET",
                    actionLabel = "Apply to Top Matched Internships"
                ),
                CareerStep(
                    stepNumber = 5,
                    title = "System Design & Cloud Architecture",
                    description = "Master microservices, distributed queues, Docker containers, and CI/CD pipelines.",
                    status = "NEXT",
                    actionLabel = "Start Cloud Certification Track"
                ),
                CareerStep(
                    stepNumber = 6,
                    title = "Career Milestone: Full-Stack Engineer",
                    description = "Lead engineering architecture and mentor junior student builders.",
                    status = "GOAL",
                    actionLabel = "Career Destination"
                )
            )
        }
    }
}
