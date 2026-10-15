package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SessionRepository
import com.example.model.AppStrings
import com.example.model.Language
import com.example.ui.components.PaymentSheet
import com.example.ui.theme.BkashPink
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.EmeraldFit
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.NagadOrange

@Composable
fun PricingScreen(
    language: Language,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPro by SessionRepository.isProUser.collectAsStateWithLifecycle()
    val credits by SessionRepository.remainingTrialCredits.collectAsStateWithLifecycle()

    var activePaymentPlan by remember { mutableStateOf<Pair<String, Int>?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Header
        Text(
            text = if (language == Language.BN) "সাবস্ক্রিপশন ও পেমেন্ট প্ল্যান" else "Pricing & Membership Plans",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (language == Language.BN)
                "বিকাশ (bKash) ও নগদ (Nagad) এর মাধ্যমে সরাসরি সহজ পেমেন্ট"
            else
                "Instant localized checkout via bKash & Nagad",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Shopper B2C Section
        Text(
            text = if (language == Language.BN) "👤 ক্রেতাদের জন্য (B2C Shoppers)" else "👤 For Shoppers (B2C)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = IndigoAccent
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Free Plan Card
        PlanCard(
            title = if (language == Language.BN) "ফ্রি বেসিক প্ল্যান" else "Free Basic",
            price = "৳0 / mo",
            features = if (language == Language.BN) listOf(
                "প্রতি মাসে ৫ টি ফ্রি ভার্চুয়াল ট্রায়াল",
                "বেসিক সাইজ সুপারিশ ও ফিট গাইড",
                "স্টুডিও মেল ও ফিমেল মডেল"
            ) else listOf(
                "5 Free Virtual Try-Ons per month",
                "Basic Size Recommendation & Fit Guide",
                "Studio Male & Female Models"
            ),
            isCurrent = !isPro,
            actionLabel = if (isPro) "Active" else "$credits Try-Ons Left",
            onAction = {},
            isHighlight = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Shopper Pro Plan
        PlanCard(
            title = if (language == Language.BN) "শপার প্রো (Shopper Pro)" else "Shopper Pro",
            price = "৳199 / mo",
            features = if (language == Language.BN) listOf(
                "আনলিমিটেড হাই-রেজোলিউশন ট্রায়াল",
                "ফিট হিটম্যাপ ও স্ট্রেস জোন বিশ্লেষণ",
                "নিজের ছবি দিয়ে ট্রায়াল আনলক",
                "ওয়ার্ডরোবে আনলিমিটেড লুক সেভ"
            ) else listOf(
                "Unlimited High-Res Try-Ons",
                "Fit Heatmap & Drape Tension Analysis",
                "Custom Full-Body Photo Try-On",
                "Unlimited Saved Wardrobe Looks"
            ),
            isCurrent = isPro,
            actionLabel = if (isPro) "Active Plan ✓" else (if (language == Language.BN) "বিকাশ/নগদে প্রো কিনুন (৳১৯৯)" else "Upgrade via bKash/Nagad (৳199)"),
            onAction = {
                if (!isPro) activePaymentPlan = Pair("Shopper Pro", 199)
            },
            isHighlight = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Seller B2B Section
        Text(
            text = if (language == Language.BN) "🏪 অনলাইন ও এফ-কমার্স বিক্রেতাদের জন্য (B2B)" else "🏪 For F-Commerce Sellers (B2B)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CoralPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Seller Pilot Card
        PlanCard(
            title = AppStrings.freePilotPlan(language),
            price = "FREE 30 Days",
            features = if (language == Language.BN) listOf(
                "১০-২০ জন নির্বাচিত সেলারের জন্য বিনামূল্যে",
                "৫০০ জন ক্রেতার ট্রায়াল সাপোর্ট",
                "ফেসবুক পোস্টের জন্য ট্রায়াল লিংক ও কিউআর",
                "রিটার্ন হ্রাস ট্র্যাকার ড্যাশবোর্ড"
            ) else listOf(
                "Complimentary access for 10-20 pilot sellers",
                "Up to 500 shopper virtual try-ons",
                "Shareable Try-On link & QR for FB posts",
                "Live Return Reduction Analytics"
            ),
            isCurrent = true,
            actionLabel = if (language == Language.BN) "পাইলট সচল রয়েছে" else "Pilot Active",
            onAction = {},
            isHighlight = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Seller Growth Card
        PlanCard(
            title = AppStrings.sellerGrowthPlan(language),
            price = "৳1,499 / mo",
            features = if (language == Language.BN) listOf(
                "আনলিমিটেড ক্রেতা ট্রায়াল লিংক ও কিউআর",
                "ব্র্যান্ডেড ফিটিং রুম ও সোশ্যাল উইজেট",
                "ডিটেইলড সাইজ ফিডব্যাক ডাটা ইনসাইট",
                "অগ্রাধিকার সাপোর্ট ও রিটেইল পরামর্শ"
            ) else listOf(
                "Unlimited Shopper Try-On Links & QR",
                "Branded Fitting Experience & Social Widget",
                "Buyer Size Feedback & Sizing Reports",
                "Priority Support & Return Optimization"
            ),
            isCurrent = false,
            actionLabel = if (language == Language.BN) "সাবস্ক্রাইব করুন (৳১,৪৯৯)" else "Subscribe (৳1,499/mo)",
            onAction = {
                activePaymentPlan = Pair("Seller Growth Plan", 1499)
            },
            isHighlight = true
        )

        Spacer(modifier = Modifier.height(28.dp))
    }

    if (activePaymentPlan != null) {
        val (title, amount) = activePaymentPlan!!
        PaymentSheet(
            planTitle = title,
            amountBdt = amount,
            language = language,
            onDismiss = { activePaymentPlan = null },
            onSuccess = {
                SessionRepository.activateProSubscription()
                Toast.makeText(context, if (language == Language.BN) "প্ল্যান অ্যাক্টিভ হয়েছে!" else "Plan activated successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    features: List<String>,
    isCurrent: Boolean,
    actionLabel: String,
    onAction: () -> Unit,
    isHighlight: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (isHighlight) androidx.compose.foundation.BorderStroke(1.5.dp, CoralPrimary) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = price,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = if (isHighlight) CoralPrimary else MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            features.forEach { feat ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = EmeraldFit,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feat,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("plan_action_${title.take(6)}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isHighlight) CoralPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isHighlight) Color.White else MaterialTheme.colorScheme.onSurface
                ),
                enabled = !isCurrent || isHighlight
            ) {
                Text(text = actionLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
