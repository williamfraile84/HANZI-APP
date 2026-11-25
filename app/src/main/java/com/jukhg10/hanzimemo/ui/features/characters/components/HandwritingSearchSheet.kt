package com.jukhg10.hanzimemo.ui.features.characters.components

import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings

import android.graphics.Bitmap
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionResult
import com.jukhg10.hanzimemo.ui.components.HandwritingCanvas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandwritingSearchSheet(
    recognitionEngine: HandwritingRecognitionEngine,
    onDismissRequest: () -> Unit,
    onSelectCharacter: (String) -> Unit
) {
    val strings = LocalAppStrings.current
    val coroutineScope = rememberCoroutineScope()
    var recognitionResult by remember { mutableStateOf<HandwritingRecognitionResult?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "✍️", fontSize = 24.sp)
                    Column {
                        Text(
                            text = strings.handwritingSearchTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.handwritingSearchSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = strings.actionClose,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Lienzo de Caligrafía
            HandwritingCanvas(
                canvasSize = 260.dp,
                onStrokesChanged = { _, bitmap ->
                    isProcessing = true
                    coroutineScope.launch {
                        val result = recognitionEngine.recognizeFromBitmap(bitmap)
                        recognitionResult = result
                        isProcessing = false
                    }
                },
                onClear = {
                    recognitionResult = null
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila de Candidatos de Caracteres Reconocidos
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isProcessing) strings.scanningProcessing else strings.candidatesDetected,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val candidates = recognitionResult?.candidates ?: emptyList()

                if (candidates.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        candidates.forEach { candidate ->
                            SuggestionChip(
                                onClick = {
                                    onSelectCharacter(candidate)
                                    onDismissRequest()
                                },
                                label = {
                                    Text(
                                        text = candidate,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                modifier = Modifier.padding(horizontal = 4.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = strings.handwritingInstruction,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

