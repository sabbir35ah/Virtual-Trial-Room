package com.example.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.FitCategory
import com.example.model.Garment
import com.example.model.GarmentCategory
import com.example.model.Language
import com.example.model.ModelSource
import com.example.model.ModelSourceType
import com.example.model.SizeFitDetail
import com.example.model.SizeOption
import com.example.ui.theme.AmberSnug
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CrimsonTight
import com.example.ui.theme.EmeraldFit
import com.example.ui.theme.IndigoAccent

@Composable
fun VirtualCanvasView(
    garment: Garment,
    modelSource: ModelSource,
    selectedSize: SizeOption,
    recommendedSize: SizeOption,
    fitDetail: SizeFitDetail?,
    isHeatmapVisible: Boolean,
    language: Language,
    onSizeSelected: (SizeOption) -> Unit,
    onToggleHeatmap: () -> Unit,
    onSaveLook: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Drape scaling based on size difference
    val sizeScaleFactor by animateFloatAsState(
        targetValue = when (selectedSize) {
            SizeOption.S -> 0.94f
            SizeOption.M -> 1.00f
            SizeOption.L -> 1.07f
            SizeOption.XL -> 1.15f
            SizeOption.XXL -> 1.23f
        },
        animationSpec = tween(durationMillis = 350),
        label = "sizeScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("virtual_canvas_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Canvas Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = CoralPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = CoralPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI Virtual Drape Engine",
                                color = CoralPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Heatmap toggle button
                FilledTonalButton(
                    onClick = onToggleHeatmap,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isHeatmapVisible) IndigoAccent else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isHeatmapVisible) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BN) "ফিট হিটম্যাপ" else "Fit Heatmap",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Fitting Area (Aspect ratio 3:4 portrait)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E2433),
                                Color(0xFF131824)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Layer 1: Model Base Image
                when (modelSource.type) {
                    ModelSourceType.DEFAULT_FEMALE -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_model_female),
                            contentDescription = "Studio Female Model",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    ModelSourceType.DEFAULT_MALE -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_model_male),
                            contentDescription = "Studio Male Model",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    ModelSourceType.CUSTOM_PHOTO -> {
                        if (modelSource.customUri != null) {
                            AsyncImage(
                                model = Uri.parse(modelSource.customUri),
                                contentDescription = "Custom User Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_model_female),
                                contentDescription = "Fallback Model",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Layer 2: Clothing Try-On Layer with Size Scale Simulation
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = (20 * (1.25f - sizeScaleFactor * 0.2f)).dp),
                    contentAlignment = Alignment.Center
                ) {
                    // If local drawable is present, render it with blending
                    if (garment.localDrawableRes != null) {
                        Image(
                            painter = painterResource(id = garment.localDrawableRes),
                            contentDescription = garment.title,
                            modifier = Modifier
                                .fillMaxWidth(sizeScaleFactor * 0.85f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit,
                            alpha = 0.94f
                        )
                    } else if (garment.customImageUri != null) {
                        AsyncImage(
                            model = Uri.parse(garment.customImageUri),
                            contentDescription = garment.title,
                            modifier = Modifier
                                .fillMaxWidth(sizeScaleFactor * 0.85f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit,
                            alpha = 0.94f
                        )
                    } else {
                        // Fallback stylish vector representation
                        Image(
                            painter = painterResource(id = R.drawable.img_garment_tee),
                            contentDescription = garment.title,
                            modifier = Modifier
                                .fillMaxWidth(sizeScaleFactor * 0.85f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit,
                            alpha = 0.92f
                        )
                    }
                }

                // Layer 3: Fit Heatmap Lines Overlay (when active)
                if (isHeatmapVisible && fitDetail != null) {
                    val chestColor = when (fitDetail.chestFit) {
                        FitCategory.PERFECT -> EmeraldFit
                        FitCategory.SNUG, FitCategory.RELAXED -> AmberSnug
                        FitCategory.TOO_TIGHT -> CrimsonTight
                        FitCategory.TOO_LOOSE -> Color(0xFF06B6D4)
                    }
                    val shoulderColor = when (fitDetail.shoulderFit) {
                        FitCategory.PERFECT -> EmeraldFit
                        FitCategory.SNUG -> AmberSnug
                        else -> CrimsonTight
                    }

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Shoulder tension contour line
                        drawLine(
                            color = shoulderColor,
                            start = Offset(w * 0.22f, h * 0.32f),
                            end = Offset(w * 0.78f, h * 0.32f),
                            strokeWidth = 6f
                        )

                        // Chest contour line
                        drawOval(
                            color = chestColor.copy(alpha = 0.3f),
                            topLeft = Offset(w * 0.24f, h * 0.38f),
                            size = Size(w * 0.52f, h * 0.16f)
                        )
                        drawOval(
                            color = chestColor,
                            topLeft = Offset(w * 0.24f, h * 0.38f),
                            size = Size(w * 0.52f, h * 0.16f),
                            style = Stroke(width = 4f)
                        )

                        // Waist contour line
                        drawLine(
                            color = EmeraldFit,
                            start = Offset(w * 0.28f, h * 0.56f),
                            end = Offset(w * 0.72f, h * 0.56f),
                            strokeWidth = 5f
                        )
                    }
                }

                // Layer 4: Floating Fit Badge on Top Right
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    val isRecommended = selectedSize == recommendedSize
                    Surface(
                        color = if (isRecommended) EmeraldFit else Color(0xDD1E293B),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = if (isRecommended) EmeraldFit else Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isRecommended) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == Language.BN) "সেরা ফিট" else "Best Fit",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "${fitDetail?.matchPercentage ?: 80}% Match",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Layer 5: Floating Garment Brand Info on Bottom Left
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Surface(
                        color = Color(0xCC0F172A),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text(
                                text = if (language == Language.BN) garment.titleBn else garment.title,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Text(
                                text = "৳${garment.priceBdt} • ${garment.sellerPageName}",
                                color = CoralPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Size Selector Row: S, M, L, XL, XXL
            Text(
                text = if (language == Language.BN) "সাইজ সিমুলেশন পরিবর্তন করুন:" else "Switch Size Simulation:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SizeOption.values().forEach { size ->
                    val isSelected = size == selectedSize
                    val isRecommended = size == recommendedSize

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSizeSelected(size) }
                            .testTag("size_button_${size.label}"),
                        color = when {
                            isSelected -> CoralPrimary
                            isRecommended -> CoralPrimary.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = if (isRecommended && !isSelected) {
                            androidx.compose.foundation.BorderStroke(1.5.dp, CoralPrimary)
                        } else null
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = size.label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected || isRecommended) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                if (isRecommended) {
                                    Text(
                                        text = "★",
                                        fontSize = 9.sp,
                                        color = if (isSelected) Color.White else CoralPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Save Look & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onSaveLook,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("save_look_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IndigoAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BN) "লুক সেভ করুন" else "Save Look",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                FilledTonalButton(
                    onClick = onShare,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("share_look_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BN) "শেয়ার করুন" else "Share Try-On",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
