package com.example.data

import com.example.engine.BodyMeasurementEstimator
import com.example.engine.FitMatchingEngine
import com.example.model.BodyMeasurements
import com.example.model.FitDiagnosis
import com.example.model.Garment
import com.example.model.Gender
import com.example.model.Language
import com.example.model.ModelSource
import com.example.model.ModelSourceType
import com.example.model.SellerPilotStats
import com.example.model.SizeOption
import com.example.model.TryOnSession
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object SessionRepository {

    private val _language = MutableStateFlow(Language.EN)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val initialMeasurements = BodyMeasurementEstimator.estimate(168f, 63f, Gender.FEMALE)
    private val _userProfile = MutableStateFlow(
        UserProfile(
            heightCm = 168f,
            weightKg = 63f,
            gender = Gender.FEMALE,
            modelSource = ModelSource(ModelSourceType.DEFAULT_FEMALE),
            measurements = initialMeasurements
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _selectedGarment = MutableStateFlow<Garment>(CatalogRepository.garments.value.first())
    val selectedGarment: StateFlow<Garment> = _selectedGarment.asStateFlow()

    private val _fitDiagnosis = MutableStateFlow<FitDiagnosis>(
        FitMatchingEngine.evaluate(initialMeasurements, _selectedGarment.value)
    )
    val fitDiagnosis: StateFlow<FitDiagnosis> = _fitDiagnosis.asStateFlow()

    private val _simulatedSize = MutableStateFlow<SizeOption>(_fitDiagnosis.value.bestSize)
    val simulatedSize: StateFlow<SizeOption> = _simulatedSize.asStateFlow()

    private val _isHeatmapVisible = MutableStateFlow(false)
    val isHeatmapVisible: StateFlow<Boolean> = _isHeatmapVisible.asStateFlow()

    private val _savedWardrobe = MutableStateFlow<List<TryOnSession>>(emptyList())
    val savedWardrobe: StateFlow<List<TryOnSession>> = _savedWardrobe.asStateFlow()

    private val _sellerStats = MutableStateFlow(SellerPilotStats())
    val sellerStats: StateFlow<SellerPilotStats> = _sellerStats.asStateFlow()

    private val _remainingTrialCredits = MutableStateFlow(5)
    val remainingTrialCredits: StateFlow<Int> = _remainingTrialCredits.asStateFlow()

    private val _isProUser = MutableStateFlow(false)
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.EN) Language.BN else Language.EN
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    fun updateHeightWeight(heightCm: Float, weightKg: Float) {
        val current = _userProfile.value
        val newMeasurements = BodyMeasurementEstimator.estimate(heightCm, weightKg, current.gender)
        _userProfile.value = current.copy(
            heightCm = heightCm,
            weightKg = weightKg,
            measurements = newMeasurements
        )
        recalculateFit()
    }

    fun updateGender(gender: Gender) {
        val current = _userProfile.value
        val modelSource = when (gender) {
            Gender.FEMALE -> ModelSource(ModelSourceType.DEFAULT_FEMALE)
            Gender.MALE -> ModelSource(ModelSourceType.DEFAULT_MALE)
            Gender.OTHER -> current.modelSource
        }
        val newMeasurements = BodyMeasurementEstimator.estimate(current.heightCm, current.weightKg, gender)
        _userProfile.value = current.copy(
            gender = gender,
            modelSource = modelSource,
            measurements = newMeasurements
        )
        recalculateFit()
    }

    fun setModelSource(modelSource: ModelSource) {
        _userProfile.value = _userProfile.value.copy(modelSource = modelSource)
    }

    fun updateMeasurements(custom: BodyMeasurements) {
        _userProfile.value = _userProfile.value.copy(measurements = custom.copy(isCustomized = true))
        recalculateFit()
    }

    fun selectGarment(garment: Garment) {
        _selectedGarment.value = garment
        recalculateFit()
    }

    fun setSimulatedSize(size: SizeOption) {
        _simulatedSize.value = size
    }

    fun toggleHeatmap() {
        _isHeatmapVisible.value = !_isHeatmapVisible.value
    }

    fun saveCurrentLook(notes: String = ""): TryOnSession {
        val session = TryOnSession(
            id = UUID.randomUUID().toString(),
            garment = _selectedGarment.value,
            selectedSize = _simulatedSize.value,
            bestRecommendedSize = _fitDiagnosis.value.bestSize,
            userMeasurements = _userProfile.value.measurements,
            matchPercentage = _fitDiagnosis.value.sizeBreakdowns[_simulatedSize.value]?.matchPercentage ?: 85,
            notes = notes
        )
        _savedWardrobe.value = listOf(session) + _savedWardrobe.value
        return session
    }

    fun removeLook(sessionId: String) {
        _savedWardrobe.value = _savedWardrobe.value.filterNot { it.id == sessionId }
    }

    fun activateProSubscription() {
        _isProUser.value = true
        _remainingTrialCredits.value = 9999
    }

    fun consumeCredit() {
        if (!_isProUser.value && _remainingTrialCredits.value > 0) {
            _remainingTrialCredits.value = _remainingTrialCredits.value - 1
        }
    }

    private fun recalculateFit() {
        val newDiag = FitMatchingEngine.evaluate(_userProfile.value.measurements, _selectedGarment.value)
        _fitDiagnosis.value = newDiag
        _simulatedSize.value = newDiag.bestSize
    }
}
