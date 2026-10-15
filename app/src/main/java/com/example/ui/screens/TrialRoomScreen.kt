package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.data.CatalogRepository
import com.example.data.SessionRepository
import com.example.model.AppStrings
import com.example.model.Garment
import com.example.model.Gender
import com.example.model.Language
import com.example.model.ModelSource
import com.example.model.ModelSourceType
import com.example.ui.components.AddGarmentDialog
import com.example.ui.components.FitReportCard
import com.example.ui.components.MeasurementEditorSheet
import com.example.ui.components.QrShareDialog
import com.example.ui.components.VirtualCanvasView
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.EmeraldFit
import com.example.ui.theme.IndigoAccent

@Composable
fun TrialRoomScreen(
    language: Language,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userProfile by SessionRepository.userProfile.collectAsStateWithLifecycle()
    val garments by CatalogRepository.garments.collectAsStateWithLifecycle()
    val selectedGarment by SessionRepository.selectedGarment.collectAsStateWithLifecycle()
    val fitDiagnosis by SessionRepository.fitDiagnosis.collectAsStateWithLifecycle()
    val simulatedSize by SessionRepository.simulatedSize.collectAsStateWithLifecycle()
    val isHeatmapVisible by SessionRepository.isHeatmapVisible.collectAsStateWithLifecycle()

    var showMeasurementEditor by remember { mutableStateOf(false) }
    var showAddGarmentDialog by remember { mutableStateOf(false) }
    var showQrShareDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            SessionRepository.setModelSource(
                ModelSource(ModelSourceType.CUSTOM_PHOTO, uri.toString())
            )
            Toast.makeText(context, if (language == Language.BN) "ছবি আপলোড সম্পন্ন হয়েছে!" else "Photo loaded!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Step 1: Model & Body Measurements Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("model_selector_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.chooseModel(language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedButton(
                        onClick = { showMeasurementEditor = true },
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.editMeasurements(language),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Model Options: Studio Female, Studio Male, Custom Photo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Studio Female
                    ModelOptionChip(
                        title = AppStrings.femaleModel(language),
                        isSelected = userProfile.modelSource.type == ModelSourceType.DEFAULT_FEMALE,
                        iconRes = R.drawable.img_model_female,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            SessionRepository.updateGender(Gender.FEMALE)
                            SessionRepository.setModelSource(ModelSource(ModelSourceType.DEFAULT_FEMALE))
                        }
                    )

                    // Studio Male
                    ModelOptionChip(
                        title = AppStrings.maleModel(language),
                        isSelected = userProfile.modelSource.type == ModelSourceType.DEFAULT_MALE,
                        iconRes = R.drawable.img_model_male,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            SessionRepository.updateGender(Gender.MALE)
                            SessionRepository.setModelSource(ModelSource(ModelSourceType.DEFAULT_MALE))
                        }
                    )

                    // Upload Photo
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("upload_photo_button"),
                        color = if (userProfile.modelSource.type == ModelSourceType.CUSTOM_PHOTO) CoralPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (userProfile.modelSource.type == ModelSourceType.CUSTOM_PHOTO) androidx.compose.foundation.BorderStroke(2.dp, CoralPrimary) else null
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = if (userProfile.modelSource.type == ModelSourceType.CUSTOM_PHOTO) CoralPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = AppStrings.customUpload(language),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (userProfile.modelSource.type == ModelSourceType.CUSTOM_PHOTO) CoralPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Summary Bar (Height, Weight, Chest, Waist)
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BodyStatBadge(
                            label = AppStrings.height(language),
                            value = "${userProfile.heightCm.toInt()} cm"
                        )
                        BodyStatBadge(
                            label = AppStrings.weight(language),
                            value = "${userProfile.weightKg.toInt()} kg"
                        )
                        BodyStatBadge(
                            label = AppStrings.chest(language),
                            value = "${String.format("%.1f", userProfile.measurements.chestInches)}\""
                        )
                        BodyStatBadge(
                            label = AppStrings.waist(language),
                            value = "${String.format("%.1f", userProfile.measurements.waistInches)}\""
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 2: Garment Catalog Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.chooseGarment(language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            FilledTonalButton(
                onClick = { showAddGarmentDialog = true },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == Language.BN) "পোশাক আপলোড" else "+ Upload Garment",
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Garment horizontal carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(garments) { garment ->
                val isSelected = garment.id == selectedGarment.id
                Surface(
                    modifier = Modifier
                        .width(135.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { SessionRepository.selectGarment(garment) }
                        .testTag("garment_card_${garment.id}"),
                    color = if (isSelected) CoralPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, CoralPrimary) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            if (garment.localDrawableRes != null) {
                                Image(
                                    painter = painterResource(id = garment.localDrawableRes),
                                    contentDescription = garment.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            } else if (garment.customImageUri != null) {
                                AsyncImage(
                                    model = Uri.parse(garment.customImageUri),
                                    contentDescription = garment.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = CoralPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == Language.BN) garment.titleBn else garment.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "৳${garment.priceBdt}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = CoralPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 3: Interactive Virtual Try-On Canvas
        VirtualCanvasView(
            garment = selectedGarment,
            modelSource = userProfile.modelSource,
            selectedSize = simulatedSize,
            recommendedSize = fitDiagnosis.bestSize,
            fitDetail = fitDiagnosis.sizeBreakdowns[simulatedSize],
            isHeatmapVisible = isHeatmapVisible,
            language = language,
            onSizeSelected = { SessionRepository.setSimulatedSize(it) },
            onToggleHeatmap = { SessionRepository.toggleHeatmap() },
            onSaveLook = {
                SessionRepository.saveCurrentLook()
                Toast.makeText(context, if (language == Language.BN) "লুক আপনার ওয়ার্ডরোবে সেভ হয়েছে!" else "Look saved to Wardrobe!", Toast.LENGTH_SHORT).show()
            },
            onShare = {
                showQrShareDialog = true
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Step 4: Comprehensive Fit Report Card
        FitReportCard(
            garment = selectedGarment,
            userMeasurements = userProfile.measurements,
            diagnosis = fitDiagnosis,
            simulatedSize = simulatedSize,
            language = language
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Sheets & Dialogs
    if (showMeasurementEditor) {
        MeasurementEditorSheet(
            userProfile = userProfile,
            language = language,
            onDismiss = { showMeasurementEditor = false },
            onSave = { h, w, measurements ->
                SessionRepository.updateHeightWeight(h, w)
                SessionRepository.updateMeasurements(measurements)
            }
        )
    }

    if (showAddGarmentDialog) {
        AddGarmentDialog(
            language = language,
            onDismiss = { showAddGarmentDialog = false },
            onGarmentCreated = { title, category, brand, price, fabric, uri, chart, sellerPage ->
                val newGarment = CatalogRepository.addCustomGarment(
                    title, category, brand, price, fabric, uri, chart, sellerPage
                )
                SessionRepository.selectGarment(newGarment)
                Toast.makeText(context, if (language == Language.BN) "পোশাক যোগ সম্পন্ন হয়েছে!" else "Garment added to fitting catalog!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showQrShareDialog) {
        QrShareDialog(
            garment = selectedGarment,
            language = language,
            onDismiss = { showQrShareDialog = false }
        )
    }
}

@Composable
private fun ModelOptionChip(
    title: String,
    isSelected: Boolean,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("model_chip_${title}"),
        color = if (isSelected) CoralPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, CoralPrimary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun BodyStatBadge(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
