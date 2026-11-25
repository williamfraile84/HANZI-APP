package com.jukhg10.hanzimemo.ui.features.flashcard.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionResult
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.ui.components.HandwritingCanvas
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import com.jukhg10.hanzimemo.ui.theme.LocalIsDarkTheme
import com.jukhg10.hanzimemo.ui.theme.SuccessEmerald
import com.jukhg10.hanzimemo.ui.theme.getPinyinToneColor
import kotlinx.coroutines.launch

@Composable
fun HandwritingPracticeCard(
    info: EnhancedCharacterInfo,
    recognitionEngine: HandwritingRecognitionEngine
) {
    val strings = LocalAppStrings.current
    val isDark = LocalIsDarkTheme.current
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val toneColor = getPinyinToneColor(info.pinyinTone, isDark)

    var recognitionResult by remember { mutableStateOf<HandwritingRecognitionResult?>(null) }
    var isMatchAchieved by remember { mutableStateOf(false) }
    var isWritingActive by remember { mutableStateOf(false) }

    // Reiniciar estado si cambia el carácter mostrado
    LaunchedEffect(info.simplified) {
        recognitionResult = null
        isMatchAchieved = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la Sección
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Draw,
                    contentDescription = strings.handwritingSectionTitle,
                    tint = toneColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = strings.handwritingSectionTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = strings.handwritingInstruction,
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Botón de activación para evitar capturar el scroll vertical
            if (!isWritingActive) {
                OutlinedButton(
                    onClick = { isWritingActive = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = toneColor
                    )
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = strings.writeModeActive, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                }
            } else {
                Button(
                    onClick = { isWritingActive = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = toneColor
                    )
                ) {
                    Icon(imageVector = Icons.Default.PanTool, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.scrollModeActive,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFF003828) else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lienzo Interactivo con Guía Fantasma
            HandwritingCanvas(
                canvasSize = 250.dp,
                ghostCharacter = info.simplified,
                inkColor = toneColor,
                enabled = isWritingActive,
                onStrokesChanged = { _, bitmap ->
                    coroutineScope.launch {
                        val res = recognitionEngine.recognizeFromBitmap(bitmap, targetCharacter = info.simplified)
                        recognitionResult = res
                        if (res.isTargetMatch && !isMatchAchieved) {
                            isMatchAchieved = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                },
                onClear = {
                    recognitionResult = null
                    isMatchAchieved = false
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tarjeta de Retroalimentación y Reconocimiento
            AnimatedContent(
                targetState = isMatchAchieved,
                label = "WritingFeedbackAnimation"
            ) { achieved ->
                if (achieved) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SuccessEmerald.copy(alpha = if (isDark) 0.22f else 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SuccessEmerald),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correcto",
                                tint = SuccessEmerald,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = strings.strokeEvaluatedSuccess,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = SuccessEmerald
                                )
                                Text(
                                    text = "${info.simplified} (${info.pinyin})",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Consejo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            val candidates = recognitionResult?.candidates ?: emptyList()
                            val hintText = if (candidates.isNotEmpty()) {
                                "OCR: ${candidates.take(3).joinToString(" ")}"
                            } else {
                                strings.strokeEvaluatedRetry
                            }
                            Text(
                                text = hintText,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
