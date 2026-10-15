package com.example.model

enum class Language(val code: String, val displayName: String, val nativeName: String) {
    EN("en", "English", "English"),
    BN("bn", "Bangla", "বাংলা")
}

object AppStrings {
    fun appTitle(lang: Language) = when (lang) {
        Language.EN -> "Codixel Fit"
        Language.BN -> "কোডিক্সেল ফিট"
    }

    fun appSubtitle(lang: Language) = when (lang) {
        Language.EN -> "AI Virtual Trial Room & Fit Advisor"
        Language.BN -> "এআই ভার্চুয়াল ট্রায়াল রুম ও সাইজ উপদেষ্টা"
    }

    fun tabTrialRoom(lang: Language) = when (lang) {
        Language.EN -> "Trial Room"
        Language.BN -> "ট্রায়াল রুম"
    }

    fun tabSellerPilot(lang: Language) = when (lang) {
        Language.EN -> "Seller Hub"
        Language.BN -> "সেলার হাব"
    }

    fun tabWardrobe(lang: Language) = when (lang) {
        Language.EN -> "Wardrobe"
        Language.BN -> "ওয়ার্ডরোব"
    }

    fun tabPricing(lang: Language) = when (lang) {
        Language.EN -> "Pricing"
        Language.BN -> "সাবস্ক্রিপশন"
    }

    fun chooseModel(lang: Language) = when (lang) {
        Language.EN -> "1. Select Model or Upload Photo"
        Language.BN -> "১. মডেল নির্বাচন বা ছবি আপলোড"
    }

    fun femaleModel(lang: Language) = when (lang) {
        Language.EN -> "Studio Female"
        Language.BN -> "মহিলা মডেল"
    }

    fun maleModel(lang: Language) = when (lang) {
        Language.EN -> "Studio Male"
        Language.BN -> "পুরুষ মডেল"
    }

    fun customUpload(lang: Language) = when (lang) {
        Language.EN -> "Upload Photo"
        Language.BN -> "নিজের ছবি"
    }

    fun bodyStatsTitle(lang: Language) = when (lang) {
        Language.EN -> "Height & Weight (For Accurate Measurements)"
        Language.BN -> "উচ্চতা ও ওজন (সঠিক মাপের জন্য)"
    }

    fun height(lang: Language) = when (lang) {
        Language.EN -> "Height"
        Language.BN -> "উচ্চতা"
    }

    fun weight(lang: Language) = when (lang) {
        Language.EN -> "Weight"
        Language.BN -> "ওজন"
    }

    fun estimatedMeasurements(lang: Language) = when (lang) {
        Language.EN -> "Estimated Body Measurements"
        Language.BN -> "অনুমিত শরীরের মাপ"
    }

    fun editMeasurements(lang: Language) = when (lang) {
        Language.EN -> "Edit / Correct"
        Language.BN -> "সংশোধন করুন"
    }

    fun chest(lang: Language) = when (lang) {
        Language.EN -> "Chest"
        Language.BN -> "বুক"
    }

    fun waist(lang: Language) = when (lang) {
        Language.EN -> "Waist"
        Language.BN -> "কোমর"
    }

    fun shoulder(lang: Language) = when (lang) {
        Language.EN -> "Shoulder"
        Language.BN -> "কাঁধ"
    }

    fun length(lang: Language) = when (lang) {
        Language.EN -> "Length"
        Language.BN -> "দৈর্ঘ্য"
    }

    fun chooseGarment(lang: Language) = when (lang) {
        Language.EN -> "2. Choose Garment & Size Chart"
        Language.BN -> "২. পোশাক ও সাইজ চার্ট নির্বাচন"
    }

    fun uploadGarment(lang: Language) = when (lang) {
        Language.EN -> "+ Upload Custom Garment"
        Language.BN -> "+ নতুন পোশাক যোগ করুন"
    }

    fun recommendedSize(lang: Language) = when (lang) {
        Language.EN -> "Recommended Size"
        Language.BN -> "সুপারিশকৃত সাইজ"
    }

    fun fitReport(lang: Language) = when (lang) {
        Language.EN -> "Comprehensive Fit Report"
        Language.BN -> "বিস্তারিত ফিট রিপোর্ট"
    }

    fun tryOnImageTitle(lang: Language) = when (lang) {
        Language.EN -> "3. Virtual Try-On Preview"
        Language.BN -> "৩. ভার্চুয়াল ট্রায়াল প্রিভিউ"
    }

    fun switchSize(lang: Language) = when (lang) {
        Language.EN -> "Switch Size Simulation:"
        Language.BN -> "সাইজ সিমুলেশন পরিবর্তন:"
    }

    fun toggleHeatmap(lang: Language) = when (lang) {
        Language.EN -> "Fit Heatmap"
        Language.BN -> "ফিট হিটম্যাপ"
    }

    fun saveToWardrobe(lang: Language) = when (lang) {
        Language.EN -> "Save Look"
        Language.BN -> "লুক সেভ করুন"
    }

    fun shareTryOn(lang: Language) = when (lang) {
        Language.EN -> "Share Try-On"
        Language.BN -> "শেয়ার করুন"
    }

    fun sellerPilotHeading(lang: Language) = when (lang) {
        Language.EN -> "Codixel F-Commerce Pilot Hub"
        Language.BN -> "কোডিক্সেল এফ-কমার্স পাইলট হাব"
    }

    fun returnRateBefore(lang: Language) = when (lang) {
        Language.EN -> "Return Rate Before"
        Language.BN -> "পূর্বের রিটার্ন রেট"
    }

    fun returnRateAfter(lang: Language) = when (lang) {
        Language.EN -> "Pilot Return Rate"
        Language.BN -> "পাইলট পরবর্তী রিটার্ন"
    }

    fun bdtSaved(lang: Language) = when (lang) {
        Language.EN -> "Saved Return Delivery Costs"
        Language.BN -> "সাশ্রয়কৃত ডেলিভারি ও রিটার্ন খরচ"
    }

    fun tryOnConversion(lang: Language) = when (lang) {
        Language.EN -> "Try-On to Buy Rate"
        Language.BN -> "ট্রায়াল থেকে কেনাকাটার হার"
    }

    fun generatePostLink(lang: Language) = when (lang) {
        Language.EN -> "Generate F-Commerce Post Link & QR"
        Language.BN -> "পোস্টের ট্রায়াল লিংক ও কিউআর বানান"
    }

    fun sizeFeedbackTitle(lang: Language) = when (lang) {
        Language.EN -> "Buyer Fit Feedback Insights"
        Language.BN -> "ক্রেতাদের সাইজ ফিডব্যাক অন্তর্দৃষ্টি"
    }

    fun localPaymentsTitle(lang: Language) = when (lang) {
        Language.EN -> "Instant Payment via bKash & Nagad"
        Language.BN -> "বিকাশ ও নগদে তাৎক্ষণিক পেমেন্ট"
    }

    fun freePilotPlan(lang: Language) = when (lang) {
        Language.EN -> "Seller Pilot (Free 30 Days)"
        Language.BN -> "সেলার পাইলট (ফ্রি ৩০ দিন)"
    }

    fun sellerGrowthPlan(lang: Language) = when (lang) {
        Language.EN -> "Seller Growth Plan"
        Language.BN -> "সেলার গ্রোথ প্ল্যান"
    }
}
