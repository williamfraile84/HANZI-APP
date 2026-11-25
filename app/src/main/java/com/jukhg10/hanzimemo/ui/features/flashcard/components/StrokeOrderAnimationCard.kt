package com.jukhg10.hanzimemo.ui.features.flashcard.components

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import com.jukhg10.hanzimemo.ui.theme.LocalIsDarkTheme
import com.jukhg10.hanzimemo.ui.theme.getPinyinToneColor

/**
 * Tarjeta interactiva para la visualización del orden de escritura y trazos de cada carácter chino.
 * Si el carácter cuenta con definición vectorial SVG (MakeMeAHanzi / AnimCJK), renderiza la animación
 * trazo a trazo con caligrafía fluida dentro de la cuadrícula tradicional 米 (Mi Zi Ge).
 */
@Composable
fun StrokeOrderAnimationCard(
    info: EnhancedCharacterInfo
) {
    val strings = LocalAppStrings.current
    val isDark = LocalIsDarkTheme.current
    val toneColor = getPinyinToneColor(info.pinyinTone, isDark)
    val totalStrokes = info.strokeCount.coerceAtLeast(1)
    
    var currentStep by remember(info.id) { mutableIntStateOf(totalStrokes) }
    var isPlaying by remember(info.id) { mutableStateOf(false) }
    var webViewRef by remember(info.id) { mutableStateOf<WebView?>(null) }

    val hasSvg = remember(info.strokeOrderPath) {
        info.strokeOrderPath.isNotBlank() && info.strokeOrderPath.contains("<svg")
    }

    val toneColorHex = remember(toneColor) {
        val argb = toneColor.toArgb()
        String.format("#%06X", 0xFFFFFF and argb)
    }

    // Fallback de animación por temporizador solo si NO hay SVG disponible
    LaunchedEffect(isPlaying, hasSvg) {
        if (isPlaying && !hasSvg) {
            while (isPlaying) {
                kotlinx.coroutines.delay(650)
                if (currentStep < totalStrokes) {
                    currentStep++
                } else {
                    currentStep = 1
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado con Título y Contador de Trazos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = strings.strokeOrderTitle,
                        tint = toneColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = strings.strokeOrderTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = toneColor.copy(alpha = if (isDark) 0.22f else 0.15f)
                ) {
                    Text(
                        text = strings.strokeStepFormat(currentStep, totalStrokes),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = toneColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Lienzo con Cuadrícula estilo 米 (Mi Zi Ge) y Renderizado Vectorial
            val canvasBg = if (isDark) Color(0xFF1B211F) else Color(0xFFFFFDF9)
            val gridColor = if (isDark) Color(0xFF2C3733) else Color(0xFFE2D9CC)
            val diagColor = if (isDark) Color(0xFF232B28) else Color(0xFFEAE2D7)

            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(canvasBg)
                    .border(2.dp, toneColor.copy(alpha = if (isDark) 0.5f else 0.35f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Cuadrícula 米 de fondo
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                    // Líneas guía horizontales, verticales y diagonales
                    drawLine(gridColor, Offset(0f, h / 2), Offset(w, h / 2), pathEffect = dash)
                    drawLine(gridColor, Offset(w / 2, 0f), Offset(w / 2, h), pathEffect = dash)
                    drawLine(diagColor, Offset(0f, 0f), Offset(w, h), pathEffect = dash)
                    drawLine(diagColor, Offset(w, 0f), Offset(0f, h), pathEffect = dash)
                }

                if (hasSvg) {
                    // Renderizado interactivo del SVG Animado de AnimCJK / MakeMeAHanzi
                    StrokeOrderWebView(
                        svgContent = info.strokeOrderPath,
                        strokeColorHex = toneColorHex,
                        initialStep = currentStep,
                        initialPlaying = isPlaying,
                        isDark = isDark,
                        onStepChanged = { step ->
                            currentStep = step
                        },
                        onWebViewReady = { webView ->
                            webViewRef = webView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Respaldo visual si el carácter no tiene trazos vectoriales registrados
                    Text(
                        text = info.simplified,
                        fontSize = 120.sp,
                        fontWeight = FontWeight.Bold,
                        color = toneColor.copy(alpha = (currentStep.toFloat() / totalStrokes.toFloat()).coerceIn(0.2f, 1.0f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Panel de Controles Multimedia de Escritura
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Trazo Anterior
                IconButton(
                    onClick = {
                        isPlaying = false
                        val prev = if (currentStep > 1) currentStep - 1 else totalStrokes
                        currentStep = prev
                        if (hasSvg) {
                            webViewRef?.evaluateJavascript("setStep($prev);", null)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = strings.prevStroke,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Reproducir / Pausar
                FilledIconButton(
                    onClick = {
                        val newPlaying = !isPlaying
                        isPlaying = newPlaying
                        if (hasSvg) {
                            if (newPlaying) {
                                webViewRef?.evaluateJavascript("play();", null)
                            } else {
                                webViewRef?.evaluateJavascript("stopPlay();", null)
                            }
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = toneColor)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) strings.pauseAnimation else strings.playAnimation,
                        tint = if (isDark) Color(0xFF003828) else Color.White
                    )
                }

                // Siguiente Trazo
                IconButton(
                    onClick = {
                        isPlaying = false
                        val next = if (currentStep < totalStrokes) currentStep + 1 else 1
                        currentStep = next
                        if (hasSvg) {
                            webViewRef?.evaluateJavascript("setStep($next);", null)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = strings.nextStroke,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Reiniciar
                IconButton(
                    onClick = {
                        isPlaying = true
                        currentStep = 1
                        if (hasSvg) {
                            webViewRef?.evaluateJavascript("replay();", null)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = strings.restartStroke,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Componente WebView optimizado para reproducir SVGs animados de trazos caligráficos.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun StrokeOrderWebView(
    svgContent: String,
    strokeColorHex: String,
    initialStep: Int,
    initialPlaying: Boolean,
    isDark: Boolean,
    onStepChanged: (Int) -> Unit,
    onWebViewReady: (WebView) -> Unit,
    modifier: Modifier = Modifier
) {
    val htmlData = remember(svgContent, strokeColorHex, isDark) {
        buildSvgHtmlDocument(
            svgContent = svgContent,
            strokeColorHex = strokeColorHex,
            initialStep = initialStep,
            initialPlaying = initialPlaying,
            isDark = isDark
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(0) // Fondo 100% transparente para mostrar la retícula 米
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    useWideViewPort = false
                    loadWithOverviewMode = true
                    cacheMode = WebSettings.LOAD_NO_CACHE
                }

                // Puente JavaScript -> Android Kotlin para sincronizar el trazo activo
                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun onStroke(step: Int, @Suppress("UNUSED_PARAMETER") total: Int) {
                            Handler(Looper.getMainLooper()).post {
                                onStepChanged(step)
                            }
                        }
                    },
                    "AndroidBridge"
                )

                loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
                onWebViewReady(this)
            }
        },
        update = { webView ->
            onWebViewReady(webView)
        },
        onRelease = { webView ->
            try {
                webView.stopLoading()
                webView.loadUrl("about:blank")
                webView.clearHistory()
                webView.removeAllViews()
                webView.destroy()
            } catch (_: Exception) {}
        }
    )
}

/**
 * Construye el documento HTML interactivo que encapsula el SVG de AnimCJK / MakeMeAHanzi.
 */
private fun buildSvgHtmlDocument(
    svgContent: String,
    strokeColorHex: String,
    initialStep: Int,
    initialPlaying: Boolean,
    isDark: Boolean
): String {
    val ghostFill = if (isDark) "#333D39" else "#DDD6CE"

    return """
        <!DOCTYPE html>
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
        <style>
          * { margin: 0; padding: 0; box-sizing: border-box; }
          html, body {
            width: 100%; height: 100%;
            background-color: transparent !important;
            overflow: hidden;
            display: flex;
            justify-content: center;
            align-items: center;
          }
          svg {
            width: 92% !important;
            height: 92% !important;
            max-width: 100%;
            max-height: 100%;
          }
          svg.acjk {
            width: 100%; height: 100%;
            display: block;
            overflow: visible;
          }
          /* Silueta de fondo del carácter en gris suave o carbón adaptativo */
          svg.acjk path[id] {
            fill: $ghostFill !important;
          }
          /* Trazos pincelados interactivos */
          svg.acjk path[clip-path] {
            stroke-width: 128;
            stroke-linecap: round;
            fill: none;
            stroke: $strokeColorHex !important;
            stroke-dasharray: 3337;
            stroke-dashoffset: 3339;
          }
          @keyframes zk {
            to { stroke-dashoffset: 0; }
          }
        </style>
        </head>
        <body>
        <div id="wrapper" style="width:100%;height:100%;display:flex;justify-content:center;align-items:center;">
          $svgContent
        </div>
        <script>
        var totalStrokes = 0;
        var currentStep = $initialStep;
        var isPlaying = $initialPlaying;
        var timer = null;

        function init() {
          var paths = document.querySelectorAll("svg.acjk path[clip-path]");
          totalStrokes = paths.length;
          if (isPlaying) {
            play();
          } else {
            setStep(currentStep);
          }
        }

        function setStep(step) {
          stopPlay();
          var paths = document.querySelectorAll("svg.acjk path[clip-path]");
          totalStrokes = paths.length;
          currentStep = Math.max(1, Math.min(step, totalStrokes));
          for (var i = 0; i < paths.length; i++) {
            paths[i].style.animation = "none";
            if (i < currentStep) {
              paths[i].style.strokeDashoffset = "0";
            } else {
              paths[i].style.strokeDashoffset = "3339";
            }
          }
          if (window.AndroidBridge && window.AndroidBridge.onStroke) {
            window.AndroidBridge.onStroke(currentStep, totalStrokes);
          }
        }

        function play() {
          stopPlay();
          isPlaying = true;
          var paths = document.querySelectorAll("svg.acjk path[clip-path]");
          totalStrokes = paths.length;
          currentStep = 0;
          for (var i = 0; i < paths.length; i++) {
            paths[i].style.animation = "none";
            paths[i].style.strokeDashoffset = "3339";
          }
          
          function nextStroke() {
            if (!isPlaying) return;
            if (currentStep < totalStrokes) {
              currentStep++;
              var p = paths[currentStep - 1];
              p.style.animation = "zk 0.65s linear forwards";
              if (window.AndroidBridge && window.AndroidBridge.onStroke) {
                window.AndroidBridge.onStroke(currentStep, totalStrokes);
              }
              timer = setTimeout(nextStroke, 800);
            } else {
              timer = setTimeout(function() {
                if (isPlaying) {
                  play();
                }
              }, 1600);
            }
          }
          nextStroke();
        }

        function stopPlay() {
          isPlaying = false;
          if (timer) clearTimeout(timer);
        }

        function replay() {
          play();
        }

        window.onload = init;
        </script>
        </body>
        </html>
    """.trimIndent()
}
