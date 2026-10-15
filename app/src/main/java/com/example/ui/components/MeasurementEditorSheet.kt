package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.BodyMeasurementEstimator
import com.example.model.BodyMeasurements
import com.example.model.Gender
import com.example.model.Language
import com.example.model.UserProfile
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.IndigoAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementEditorSheet(
    userProfile: UserProfile,
    language: Language,
    onDismiss: () -> Unit,
    onSave: (heightCm: Float, weightKg: Float, measurements: BodyMeasurements) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var heightCm by remember { mutableFloatStateOf(userProfile.heightCm) }
    var weightKg by remember { mutableFloatStateOf(userProfile.weightKg) }
    var chestInches by remember { mutableFloatStateOf(userProfile.measurements.chestInches) }
    var waistInches by remember { mutableFloatStateOf(userProfile.measurements.waistInches) }
    var shoulderInches by remember { mutableFloatStateOf(userProfile.measurements.shoulderInches) }
    var hipInches by remember { mutableFloatStateOf(userProfile.measurements.hipInches) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = CoralPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == Language.BN) "শরীরের সঠিক মাপ সংশোধন" else "Fine-Tune Body Measurements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = if (language == Language.BN)
                    "আপনার আনুমানিক মাপ স্বয়ংক্রিয়ভাবে তৈরি হয়েছে। কোনো মাপ ভুল মনে হলে স্লাইডার দিয়ে সহজে ঠিক করে নিন।"
                else
                    "Estimates are auto-calculated from your height & weight. Adjust any slider if your body proportions differ.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Height Slider
            val heightFtIn = cmToFeetInches(heightCm)
            MeasurementSliderItem(
                label = if (language == Language.BN) "উচ্চতা (Height)" else "Height",
                valueDisplay = "${heightCm.toInt()} cm ($heightFtIn)",
                value = heightCm,
                range = 140f..205f,
                step = 1f,
                onValueChange = { newH ->
                    heightCm = newH
                    // Auto-reestimate base numbers if user wishes
                    val re = BodyMeasurementEstimator.estimate(newH, weightKg, userProfile.gender)
                    chestInches = re.chestInches
                    waistInches = re.waistInches
                    shoulderInches = re.shoulderInches
                }
            )

            // Weight Slider
            MeasurementSliderItem(
                label = if (language == Language.BN) "ওজন (Weight)" else "Weight",
                valueDisplay = "${weightKg.toInt()} kg (${(weightKg * 2.20462f).toInt()} lbs)",
                value = weightKg,
                range = 35f..130f,
                step = 1f,
                onValueChange = { newW ->
                    weightKg = newW
                    val re = BodyMeasurementEstimator.estimate(heightCm, newW, userProfile.gender)
                    chestInches = re.chestInches
                    waistInches = re.waistInches
                    shoulderInches = re.shoulderInches
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (language == Language.BN) "নির্দিষ্ট অঙ্গের মাপ (ইঞ্চি):" else "Specific Anatomical Measurements (Inches):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoAccent
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Chest
                    MeasurementSliderItem(
                        label = if (language == Language.BN) "বুকের মাপ (Chest)" else "Chest Girth",
                        valueDisplay = "${String.format("%.1f", chestInches)}\" (${String.format("%.1f", chestInches * 2.54f)} cm)",
                        value = chestInches,
                        range = 28f..56f,
                        step = 0.5f,
                        onValueChange = { chestInches = it }
                    )

                    // Waist
                    MeasurementSliderItem(
                        label = if (language == Language.BN) "কোমরের মাপ (Waist)" else "Waist Girth",
                        valueDisplay = "${String.format("%.1f", waistInches)}\" (${String.format("%.1f", waistInches * 2.54f)} cm)",
                        value = waistInches,
                        range = 22f..52f,
                        step = 0.5f,
                        onValueChange = { waistInches = it }
                    )

                    // Shoulder
                    MeasurementSliderItem(
                        label = if (language == Language.BN) "কাঁধের চওড়া (Shoulder)" else "Shoulder Width",
                        valueDisplay = "${String.format("%.1f", shoulderInches)}\"",
                        value = shoulderInches,
                        range = 13f..24f,
                        step = 0.5f,
                        onValueChange = { shoulderInches = it }
                    )

                    // Hips
                    MeasurementSliderItem(
                        label = if (language == Language.BN) "নিতম্ব (Hips)" else "Hips",
                        valueDisplay = "${String.format("%.1f", hipInches)}\"",
                        value = hipInches,
                        range = 30f..56f,
                        step = 0.5f,
                        onValueChange = { hipInches = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val re = BodyMeasurementEstimator.estimate(heightCm, weightKg, userProfile.gender)
                        chestInches = re.chestInches
                        waistInches = re.waistInches
                        shoulderInches = re.shoulderInches
                        hipInches = re.hipInches
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == Language.BN) "রিসেট" else "Reset Auto",
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        val updatedMeasurements = BodyMeasurements(
                            chestInches = chestInches,
                            waistInches = waistInches,
                            shoulderInches = shoulderInches,
                            hipInches = hipInches,
                            armLengthInches = userProfile.measurements.armLengthInches,
                            isCustomized = true
                        )
                        onSave(heightCm, weightKg, updatedMeasurements)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("save_measurements_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BN) "মাপ সংরক্ষণ করুন" else "Apply & Re-Fit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MeasurementSliderItem(
    label: String,
    valueDisplay: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    step: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valueDisplay,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = CoralPrimary,
                activeTrackColor = CoralPrimary
            )
        )
    }
}

private fun cmToFeetInches(cm: Float): String {
    val totalInches = cm / 2.54f
    val feet = (totalInches / 12).toInt()
    val inches = (totalInches % 12).toInt()
    return "$feet'$inches\""
}
