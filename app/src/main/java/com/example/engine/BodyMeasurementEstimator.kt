package com.example.engine

import com.example.model.BodyMeasurements
import com.example.model.Gender
import kotlin.math.roundToInt

object BodyMeasurementEstimator {

    /**
     * Estimates anatomical body measurements from height, weight, and gender.
     * Uses anthropometric sizing statistical correlations adjusted for South Asian & global builds.
     */
    fun estimate(heightCm: Float, weightKg: Float, gender: Gender): BodyMeasurements {
        val heightInches = heightCm / 2.54f
        val weightLbs = weightKg * 2.20462f
        val bmi = weightKg / ((heightCm / 100f) * (heightCm / 100f))

        return when (gender) {
            Gender.FEMALE -> {
                // Female anthropometric estimation
                val baseChest = (heightInches * 0.52f) + ((bmi - 21f) * 0.9f)
                val baseWaist = (heightInches * 0.42f) + ((bmi - 21f) * 0.85f)
                val baseShoulder = (heightInches * 0.235f) + ((bmi - 21f) * 0.15f)
                val baseHip = (heightInches * 0.56f) + ((bmi - 21f) * 1.1f)
                val baseArm = heightInches * 0.35f

                BodyMeasurements(
                    chestInches = (baseChest * 10f).roundToInt() / 10f,
                    waistInches = (baseWaist * 10f).roundToInt() / 10f,
                    shoulderInches = (baseShoulder * 10f).roundToInt() / 10f,
                    hipInches = (baseHip * 10f).roundToInt() / 10f,
                    armLengthInches = (baseArm * 10f).roundToInt() / 10f,
                    isCustomized = false
                )
            }
            Gender.MALE, Gender.OTHER -> {
                // Male anthropometric estimation
                val baseChest = (heightInches * 0.56f) + ((bmi - 22f) * 1.05f)
                val baseWaist = (heightInches * 0.46f) + ((bmi - 22f) * 0.95f)
                val baseShoulder = (heightInches * 0.27f) + ((bmi - 22f) * 0.2f)
                val baseHip = (heightInches * 0.54f) + ((bmi - 22f) * 0.9f)
                val baseArm = heightInches * 0.365f

                BodyMeasurements(
                    chestInches = (baseChest * 10f).roundToInt() / 10f,
                    waistInches = (baseWaist * 10f).roundToInt() / 10f,
                    shoulderInches = (baseShoulder * 10f).roundToInt() / 10f,
                    hipInches = (baseHip * 10f).roundToInt() / 10f,
                    armLengthInches = (baseArm * 10f).roundToInt() / 10f,
                    isCustomized = false
                )
            }
        }
    }
}
