package com.jukhg10.hanzimemo.ui.features.handwriting

import androidx.compose.animation.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState as rememberVerticalScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionResult
import com.jukhg10.hanzimemo.ui.components.CanvasGridType
import com.jukhg10.hanzimemo.ui.components.HandwritingCanvas
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import com.jukhg10.hanzimemo.ui.navigation.Routes
import com.jukhg10.hanzimemo.ui.theme.SuccessEmerald
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandwritingPracticeScreen(
    recognitionEngine: HandwritingRecognitionEngine,
    navController: NavController? = null
) {
    val strings = LocalAppStrings.current
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val quickStudyCharacters = listOf("你", "好", "我", "爱", "休", "明", "家", "男", "看", "学", "中", "国", "人", "水", "火", "山", "日", "月")
    var selectedTargetChar by remember { mutableStateOf<String?>("好") }
    var recognitionResult by remember { mutableStateOf<HandwritingRecognitionResult?>(null) }
    var isMatchAchieved by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTargetChar) {
        recognitionResult = null
        isMatchAchieved = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "✍️", fontSize = 22.sp)
                        Text(
                            text = strings.calligraphyScreenTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberVerticalScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Selector de Carácter de Práctica
            Text(
                text = strings.calligraphySelectHint,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Opción Modo Libre
                FilterChip(
                    selected = selectedTargetChar == null,
                    onClick = { selectedTargetChar = null },
                    label = { Text(strings.freeMode, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )

                // Caracteres rápidos
                quickStudyCharacters.forEach { char ->
                    FilterChip(
                        selected = selectedTargetChar == char,
                        onClick = { selectedTargetChar = char },
                        label = {
                            Text(
                                text = char,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lienzo Interactivo
            HandwritingCanvas(
                canvasSize = 280.dp,
                ghostCharacter = selectedTargetChar,
                gridType = CanvasGridType.MI_ZI_GE,
                onStrokesChanged = { _, bitmap ->
                    isProcessing = true
                    coroutineScope.launch {
                        val result = recognitionEngine.recognizeFromBitmap(
                            bitmap = bitmap,
                            targetCharacter = selectedTargetChar
                        )
                        recognitionResult = result
                        isProcessing = false
                        if (result.isTargetMatch && !isMatchAchieved) {
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

            Spacer(modifier = Modifier.height(16.dp))

            // Panel de Evaluación y Candidatos Reconocidos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isMatchAchieved && selectedTargetChar != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessEmerald.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${strings.strokeEvaluatedSuccess} ('$selectedTargetChar')",
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessEmerald,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            text = if (isProcessing) strings.scanningProcessing else strings.matchesFound,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val candidates = recognitionResult?.candidates ?: emptyList()
                    if (candidates.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            candidates.forEach { cand ->
                                SuggestionChip(
                                    onClick = {
                                        if (navController != null) {
                                            navController.navigate(Routes.Dictionary.route)
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = cand,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    } else {
                        Text(
                            text = strings.handwritingInstruction,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
