package com.example.data

import com.example.model.DifficultyLevel
import com.example.model.DomainType
import com.example.model.ProjectRecommendationItem

object DemoProjects {
    val list: List<ProjectRecommendationItem> = listOf(
        ProjectRecommendationItem(
            id = "proj_1",
            title = "AI Resume & Job Match Analyzer",
            description = "Build an automated tool that extracts keywords from PDF resumes, computes semantic cosine similarity against job descriptions, and highlights skill gaps.",
            difficulty = DifficultyLevel.INTERMEDIATE,
            estimatedDuration = "7–10 days",
            targetDomain = DomainType.AI_ML,
            skillsGained = listOf("Python", "Machine Learning", "NLP", "API Integration", "FastAPI"),
            careerRelevanceStars = 5,
            isCompleted = false
        ),
        ProjectRecommendationItem(
            id = "proj_2",
            title = "Student Academic Performance Prediction System",
            description = "Develop an interactive data science application predicting student semester outcomes based on attendance, test scores, and socio-academic factors.",
            difficulty = DifficultyLevel.INTERMEDIATE,
            estimatedDuration = "5–7 days",
            targetDomain = DomainType.DATA_SCIENCE,
            skillsGained = listOf("Python", "Pandas", "Scikit-learn", "Data Visualization", "Streamlit"),
            careerRelevanceStars = 5,
            isCompleted = false
        ),
        ProjectRecommendationItem(
            id = "proj_3",
            title = "Containerized Cloud Task Orchestrator",
            description = "Implement an automated job dispatch engine with Dockerized containers, health probes, redis queue broker, and a React management dashboard.",
            difficulty = DifficultyLevel.ADVANCED,
            estimatedDuration = "10–14 days",
            targetDomain = DomainType.CLOUD,
            skillsGained = listOf("Docker", "Cloud Computing", "Python", "React", "CI/CD"),
            careerRelevanceStars = 5,
            isCompleted = false
        ),
        ProjectRecommendationItem(
            id = "proj_4",
            title = "Real-Time Collaborative Kanban Board",
            description = "Construct a high-performance web board with drag-and-drop cards, live WebSocket presence indicators, optimistic updates, and SQLite persistence.",
            difficulty = DifficultyLevel.INTERMEDIATE,
            estimatedDuration = "6–8 days",
            targetDomain = DomainType.WEB_DEV,
            skillsGained = listOf("React", "TypeScript", "SQL", "WebSockets", "UI/UX"),
            careerRelevanceStars = 5,
            isCompleted = true
        ),
        ProjectRecommendationItem(
            id = "proj_5",
            title = "Zero-Trust Identity & API Security Gateway",
            description = "Create a reverse proxy gateway verifying JWT claims, rate-limiting malicious IPs, encrypting sensitive payloads, and logging security telemetry.",
            difficulty = DifficultyLevel.ADVANCED,
            estimatedDuration = "8–12 days",
            targetDomain = DomainType.CYBERSECURITY,
            skillsGained = listOf("Cybersecurity", "Java", "SQL", "Cryptography", "Network Security"),
            careerRelevanceStars = 5,
            isCompleted = false
        ),
        ProjectRecommendationItem(
            id = "proj_6",
            title = "Decentralized Credential Verification Protocol",
            description = "Mint tamper-proof academic degrees as non-fungible on-chain verifiable credentials with cryptographic signature verification.",
            difficulty = DifficultyLevel.ADVANCED,
            estimatedDuration = "10–14 days",
            targetDomain = DomainType.BLOCKCHAIN,
            skillsGained = listOf("Blockchain", "JavaScript", "Solidity", "Smart Contracts"),
            careerRelevanceStars = 4,
            isCompleted = false
        ),
        ProjectRecommendationItem(
            id = "proj_7",
            title = "Autonomous Robot Obstacle Avoidance Simulator",
            description = "Simulate wheeled differential drive robotics navigating complex mazes using LIDAR distance calculations and A* path planning.",
            difficulty = DifficultyLevel.ADVANCED,
            estimatedDuration = "9–12 days",
            targetDomain = DomainType.ROBOTICS,
            skillsGained = listOf("C++", "Python", "Algorithms", "Robotics"),
            careerRelevanceStars = 4,
            isCompleted = false
        ),
        ProjectRecommendationItem(
            id = "proj_8",
            title = "Accessible Material Design Component Library",
            description = "Package reusable, keyboard-accessible, screen-reader compliant UI widgets with interactive documentation and dark mode support.",
            difficulty = DifficultyLevel.BEGINNER,
            estimatedDuration = "4–6 days",
            targetDomain = DomainType.UI_UX,
            skillsGained = listOf("UI/UX", "HTML", "CSS", "React", "Design Systems"),
            careerRelevanceStars = 4,
            isCompleted = false
        )
    )
}
