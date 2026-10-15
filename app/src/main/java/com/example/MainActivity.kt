package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SessionRepository
import com.example.model.AppStrings
import com.example.model.Language
import com.example.ui.components.AppHeader
import com.example.ui.screens.PricingScreen
import com.example.ui.screens.SellerPilotScreen
import com.example.ui.screens.TrialRoomScreen
import com.example.ui.screens.WardrobeScreen
import com.example.ui.theme.CodixelFitTheme
import com.example.ui.theme.CoralPrimary

enum class AppTab {
    TRIAL_ROOM,
    SELLER_HUB,
    WARDROBE,
    PRICING
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodixelFitTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentTab by remember { mutableStateOf(AppTab.TRIAL_ROOM) }

    val language by SessionRepository.language.collectAsStateWithLifecycle()
    val isPro by SessionRepository.isProUser.collectAsStateWithLifecycle()
    val credits by SessionRepository.remainingTrialCredits.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                currentLanguage = language,
                isPro = isPro,
                remainingCredits = credits,
                onToggleLanguage = { SessionRepository.toggleLanguage() },
                onPricingClick = { currentTab = AppTab.PRICING }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                // Tab 1: Trial Room
                NavigationBarItem(
                    selected = currentTab == AppTab.TRIAL_ROOM,
                    onClick = { currentTab = AppTab.TRIAL_ROOM },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = AppStrings.tabTrialRoom(language)
                        )
                    },
                    label = {
                        Text(
                            text = AppStrings.tabTrialRoom(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.TRIAL_ROOM) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CoralPrimary,
                        selectedTextColor = CoralPrimary,
                        indicatorColor = CoralPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_trial_room")
                )

                // Tab 2: Seller Hub
                NavigationBarItem(
                    selected = currentTab == AppTab.SELLER_HUB,
                    onClick = { currentTab = AppTab.SELLER_HUB },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = AppStrings.tabSellerPilot(language)
                        )
                    },
                    label = {
                        Text(
                            text = AppStrings.tabSellerPilot(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.SELLER_HUB) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CoralPrimary,
                        selectedTextColor = CoralPrimary,
                        indicatorColor = CoralPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_seller_hub")
                )

                // Tab 3: Wardrobe
                NavigationBarItem(
                    selected = currentTab == AppTab.WARDROBE,
                    onClick = { currentTab = AppTab.WARDROBE },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = AppStrings.tabWardrobe(language)
                        )
                    },
                    label = {
                        Text(
                            text = AppStrings.tabWardrobe(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.WARDROBE) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CoralPrimary,
                        selectedTextColor = CoralPrimary,
                        indicatorColor = CoralPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_wardrobe")
                )

                // Tab 4: Pricing
                NavigationBarItem(
                    selected = currentTab == AppTab.PRICING,
                    onClick = { currentTab = AppTab.PRICING },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = AppStrings.tabPricing(language)
                        )
                    },
                    label = {
                        Text(
                            text = AppStrings.tabPricing(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.PRICING) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CoralPrimary,
                        selectedTextColor = CoralPrimary,
                        indicatorColor = CoralPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_pricing")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.TRIAL_ROOM -> TrialRoomScreen(language = language)
                AppTab.SELLER_HUB -> SellerPilotScreen(language = language)
                AppTab.WARDROBE -> WardrobeScreen(
                    language = language,
                    onNavigateToTrial = { currentTab = AppTab.TRIAL_ROOM }
                )
                AppTab.PRICING -> PricingScreen(language = language)
            }
        }
    }
}
