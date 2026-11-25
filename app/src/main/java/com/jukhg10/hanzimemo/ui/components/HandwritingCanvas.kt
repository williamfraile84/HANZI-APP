package com.jukhg10.hanzimemo.ui.components

import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint as AndroidPaint
import android.graphics.Path as AndroidPath
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class CanvasGridType {
    MI_ZI_GE,   // 米字格 (Rejilla tradicional de 8 secciones con diagonales)
    TIAN_ZI_GE, // 田字格 (Rejilla clásica en cruz de 4 secciones)
    PLAIN       // Lienzo despejado sin guías
}

data class StrokePoint(val x: Float, val y: Float, val timestamp: Long = System.currentTimeMillis())

data class DrawnStroke(
    val points: List<StrokePoint>,
    val color: Color = Color.Black,
    val strokeWidth: Float = 14f
)

/**
 * Lienzo táctil interactivo de caligrafía a mano alzada para caracteres Hanzi.
 * Implementa suavizado Bézier cuadrático, soporte de cuadrículas caligráficas,
 * silueta guía fantasma y captura bitmap para reconocimiento ML Kit.
 */
@Composable
fun HandwritingCanvas(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 280.dp,
    ghostCharacter: String? = null,
    inkColor: Color = MaterialTheme.colorScheme.onSurface,
    gridType: CanvasGridType = CanvasGridType.MI_ZI_GE,
    enabled: Boolean = true,
    onStrokesChanged: ((List<DrawnStroke>, Bitmap) -> Unit)? = null,
    onClear: (() -> Unit)? = null
) {
    val strings = LocalAppStrings.current
    val strokes = remember { mutableStateListOf<DrawnStroke>() }
    var currentPoints by remember { mutableStateOf<List<StrokePoint>>(emptyList()) }
    var currentGrid by remember { mutableStateOf(gridType) }
    var showGhostGuide by remember { mutableStateOf(!ghostCharacter.isNullOrEmpty()) }
    val density = LocalDensity.current
    val pxSize = with(density) { canvasSize.toPx() }

    // Generador de Bitmap para reconocimiento offline
    fun generateRecognitionBitmap(sizePx: Int = 400): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val androidCanvas = AndroidCanvas(bitmap)
        androidCanvas.drawColor(AndroidColor.WHITE)

        val paint = AndroidPaint().apply {
            color = AndroidColor.BLACK
            isAntiAlias = true
            style = AndroidPaint.Style.STROKE
            strokeJoin = AndroidPaint.Join.ROUND
            strokeCap = AndroidPaint.Cap.ROUND
            strokeWidth = 20f
        }

        val scale = if (pxSize > 0) sizePx.toFloat() / pxSize else 1f

        for (s in strokes) {
            if (s.points.size < 2) continue
            val path = AndroidPath()
            path.moveTo(s.points[0].x * scale, s.points[0].y * scale)
            for (i in 1 until s.points.size) {
                val p0 = s.points[i - 1]
                val p1 = s.points[i]
                val midX = (p0.x + p1.x) / 2f * scale
                val midY = (p0.y + p1.y) / 2f * scale
                path.quadTo(p0.x * scale, p0.y * scale, midX, midY)
            }
            androidCanvas.drawPath(path, paint)
        }
        return bitmap
    }

    // Debounce para notificar cambios de trazo al reconocedor
    LaunchedEffect(strokes.size) {
        if (strokes.isNotEmpty()) {
            delay(150)
            val bmp = generateRecognitionBitmap()
            onStrokesChanged?.invoke(strokes.toList(), bmp)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Área Cuadrada del Lienzo
        Box(
            modifier = Modifier
                .size(canvasSize)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp)
                )
                .then(
                    if (enabled) {
                        Modifier.pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints = listOf(StrokePoint(offset.x, offset.y))
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPoints = currentPoints + StrokePoint(change.position.x, change.position.y)
                                },
                                onDragEnd = {
                                    if (currentPoints.size >= 2) {
                                        strokes.add(DrawnStroke(currentPoints, inkColor, 14f))
                                    }
                                    currentPoints = emptyList()
                                },
                                onDragCancel = {
                                    currentPoints = emptyList()
                                }
                            )
                        }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            // 1. Cuadrícula de Fondo Caligráfica
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawGrid(currentGrid, size)
            }

            // 2. Silueta Guía Fantasma (Ghost Overlay)
            if (showGhostGuide && !ghostCharacter.isNullOrEmpty()) {
                Text(
                    text = ghostCharacter,
                    fontSize = (canvasSize.value * 0.75f).sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    textAlign = TextAlign.Center
                )
            }

            // 3. Trazos Dibujados por el Usuario con Curvas Bézier Suaves
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Trazos consolidados
                for (stroke in strokes) {
                    drawSmoothStroke(stroke.points, stroke.color, stroke.strokeWidth)
                }
                // Trazo en curso en tiempo real
                if (currentPoints.size >= 2) {
                    drawSmoothStroke(currentPoints, inkColor, 14f)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Barra de Controles y Herramientas del Lienzo
        Row(
            modifier = Modifier.width(canvasSize),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selector de Cuadrícula (米 / 田 / Limpio)
            IconButton(
                onClick = {
                    currentGrid = when (currentGrid) {
                        CanvasGridType.MI_ZI_GE -> CanvasGridType.TIAN_ZI_GE
                        CanvasGridType.TIAN_ZI_GE -> CanvasGridType.PLAIN
                        CanvasGridType.PLAIN -> CanvasGridType.MI_ZI_GE
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            ) {
                Icon(
                    imageVector = Icons.Default.GridOn,
                    contentDescription = strings.changeGridContentDesc,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Interruptor de Guía Fantasma (si hay carácter disponible)
            if (!ghostCharacter.isNullOrEmpty()) {
                IconButton(
                    onClick = { showGhostGuide = !showGhostGuide },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (showGhostGuide) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        )
                ) {
                    Icon(
                        imageVector = if (showGhostGuide) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = strings.toggleGuideContentDesc,
                        tint = if (showGhostGuide) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Botón Deshacer Último Trazo (Undo)
                IconButton(
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            strokes.removeAt(strokes.lastIndex)
                            val bmp = generateRecognitionBitmap()
                            onStrokesChanged?.invoke(strokes.toList(), bmp)
                        }
                    },
                    enabled = strokes.isNotEmpty(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = strings.undoStrokeContentDesc,
                        tint = if (strokes.isNotEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Botón Limpiar Todo (Clear)
                IconButton(
                    onClick = {
                        strokes.clear()
                        currentPoints = emptyList()
                        onClear?.invoke()
                    },
                    enabled = strokes.isNotEmpty(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = strings.clearCanvasContentDesc,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// Renderizado de Trazos con Spline Cuadrático Bézier
private fun DrawScope.drawSmoothStroke(points: List<StrokePoint>, color: Color, strokeWidth: Float) {
    if (points.size < 2) return
    val path = Path()
    path.moveTo(points[0].x, points[0].y)

    for (i in 1 until points.size) {
        val p0 = points[i - 1]
        val p1 = points[i]
        val midX = (p0.x + p1.x) / 2f
        val midY = (p0.y + p1.y) / 2f
        path.quadraticBezierTo(p0.x, p0.y, midX, midY)
    }
    path.lineTo(points.last().x, points.last().y)

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

// Dibujo de Cuadrículas Caligráficas Tradicionales (米字格 y 田字格)
private fun DrawScope.drawGrid(gridType: CanvasGridType, size: androidx.compose.ui.geometry.Size) {
    if (gridType == CanvasGridType.PLAIN) return

    val gridColor = Color(0xFFD63031).copy(alpha = 0.22f)
    val strokeWidth = 1.2.dp.toPx()
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    val w = size.width
    val h = size.height

    // Marco exterior sutil
    drawRect(
        color = gridColor,
        style = Stroke(width = strokeWidth)
    )

    // Líneas cruzadas (田字格)
    drawLine(
        color = gridColor,
        start = Offset(w / 2f, 0f),
        end = Offset(w / 2f, h),
        strokeWidth = strokeWidth,
        pathEffect = pathEffect
    )
    drawLine(
        color = gridColor,
        start = Offset(0f, h / 2f),
        end = Offset(w, h / 2f),
        strokeWidth = strokeWidth,
        pathEffect = pathEffect
    )

    // Diagonales adicionales para 米字格
    if (gridType == CanvasGridType.MI_ZI_GE) {
        drawLine(
            color = gridColor,
            start = Offset(0f, 0f),
            end = Offset(w, h),
            strokeWidth = strokeWidth,
            pathEffect = pathEffect
        )
        drawLine(
            color = gridColor,
            start = Offset(w, 0f),
            end = Offset(0f, h),
            strokeWidth = strokeWidth,
            pathEffect = pathEffect
        )
    }
}

