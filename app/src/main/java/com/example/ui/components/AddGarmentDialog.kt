package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GarmentCategory
import com.example.model.GarmentDimensions
import com.example.model.Language
import com.example.model.SizeOption
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.IndigoAccent

@Composable
fun AddGarmentDialog(
    language: Language,
    onDismiss: () -> Unit,
    onGarmentCreated: (
        title: String,
        category: GarmentCategory,
        brand: String,
        priceBdt: Int,
        fabric: String,
        imageUri: String?,
        sizeChart: Map<SizeOption, GarmentDimensions>,
        sellerPage: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("My F-Commerce Brand") }
    var priceStr by remember { mutableStateOf("750") }
    var fabric by remember { mutableStateOf("100% Breathable Cotton") }
    var sellerPage by remember { mutableStateOf("Dhaka Closet Facebook Page") }
    var selectedCategory by remember { mutableStateOf(GarmentCategory.T_SHIRT) }
    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }

    // Manual size chart entries
    var sChest by remember { mutableStateOf("37") }
    var sLength by remember { mutableStateOf("27") }

    var mChest by remember { mutableStateOf("39") }
    var mLength by remember { mutableStateOf("28") }

    var lChest by remember { mutableStateOf("41") }
    var lLength by remember { mutableStateOf("29") }

    var xlChest by remember { mutableStateOf("43") }
    var xlLength by remember { mutableStateOf("30") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("add_garment_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
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
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.BN) "নতুন পোশাক ও সাইজ চার্ট আপলোড" else "Upload Garment & Size Chart",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Photo picker button
                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedPhotoUri != null)
                            (if (language == Language.BN) "ছবি নির্বাচিত হয়েছে ✓" else "Photo Selected ✓")
                        else
                            (if (language == Language.BN) "পোশাকের ছবি নির্বাচন করুন" else "Pick Garment Photo (Gallery)")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (language == Language.BN) "পোশাকের নাম" else "Garment Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price and Category Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Price (৳ BDT)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Brand / Page") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Manual Size Chart Inputs (S, M, L, XL)
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (language == Language.BN) "ম্যানুয়াল সাইজ চার্ট (বুক ও দৈর্ঘ্য ইঞ্চি):" else "Manual Size Chart (Chest & Length in Inches):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoAccent
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // S
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("S:", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(22.dp))
                            OutlinedTextField(value = sChest, onValueChange = { sChest = it }, label = { Text("Chest") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                            OutlinedTextField(value = sLength, onValueChange = { sLength = it }, label = { Text("Length") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // M
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("M:", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(22.dp))
                            OutlinedTextField(value = mChest, onValueChange = { mChest = it }, label = { Text("Chest") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                            OutlinedTextField(value = mLength, onValueChange = { mLength = it }, label = { Text("Length") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // L
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("L:", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(22.dp))
                            OutlinedTextField(value = lChest, onValueChange = { lChest = it }, label = { Text("Chest") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                            OutlinedTextField(value = lLength, onValueChange = { lLength = it }, label = { Text("Length") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // XL
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("XL:", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(22.dp))
                            OutlinedTextField(value = xlChest, onValueChange = { xlChest = it }, label = { Text("Chest") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                            OutlinedTextField(value = xlLength, onValueChange = { xlLength = it }, label = { Text("Length") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val parsedChart = mapOf(
                            SizeOption.S to GarmentDimensions(sChest.toFloatOrNull() ?: 36.5f, sLength.toFloatOrNull() ?: 26.5f, 16.5f),
                            SizeOption.M to GarmentDimensions(mChest.toFloatOrNull() ?: 38.5f, mLength.toFloatOrNull() ?: 27.5f, 17.5f),
                            SizeOption.L to GarmentDimensions(lChest.toFloatOrNull() ?: 41.0f, lLength.toFloatOrNull() ?: 28.5f, 18.5f),
                            SizeOption.XL to GarmentDimensions(xlChest.toFloatOrNull() ?: 43.5f, xlLength.toFloatOrNull() ?: 29.5f, 19.5f),
                            SizeOption.XXL to GarmentDimensions((xlChest.toFloatOrNull() ?: 43.5f) + 2.5f, (xlLength.toFloatOrNull() ?: 29.5f) + 1f, 20.5f)
                        )
                        onGarmentCreated(
                            if (title.isBlank()) "Custom Seller Top" else title,
                            selectedCategory,
                            brand,
                            priceStr.toIntOrNull() ?: 750,
                            fabric,
                            selectedPhotoUri,
                            parsedChart,
                            sellerPage
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("submit_garment_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BN) "পোশাক যোগ করুন ও ট্রায়াল শুরু করুন" else "Save & Start Virtual Try-On",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
