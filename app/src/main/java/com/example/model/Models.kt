package com.example.model

enum class Gender {
    FEMALE, MALE, OTHER
}

enum class ModelSourceType {
    DEFAULT_FEMALE,
    DEFAULT_MALE,
    CUSTOM_PHOTO
}

data class ModelSource(
    val type: ModelSourceType,
    val customUri: String? = null,
    val displayName: String = when (type) {
        ModelSourceType.DEFAULT_FEMALE -> "Studio Female Model"
        ModelSourceType.DEFAULT_MALE -> "Studio Male Model"
        ModelSourceType.CUSTOM_PHOTO -> "Uploaded Photo"
    }
)

data class BodyMeasurements(
    val chestInches: Float,
    val waistInches: Float,
    val shoulderInches: Float,
    val hipInches: Float,
    val armLengthInches: Float,
    val isCustomized: Boolean = false
) {
    val chestCm: Float get() = chestInches * 2.54f
    val waistCm: Float get() = waistInches * 2.54f
    val shoulderCm: Float get() = shoulderInches * 2.54f
}

data class UserProfile(
    val heightCm: Float = 168f, // Default approx 5 ft 6 in
    val weightKg: Float = 63f,
    val gender: Gender = Gender.FEMALE,
    val modelSource: ModelSource = ModelSource(ModelSourceType.DEFAULT_FEMALE),
    val measurements: BodyMeasurements = BodyMeasurements(
        chestInches = 36.0f,
        waistInches = 29.5f,
        shoulderInches = 15.5f,
        hipInches = 38.0f,
        armLengthInches = 23.0f
    )
)

enum class GarmentCategory(val displayName: String, val displayNameBn: String) {
    T_SHIRT("T-Shirt", "টি-শার্ট"),
    SHIRT("Button Shirt", "শার্ট"),
    PANJABI("Panjabi / Kurta", "পাঞ্জাবি / কুর্তা"),
    CASUAL_TOP("Casual Top", "টপস")
}

enum class SizeOption(val label: String) {
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL")
}

data class GarmentDimensions(
    val chestInches: Float,
    val lengthInches: Float,
    val shoulderInches: Float,
    val waistInches: Float = chestInches - 1.5f
)

data class Garment(
    val id: String,
    val title: String,
    val titleBn: String,
    val category: GarmentCategory,
    val brand: String,
    val priceBdt: Int,
    val fabric: String,
    val fabricBn: String,
    val localDrawableRes: Int? = null,
    val customImageUri: String? = null,
    val sizeChart: Map<SizeOption, GarmentDimensions>,
    val sellerPageName: String = "Codixel Fashion BD",
    val isFCommercePilotItem: Boolean = true
)

enum class FitCategory(val labelEn: String, val labelBn: String, val score: Int) {
    TOO_TIGHT("Too Tight", "খুবই টাইট", 35),
    SNUG("Snug / Fitted", "আঁটসাঁট", 80),
    PERFECT("Perfect Fit", "নিখুঁত ফিট", 100),
    RELAXED("Comfortable Relaxed", "আরামদায়ক ঢিলেঢালা", 92),
    TOO_LOOSE("Too Loose", "অতিরিক্ত ঢিলেঢালা", 50)
}

data class SizeFitDetail(
    val size: SizeOption,
    val chestFit: FitCategory,
    val chestEaseInches: Float,
    val shoulderFit: FitCategory,
    val waistFit: FitCategory,
    val lengthFit: FitCategory,
    val matchPercentage: Int,
    val summaryEn: String,
    val summaryBn: String
)

data class FitDiagnosis(
    val bestSize: SizeOption,
    val overallConfidenceScore: Int,
    val bestDetail: SizeFitDetail,
    val sizeBreakdowns: Map<SizeOption, SizeFitDetail>,
    val recommendationReasonEn: String,
    val recommendationReasonBn: String
)

data class SellerSizeFeedback(
    val size: SizeOption,
    val commentEn: String,
    val commentBn: String,
    val percentage: Int
)

data class SellerPilotStats(
    val storeName: String = "Zahra Boutique (F-Commerce)",
    val totalTryOnsCount: Int = 842,
    val returnRateBefore: Float = 28.4f,
    val returnRateCurrent: Float = 8.6f,
    val estimatedBdtSaved: Long = 27400L,
    val tryOnToOrderRate: Float = 36.8f,
    val sizeAcceptanceRate: Float = 89.2f,
    val feedbackInsights: List<SellerSizeFeedback> = listOf(
        SellerSizeFeedback(SizeOption.M, "82% found Size M chest ideal", "৮২% ক্রেতা সাইজ M বুক নিখুঁত পেয়েছেন", 82),
        SellerSizeFeedback(SizeOption.L, "12% mentioned Length slightly long", "১২% জানিয়েছেন দৈর্ঘ্য কিছুটা লম্বা", 12),
        SellerSizeFeedback(SizeOption.S, "6% felt shoulders snug", "৬% কাঁধে সামান্য টাইট অনুভব করেছেন", 6)
    )
)

data class TryOnSession(
    val id: String,
    val garment: Garment,
    val selectedSize: SizeOption,
    val bestRecommendedSize: SizeOption,
    val userMeasurements: BodyMeasurements,
    val matchPercentage: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

enum class PaymentMethod(val displayName: String, val iconRes: String) {
    BKASH("bKash (বিকাশ)", "bkash"),
    NAGAD("Nagad (নগদ)", "nagad"),
    CARD("Debit / Credit Card", "card")
}
