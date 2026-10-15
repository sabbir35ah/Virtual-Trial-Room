package com.example.engine

import com.example.model.BodyMeasurements
import com.example.model.FitCategory
import com.example.model.FitDiagnosis
import com.example.model.Garment
import com.example.model.GarmentCategory
import com.example.model.GarmentDimensions
import com.example.model.SizeFitDetail
import com.example.model.SizeOption
import kotlin.math.abs

object FitMatchingEngine {

    /**
     * Recommends the best size and diagnoses fit across all sizes for a given user & garment.
     */
    fun evaluate(user: BodyMeasurements, garment: Garment): FitDiagnosis {
        val targetChestEase = when (garment.category) {
            GarmentCategory.T_SHIRT -> 2.5f
            GarmentCategory.SHIRT -> 3.0f
            GarmentCategory.PANJABI -> 4.5f
            GarmentCategory.CASUAL_TOP -> 2.5f
        }

        val breakdowns = mutableMapOf<SizeOption, SizeFitDetail>()

        for ((size, dims) in garment.sizeChart) {
            val chestEase = dims.chestInches - user.chestInches
            val shoulderEase = dims.shoulderInches - user.shoulderInches
            val waistEase = dims.waistInches - user.waistInches

            val chestFit = classifyFit(chestEase, targetChestEase, tolerance = 1.2f)
            val shoulderFit = classifyFit(shoulderEase, idealEase = 0.5f, tolerance = 0.8f)
            val waistFit = classifyFit(waistEase, idealEase = 3.0f, tolerance = 2.0f)
            val lengthFit = if (dims.lengthInches < 25f) FitCategory.TOO_TIGHT else FitCategory.PERFECT

            // Calculate match score 0-100%
            val chestScore = calculateMetricScore(chestEase, targetChestEase)
            val shoulderScore = calculateMetricScore(shoulderEase, 0.5f)
            val waistScore = calculateMetricScore(waistEase, 3.0f)
            val overallScore = ((chestScore * 0.55f) + (shoulderScore * 0.25f) + (waistScore * 0.20f)).toInt().coerceIn(30, 99)

            val summaryEn = generateSummaryEn(size, chestEase, targetChestEase, chestFit, garment.category)
            val summaryBn = generateSummaryBn(size, chestEase, targetChestEase, chestFit, garment.category)

            breakdowns[size] = SizeFitDetail(
                size = size,
                chestFit = chestFit,
                chestEaseInches = chestEase,
                shoulderFit = shoulderFit,
                waistFit = waistFit,
                lengthFit = lengthFit,
                matchPercentage = overallScore,
                summaryEn = summaryEn,
                summaryBn = summaryBn
            )
        }

        // Pick highest scoring size that is not TOO_TIGHT
        val bestEntry = breakdowns.maxByOrNull { entry ->
            val penalty = if (entry.value.chestFit == FitCategory.TOO_TIGHT) -50 else 0
            entry.value.matchPercentage + penalty
        } ?: breakdowns.entries.first()

        val bestSize = bestEntry.key
        val bestDetail = bestEntry.value

        val reasonEn = "Size ${bestSize.label} provides an optimal ${String.format("%.1f", bestDetail.chestEaseInches)}\" ease around the chest and natural shoulder line."
        val reasonBn = "সাইজ ${bestSize.label} আপনার বুকে সবচেয়ে মানানসই এবং কাঁধের সাথে আরামদায়কভাবে বসে।"

        return FitDiagnosis(
            bestSize = bestSize,
            overallConfidenceScore = bestDetail.matchPercentage,
            bestDetail = bestDetail,
            sizeBreakdowns = breakdowns,
            recommendationReasonEn = reasonEn,
            recommendationReasonBn = reasonBn
        )
    }

    private fun classifyFit(actualEase: Float, idealEase: Float, tolerance: Float): FitCategory {
        val diff = actualEase - idealEase
        return when {
            actualEase < 0.2f -> FitCategory.TOO_TIGHT
            diff < -tolerance -> FitCategory.SNUG
            abs(diff) <= tolerance -> FitCategory.PERFECT
            diff <= tolerance * 2.5f -> FitCategory.RELAXED
            else -> FitCategory.TOO_LOOSE
        }
    }

    private fun calculateMetricScore(actualEase: Float, idealEase: Float): Float {
        val deviation = abs(actualEase - idealEase)
        return (100f - (deviation * 18f)).coerceIn(20f, 100f)
    }

    private fun generateSummaryEn(
        size: SizeOption,
        chestEase: Float,
        idealEase: Float,
        fit: FitCategory,
        category: GarmentCategory
    ): String {
        val easeStr = if (chestEase >= 0) "+${String.format("%.1f", chestEase)}\"" else "${String.format("%.1f", chestEase)}\""
        return when (fit) {
            FitCategory.TOO_TIGHT -> "Size ${size.label}: Runs small ($easeStr ease). May pull at buttons/seams."
            FitCategory.SNUG -> "Size ${size.label}: Form-fitting ($easeStr ease). Tight athletic fit."
            FitCategory.PERFECT -> "Size ${size.label}: Recommended ideal match! ($easeStr ease) for ${category.displayName}."
            FitCategory.RELAXED -> "Size ${size.label}: Comfortable relaxed fit ($easeStr ease)."
            FitCategory.TOO_LOOSE -> "Size ${size.label}: Oversized/boxy ($easeStr ease)."
        }
    }

    private fun generateSummaryBn(
        size: SizeOption,
        chestEase: Float,
        idealEase: Float,
        fit: FitCategory,
        category: GarmentCategory
    ): String {
        return when (fit) {
            FitCategory.TOO_TIGHT -> "সাইজ ${size.label}: আপনার জন্য বেশি আঁটসাঁট বা ছোট।"
            FitCategory.SNUG -> "সাইজ ${size.label}: বেশ ফিটেড ও বডি-হাগিং।"
            FitCategory.PERFECT -> "সাইজ ${size.label}: আপনার জন্য একদম পারফেক্ট এবং সেরা চয়েস!"
            FitCategory.RELAXED -> "সাইজ ${size.label}: আরামদায়ক ও ঢিলেঢালা ফিট।"
            FitCategory.TOO_LOOSE -> "সাইজ ${size.label}: প্রয়োজনের চেয়ে বেশ বড় বা লুজ।"
        }
    }
}
