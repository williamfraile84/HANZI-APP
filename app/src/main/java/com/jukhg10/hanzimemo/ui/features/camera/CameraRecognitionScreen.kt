package com.jukhg10.hanzimemo.ui.features.camera

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.jukhg10.hanzimemo.BuildConfig
import com.jukhg10.hanzimemo.data.HanziRecognitionService
import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.StudyItem
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.domain.search.SearchToken
import com.jukhg10.hanzimemo.domain.search.TokenType
import com.jukhg10.hanzimemo.ui.navigation.Routes
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraRecognitionScreen(
    viewModel: CameraRecognitionViewModel,
    navController: NavController? = null
) {
    val strings = LocalAppStrings.current
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    var tempPhotoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var cropSourceUri by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.setError(null)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            cropSourceUri = tempPhotoUri
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            cropSourceUri = uri
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Long,
                actionLabel = "Entendido"
            )
        }
    }

    cropSourceUri?.let { uri ->
        NativeImageCropDialog(
            imageUri = uri,
            onCropSuccess = { croppedUri ->
                cropSourceUri = null
                viewModel.recognizeFromUri(croppedUri)
            },
            onDismiss = { cropSourceUri = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.cameraScreenTitle, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cameraPermissionState.status != PermissionStatus.Granted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = strings.cameraPermissionTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.cameraPermissionMsg,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { cameraPermissionState.launchPermissionRequest() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(strings.grantPermission)
                        }
                    }
                }
            }

            // Acciones Rápidas: Cámara y Galería
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (cameraPermissionState.status == PermissionStatus.Granted) {
                            try {
                                val photoFile = File.createTempFile("photo_hanzi_", ".jpg", context.cacheDir)
                                val authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
                                val photoUri = androidx.core.content.FileProvider.getUriForFile(
                                    context,
                                    authority,
                                    photoFile
                                )
                                tempPhotoUri = photoUri
                                cameraLauncher.launch(photoUri)
                            } catch (e: Exception) {
                                viewModel.setError("Error al abrir cámara: ${e.message}")
                            }
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text(strings.cameraCapture, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text(strings.gallerySelect, fontWeight = FontWeight.Bold)
                }
            }

            if (uiState.isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = strings.scanningProcessing,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Feedback si la imagen fue procesada pero no se encontró texto
            if (!uiState.isLoading && uiState.hasProcessedImage && uiState.lastRecognizedText.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.cameraNoCharsDetectedTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.cameraNoCharsDetectedDesc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Texto Detectado
            if (uiState.lastRecognizedText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = strings.cameraDetectedTextLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = uiState.lastRecognizedText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Lista de Resultados
            if (uiState.matches.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.cameraResultsCount(uiState.matches.size),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { viewModel.clearMatches() }) {
                        Text(strings.cameraClearBtn, fontWeight = FontWeight.SemiBold)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.matches) { match ->
                        HanziMatchCard(
                            match = match,
                            onAddToStudy = {
                                if (match.isInStudyList) viewModel.removeMatchFromStudyList(match) else viewModel.addMatchToStudyList(match)
                            },
                            onOpenFlashcard = {
                                val id = match.matchedCharacter?.id ?: match.matchedWord?.id
                                val type = if (match.matchedCharacter != null) "character" else "word"
                                if (id != null) {
                                    navController?.navigate(Routes.FlashcardWithId.createRoute(type, id)) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            } else if (!uiState.isLoading && uiState.lastRecognizedText.isEmpty() && !uiState.hasProcessedImage) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = strings.cameraScreenSubtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HanziMatchCard(
    match: HanziMatch,
    onAddToStudy: () -> Unit,
    onOpenFlashcard: () -> Unit
) {
    val strings = LocalAppStrings.current
    val hasMatch = match.matchedCharacter != null || match.matchedWord != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = hasMatch) { onOpenFlashcard() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = match.hanzi,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))

                    if (match.matchedCharacter != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = strings.badgeCharacter,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    } else if (match.matchedWord != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = strings.badgeWord,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                when {
                    match.matchedCharacter != null -> {
                        val char = match.matchedCharacter
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = char.pinyin,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = strings.formatDefinition(char.definition),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                    match.matchedWord != null -> {
                        val word = match.matchedWord
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = word.pinyin,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = strings.formatDefinition(word.definition),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                    else -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.noMatchesFound,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (hasMatch) {
                    IconButton(onClick = onAddToStudy) {
                        Icon(
                            imageVector = if (match.isInStudyList)
                                Icons.Outlined.CheckCircle else
                                Icons.Outlined.AddCircleOutline,
                            contentDescription = strings.addToStudyList,
                            tint = if (match.isInStudyList) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = strings.viewLearningDetails,
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

/**
 * COMPONENTE NATIVO JETPACK COMPOSE DE RECORTE REAL DE BITMAP
 * Abre la imagen completa ajustada a la pantalla (fitScale) y permite pan/zoom intuitivo sin ampliación forzada.
 */
@Composable
fun NativeImageCropDialog(
    imageUri: Uri,
    onCropSuccess: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    var loadedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scale by remember { mutableFloatStateOf(1f) }
    var minScale by remember { mutableFloatStateOf(0.1f) }
    var maxScale by remember { mutableFloatStateOf(5f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var isScaleInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(imageUri) {
        withContext(Dispatchers.IO) {
            try {
                // Decodificar imagen con tamaño acotado para evitar OOM con fotos de 50MP
                val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                if (imageUri.scheme == "file" && imageUri.path != null) {
                    BitmapFactory.decodeFile(imageUri.path, boundsOptions)
                } else {
                    context.contentResolver.openInputStream(imageUri)?.use { 
                        BitmapFactory.decodeStream(it, null, boundsOptions) 
                    }
                }

                var sampleSize = 1
                val maxDim = 2048
                while (boundsOptions.outWidth / sampleSize > maxDim || boundsOptions.outHeight / sampleSize > maxDim) {
                    sampleSize *= 2
                }

                val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
                val bitmap = if (imageUri.scheme == "file" && imageUri.path != null) {
                    BitmapFactory.decodeFile(imageUri.path, decodeOptions)
                } else {
                    context.contentResolver.openInputStream(imageUri)?.use { 
                        BitmapFactory.decodeStream(it, null, decodeOptions) 
                    }
                }
                loadedBitmap = bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Inicializar la escala de ajuste completo a la pantalla
    LaunchedEffect(canvasSize, loadedBitmap) {
        val bmp = loadedBitmap
        if (bmp != null && canvasSize.width > 0 && canvasSize.height > 0 && !isScaleInitialized) {
            val cw = canvasSize.width.toFloat()
            val ch = canvasSize.height.toFloat()
            val fitScale = minOf(cw / bmp.width.toFloat(), ch / bmp.height.toFloat()) * 0.95f
            scale = fitScale
            minScale = fitScale * 0.5f
            maxScale = fitScale * 8f
            offset = Offset.Zero
            isScaleInitialized = true
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            if (loadedBitmap == null) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                val bitmap = loadedBitmap!!
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .onGloballyPositioned { canvasSize = it.size }
                            .pointerInput(minScale, maxScale) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(minScale, maxScale)
                                    offset += pan
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height

                            val cropSize = minOf(canvasWidth, canvasHeight) * 0.75f
                            val cropRect = Rect(
                                left = (canvasWidth - cropSize) / 2,
                                top = (canvasHeight - cropSize) / 2,
                                right = (canvasWidth + cropSize) / 2,
                                bottom = (canvasHeight + cropSize) / 2
                            )

                            val imageBitmap = bitmap.asImageBitmap()
                            val pivotPoint = Offset(canvasWidth / 2, canvasHeight / 2)

                            translate(offset.x, offset.y) {
                                scale(scale = scale, pivot = pivotPoint) {
                                    val imgLeft = (canvasWidth - imageBitmap.width) / 2
                                    val imgTop = (canvasHeight - imageBitmap.height) / 2
                                    drawImage(imageBitmap, topLeft = Offset(imgLeft, imgTop))
                                }
                            }

                            val path = Path().apply {
                                addRect(Rect(0f, 0f, canvasWidth, canvasHeight))
                                addRect(cropRect)
                                fillType = PathFillType.EvenOdd
                            }
                            drawPath(path, Color.Black.copy(alpha = 0.6f))
                            drawRect(
                                color = Color.White,
                                topLeft = cropRect.topLeft,
                                size = Size(cropSize, cropSize),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF121212))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(strings.cropCancel, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }

                        Button(onClick = {
                            val cw = canvasSize.width.toFloat()
                            val ch = canvasSize.height.toFloat()
                            if (cw <= 0f || ch <= 0f) return@Button

                            val cropSize = minOf(cw, ch) * 0.75f
                            val cropRectLeft = (cw - cropSize) / 2f
                            val cropRectTop = (ch - cropSize) / 2f

                            // Mapeo inverso de coordenadas del Canvas al Bitmap original
                            val cropLeftOnImg = (cropRectLeft - (cw / 2f) - offset.x) / scale + (bitmap.width / 2f)
                            val cropTopOnImg = (cropRectTop - (ch / 2f) - offset.y) / scale + (bitmap.height / 2f)
                            val cropSizeOnImg = cropSize / scale

                            val startX = cropLeftOnImg.toInt().coerceIn(0, bitmap.width - 1)
                            val startY = cropTopOnImg.toInt().coerceIn(0, bitmap.height - 1)
                            val cropWidth = cropSizeOnImg.toInt().coerceIn(1, bitmap.width - startX)
                            val cropHeight = cropSizeOnImg.toInt().coerceIn(1, bitmap.height - startY)

                            try {
                                val croppedBitmap = Bitmap.createBitmap(
                                    bitmap,
                                    startX,
                                    startY,
                                    cropWidth,
                                    cropHeight
                                )

                                val croppedFile = File(context.cacheDir, "crop_${System.currentTimeMillis()}.jpg")
                                val outputStream = FileOutputStream(croppedFile)
                                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                                outputStream.flush()
                                outputStream.close()
                                onCropSuccess(Uri.fromFile(croppedFile))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }) {
                            Text(strings.cropConfirm, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
