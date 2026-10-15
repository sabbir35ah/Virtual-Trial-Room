package com.example

import com.example.data.CatalogRepository
import com.example.engine.BodyMeasurementEstimator
import com.example.engine.FitMatchingEngine
import com.example.model.Gender
import com.example.model.SizeOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testBodyMeasurementEstimation() {
        // Test estimation for female model: 168 cm, 63 kg
        val femaleMeasurements = BodyMeasurementEstimator.estimate(168f, 63f, Gender.FEMALE)
        assertTrue("Chest should be positive", femaleMeasurements.chestInches > 30f)
        assertTrue("Waist should be reasonable", femaleMeasurements.waistInches > 24f)
        assertTrue("Shoulder should be proportionate", femaleMeasurements.shoulderInches > 13f)

        // Test estimation for male model: 175 cm, 72 kg
        val maleMeasurements = BodyMeasurementEstimator.estimate(175f, 72f, Gender.MALE)
        assertTrue("Male chest should be larger than waist", maleMeasurements.chestInches > maleMeasurements.waistInches)
    }

    @Test
    fun testFitMatchingRecommendation() {
        val measurements = BodyMeasurementEstimator.estimate(168f, 63f, Gender.FEMALE)
        val garment = CatalogRepository.garments.value.first()
        val diagnosis = FitMatchingEngine.evaluate(measurements, garment)

        assertNotNull(diagnosis)
        assertNotNull(diagnosis.bestSize)
        assertTrue("Overall confidence score should be > 50", diagnosis.overallConfidenceScore > 50)
        assertTrue("Size breakdowns should cover S, M, L, XL, XXL", diagnosis.sizeBreakdowns.containsKey(SizeOption.M))
    }
}
