package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BodyMeasurements
import com.example.model.FitCategory
import com.example.model.FitDiagnosis
import com.example.model.Garment
import com.example.model.Language
import com.example.model.SizeOption
import com.example.ui.theme.AmberSnug
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CrimsonTight
import com.example.ui.theme.EmeraldFit
import com.example.ui.theme.IndigoAccent

@Composable
fun FitReportCard(
    garment: Garment,
    userMeasurements: BodyMeasurements,
    diagnosis: FitDiagnosis,
    simulatedSize: SizeOption,
    language: Language,
    modifier: Modifier = Modifier
) {
    var showAllSizesTable by remember { mutableStateOf(false) }
    val currentDetail = diagnosis.sizeBreakdowns[simulatedSize] ?: diagnosis.bestDetail

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("fit_report_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = IndigoAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == Language.BN) "স্মার্ট ফিট রিপোর্ট ও সাইজ ম্যাচ" else "Smart Fit Report & Sizing",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Confidence pill
                Surface(
                    color = EmeraldFit.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${diagnosis.overallConfidenceScore}% Match",
                        color = EmeraldFit,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Best Size Recommended Hero Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CoralPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CoralPrimary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(CoralPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = diagnosis.bestSize.label,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.BN) "আপনার জন্য সেরা সাইজ" else "Recommended Best Size",
                            color = CoralPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (language == Language.BN) diagnosis.recommendationReasonBn else diagnosis.recommendationReasonEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Size Details Indicator
            Text(
                text = if (language == Language.BN) "সাইজ ${simulatedSize.label} এর বিস্তারিত ফিট অ্যানালাইসিস:" else "Fit Analysis for Size ${simulatedSize.label}:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fit summary string
            Text(
                text = if (language == Language.BN) currentDetail.summaryBn else currentDetail.summaryEn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4-Point Measurement Metric Cards (Chest, Shoulder, Waist, Length)
            val garmentDims = garment.sizeChart[simulatedSize]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricItem(
                    title = if (language == Language.BN) "বুক" else "Chest",
                    bodyVal = "${String.format("%.1f", userMeasurements.chestInches)}\"",
                    garmentVal = "${garmentDims?.chestInches ?: 0f}\"",
                    fit = currentDetail.chestFit,
                    language = language,
                    modifier = Modifier.weight(1f)
                )

                MetricItem(
                    title = if (language == Language.BN) "কাঁধ" else "Shoulder",
                    bodyVal = "${String.format("%.1f", userMeasurements.shoulderInches)}\"",
                    garmentVal = "${garmentDims?.shoulderInches ?: 0f}\"",
                    fit = currentDetail.shoulderFit,
                    language = language,
                    modifier = Modifier.weight(1f)
                )

                MetricItem(
                    title = if (language == Language.BN) "কোমর" else "Waist",
                    bodyVal = "${String.format("%.1f", userMeasurements.waistInches)}\"",
                    garmentVal = "${garmentDims?.waistInches ?: 0f}\"",
                    fit = currentDetail.waistFit,
                    language = language,
                    modifier = Modifier.weight(1f)
                )

                MetricItem(
                    title = if (language == Language.BN) "দৈর্ঘ্য" else "Length",
                    bodyVal = "-",
                    garmentVal = "${garmentDims?.lengthInches ?: 0f}\"",
                    fit = currentDetail.lengthFit,
                    language = language,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Toggle "Compare All Sizes" button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAllSizesTable = !showAllSizesTable }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == Language.BN) "সব সাইজের তুলনা দেখুন (S, M, L, XL, XXL)" else "Compare All Sizes (S, M, L, XL, XXL)",
                    color = IndigoAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (showAllSizesTable) "▲" else "▼",
                    color = IndigoAccent,
                    fontSize = 12.sp
                )
            }

            AnimatedVisibility(visible = showAllSizesTable) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    diagnosis.sizeBreakdowns.forEach { (sz, detail) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (sz == diagnosis.bestSize) CoralPrimary.copy(alpha = 0.08f) else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = sz.label,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (sz == diagnosis.bestSize) CoralPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                if (sz == diagnosis.bestSize) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == Language.BN) "(সেরা চয়েস)" else "(Best Pick)",
                                        fontSize = 10.sp,
                                        color = CoralPrimary
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val color = when (detail.chestFit) {
                                    FitCategory.PERFECT -> EmeraldFit
                                    FitCategory.SNUG, FitCategory.RELAXED -> AmberSnug
                                    FitCategory.TOO_TIGHT -> CrimsonTight
                                    FitCategory.TOO_LOOSE -> Color(0xFF06B6D4)
                                }
                                Text(
                                    text = if (language == Language.BN) detail.chestFit.labelBn else detail.chestFit.labelEn,
                                    color = color,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${detail.matchPercentage}%",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricItem(
    title: String,
    bodyVal: String,
    garmentVal: String,
    fit: FitCategory,
    language: Language,
    modifier: Modifier = Modifier
) {
    val fitColor = when (fit) {
        FitCategory.PERFECT -> EmeraldFit
        FitCategory.SNUG, FitCategory.RELAXED -> AmberSnug
        FitCategory.TOO_TIGHT -> CrimsonTight
        FitCategory.TOO_LOOSE -> Color(0xFF06B6D4)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = garmentVal,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = fitColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if (language == Language.BN) fit.labelBn else fit.labelEn,
                    color = fitColor,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    maxLines = 1
                )
            }
        }
    }
}
