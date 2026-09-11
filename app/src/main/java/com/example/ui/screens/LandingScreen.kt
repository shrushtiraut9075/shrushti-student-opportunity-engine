package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OxLogo
import com.example.ui.theme.HighMatchColor
import com.example.ui.theme.OxAmber
import com.example.ui.theme.OxCyan
import com.example.ui.theme.OxCyanLight
import com.example.ui.theme.OxIndigo
import com.example.ui.theme.OxViolet

@Composable
fun LandingScreen(
    onGetStarted: () -> Unit,
    onExplore: () -> Unit,
    onUseDemoAlexSharma: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Brand Header
        OxLogo(size = 48, showFullName = true, showTagline = true)

        Spacer(modifier = Modifier.height(28.dp))

        // Hero Tagline Pill
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OxIndigo.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, OxIndigo.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = OxCyanLight, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "DISCOVER • BUILD SKILLS • SHAPE FUTURE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = OxCyanLight,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Headline
        Text(
            text = "Your Next Opportunity Is Closer Than You Think.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "OpportunityX uses AI to discover internships, hackathons, scholarships, competitions and projects matched to your skills and career goals.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Hero Visual Preview Card (Section 2)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(OxCyan, OxIndigo))),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF97316).copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF97316), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("🔥 High Priority", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF97316))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = HighMatchColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "92% Match",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = HighMatchColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "AI Innovation Internship",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "DeepCore Labs • Remote • ₹25,000/mo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "✓ Python & SQL matched  •  ✓ AI Engineering aligned",
                    fontSize = 12.sp,
                    color = HighMatchColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Flow step banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Your Skills  ➔  AI Matching  ➔  Best Opportunities  ➔  Career Growth",
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Buttons
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("landing_get_started_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Get Started (Build Profile)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onExplore,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Explore All (30+)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }

            Button(
                onClick = onUseDemoAlexSharma,
                modifier = Modifier
                    .weight(1.2f)
                    .height(46.dp)
                    .testTag("landing_demo_alex_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OxIndigo)
            ) {
                Text("Demo Profile (Alex)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Statistics Section (Section 2)
        Text(
            text = "TRUSTED BY AMBITIOUS STUDENTS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox("10,000+", "Opportunities", Modifier.weight(1f))
            StatBox("5,000+", "Students", Modifier.weight(1f))
            StatBox("30+", "Domains", Modifier.weight(1f))
            StatBox("95%", "AI Match Rate", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 6 Platform Features Grid (Section 2)
        Text(
            text = "WHY OPPORTUNITYX IS DIFFERENT",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        FeatureCard(
            icon = Icons.Default.AutoAwesome,
            title = "1. AI-Powered Recommendations",
            description = "Weighted 7-factor matching evaluating skills, education, career goals, and portfolio projects."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FeatureCard(
            icon = Icons.Default.Speed,
            title = "2. Smart Match Score",
            description = "Explainable 0-100% suitability rating showing precisely why an opportunity fits you."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FeatureCard(
            icon = Icons.Default.CrisisAlert,
            title = "3. Skill Gap Analysis",
            description = "Detects missing competencies and tells you the exact application readiness percentage."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FeatureCard(
            icon = Icons.Default.Route,
            title = "4. Career Path Planning",
            description = "Visual roadmap guiding you from 'Where I Am' to 'Where I Want To Be'."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FeatureCard(
            icon = Icons.Default.LocalFireDepartment,
            title = "5. Deadline Intelligence",
            description = "Compares time until closing against your estimated preparation time so you never miss out."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FeatureCard(
            icon = Icons.Default.Psychology,
            title = "6. AI Career Copilot",
            description = "Your personal 24/7 intelligent mentor answering what to apply for, what to build, and next moves."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatBox(number: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(OxIndigo.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = OxCyan, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
