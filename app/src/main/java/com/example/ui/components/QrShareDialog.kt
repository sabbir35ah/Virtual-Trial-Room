package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Garment
import com.example.model.Language
import com.example.ui.theme.BkashPink
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.IndigoAccent

@Composable
fun QrShareDialog(
    garment: Garment,
    language: Language,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val tryOnUrl = "https://codixel.fit/try/${garment.id}"

    val socialCaption = if (language == Language.BN) {
        """
        🛍️ নতুন কালেকশন: ${garment.titleBn} (৳${garment.priceBdt})
        কিনবার আগে নিজের শরীরে কেমন মানাবে ও কোন সাইজ লাগবে এখনই ট্রাই করে দেখুন কোডিক্সেল ভার্চুয়াল ফিটিং রুমে:
        🔗 ট্রায়াল লিংক: $tryOnUrl
        #CodixelFit #OnlineShoppingBD #NoMoreWrongSize
        """.trimIndent()
    } else {
        """
        🛍️ New Arrival: ${garment.title} (৳${garment.priceBdt})
        Try it on your own body & find your exact size before ordering!
        🔗 Virtual Fitting Link: $tryOnUrl
        #CodixelFit #VirtualFittingRoom #PerfectFit
        """.trimIndent()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("qr_share_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.BN) "ট্রায়াল লিংক ও কিউআর শেয়ার" else "F-Commerce Try-On Link & QR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated Stylized QR Code
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(170.dp)
                        .padding(4.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(14.dp)
                    ) {
                        val gridSize = 9
                        val cellSize = size.width / gridSize
                        val darkColor = Color(0xFF0F172A)

                        // Outer corner markers
                        drawRect(darkColor, Offset(0f, 0f), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize * 0.5f, cellSize * 0.5f), Size(cellSize * 2, cellSize * 2))
                        drawRect(darkColor, Offset(cellSize * 1f, cellSize * 1f), Size(cellSize, cellSize))

                        drawRect(darkColor, Offset(cellSize * 6, 0f), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize * 6.5f, cellSize * 0.5f), Size(cellSize * 2, cellSize * 2))
                        drawRect(darkColor, Offset(cellSize * 7f, cellSize * 1f), Size(cellSize, cellSize))

                        drawRect(darkColor, Offset(0f, cellSize * 6), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize * 0.5f, cellSize * 6.5f), Size(cellSize * 2, cellSize * 2))
                        drawRect(darkColor, Offset(cellSize * 1f, cellSize * 7f), Size(cellSize, cellSize))

                        // Pattern data cells
                        val pattern = listOf(
                            Pair(4, 1), Pair(4, 2), Pair(4, 4), Pair(1, 4), Pair(7, 4),
                            Pair(3, 3), Pair(5, 3), Pair(3, 5), Pair(5, 5), Pair(4, 7),
                            Pair(7, 6), Pair(6, 7), Pair(8, 8), Pair(5, 8), Pair(8, 5)
                        )
                        pattern.forEach { (col, row) ->
                            drawRect(
                                color = darkColor,
                                topLeft = Offset(col * cellSize, row * cellSize),
                                size = Size(cellSize, cellSize)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Link Display Pill with Copy
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Try-On Link", tryOnUrl))
                            Toast.makeText(context, if (language == Language.BN) "লিংক কপি হয়েছে!" else "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = tryOnUrl,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IndigoAccent,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = IndigoAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Social Post Caption box
                Text(
                    text = if (language == Language.BN) "ফেসবুক/ইনস্টাগ্রাম পোস্টের ক্যাপশন:" else "Ready Social Media Post Caption:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = socialCaption,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(10.dp),
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Copy Caption Button
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Social Caption", socialCaption))
                        Toast.makeText(context, if (language == Language.BN) "ক্যাপশন ও লিংক কপি হয়েছে!" else "Caption & Link copied!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("copy_post_caption_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BN) "ক্যাপশন কপি করে ফেসবুকে পোস্ট করুন" else "Copy Caption for Facebook Post",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
