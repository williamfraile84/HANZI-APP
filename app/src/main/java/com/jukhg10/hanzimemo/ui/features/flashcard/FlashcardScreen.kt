package com.jukhg10.hanzimemo.ui.features.flashcard
import androidx.compose.material.icons.outlined.AddCircleOutline

import androidx.compose.material.icons.outlined.CheckCircle

import com.jukhg10.hanzimemo.data.PinyinFormatter

import com.jukhg10.hanzimemo.data.repository.ReviewPair

import com.jukhg10.hanzimemo.ui.navigation.Routes

import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem

import com.jukhg10.hanzimemo.ui.features.flashcard.components.*

import androidx.activity.compose.BackHandler

import androidx.compose.animation.*

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.automirrored.filled.VolumeUp

import androidx.compose.material.icons.filled.Flip

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalLifecycleOwner

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import androidx.lifecycle.Lifecycle

import androidx.lifecycle.LifecycleEventObserver

import androidx.navigation.NavController

import com.jukhg10.hanzimemo.data.audio.TextToSpeechManager

import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings

enum class StudyTab { STUDYING, LEARNED }

@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun FlashcardScreen(

viewModel: FlashcardViewModel,

navController: NavController? = null,

studyItemId: Int? = null,

itemType: String = ""

) {

val strings = LocalAppStrings.current

val uiState by viewModel.uiState.collectAsState()

var studyTab by remember { mutableStateOf(StudyTab.STUDYING) }

val lifecycleOwner = LocalLifecycleOwner.current

val hasInitialized = remember { mutableStateOf(false) }

val isDetailMode = studyItemId != null && studyItemId > 0 && itemType.isNotEmpty() && itemType != "session"

if (isDetailMode && navController != null) {

BackHandler {

if (!navController.popBackStack()) {

navController.navigate(Routes.Dictionary.route) {

popUpTo(0) { inclusive = true }

}

}

}

}

LaunchedEffect(studyItemId, itemType) {

if (studyItemId != null && studyItemId > 0 && itemType.isNotEmpty()) {

viewModel.loadDetailOnly(itemType, studyItemId)

hasInitialized.value = true

}

}

LaunchedEffect(Unit) {

if (!isDetailMode && !hasInitialized.value) {

viewModel.startSession()

hasInitialized.value = true

}

}

DisposableEffect(lifecycleOwner, isDetailMode) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!isDetailMode) {
                    viewModel.syncDeckOnResume()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

Scaffold(

topBar = {

TopAppBar(

title = {

Text(

text = if (isDetailMode) {
                            if (itemType == "word" || uiState.detailDictionaryItem is com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem.WordItem) {
                                strings.wordDetailTitle
                            } else {
                                strings.characterDetailTitle
                            }
                        } else {
                            strings.flashcardsTitle
                        },

fontWeight = FontWeight.Bold

)

},

navigationIcon = {

if (navController != null && isDetailMode) {

IconButton(

onClick = {

if (!navController.popBackStack()) {

navController.navigate(Routes.Dictionary.route) {

popUpTo(0) { inclusive = true }

}

}

}

) {

Icon(

imageVector = Icons.AutoMirrored.Filled.ArrowBack,

contentDescription = strings.backToDictionary

)

}

}

},

actions = {

if (isDetailMode) {

if (studyItemId != null && studyItemId > 0) {

IconButton(

onClick = {

viewModel.toggleDetailStudyStatus(itemType, studyItemId)

}

) {

Icon(

imageVector = if (uiState.isCurrentItemStudied) Icons.Outlined.CheckCircle else Icons.Outlined.AddCircleOutline,

contentDescription = if (uiState.isCurrentItemStudied) strings.inStudyList else strings.addToStudyList,

tint = if (uiState.isCurrentItemStudied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

)

}

}

} else if (uiState.isRevealed) {

FilledTonalButton(

onClick = { viewModel.toggleRevealCard() },

shape = RoundedCornerShape(12.dp),

contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),

modifier = Modifier.padding(end = 8.dp)

) {

Icon(

imageVector = Icons.Default.Flip,

contentDescription = strings.viewFront,

modifier = Modifier.size(16.dp)

)

Spacer(modifier = Modifier.width(4.dp))

Text(

text = strings.flipToFront,

fontWeight = FontWeight.Bold,

fontSize = 13.sp

)

}

}

},

colors = TopAppBarDefaults.topAppBarColors(

containerColor = MaterialTheme.colorScheme.surface

)

)

}

) { innerPadding ->

if (isDetailMode) {

DetailScreenContent(

uiState = uiState,

viewModel = viewModel,

modifier = Modifier.padding(innerPadding)

)

} else {

Column(

modifier = Modifier

.fillMaxSize()

.padding(innerPadding)

) {

val characterCount by viewModel.characterCount.collectAsState()

val wordCount by viewModel.wordCount.collectAsState()

val progressPercentage by viewModel.progressPercentage.collectAsState()

val learnedCountValue by viewModel.learnedCount.collectAsState()

val totalCountValue by viewModel.totalCount.collectAsState()

val studyingCountValue by viewModel.studyingCount.collectAsState()

LaunchedEffect(uiState.currentStudyStatus) {

studyTab = if (uiState.currentStudyStatus == StudyStatus.STUDYING) StudyTab.STUDYING else StudyTab.LEARNED

}

Column(

modifier = Modifier

.fillMaxSize()

.padding(horizontal = 16.dp, vertical = 8.dp),

verticalArrangement = Arrangement.SpaceBetween

) {

Column(modifier = Modifier.fillMaxWidth()) {

if (totalCountValue > 0) {

Column(

modifier = Modifier

.fillMaxWidth()

.padding(bottom = 8.dp)

) {

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.SpaceBetween,

verticalAlignment = Alignment.CenterVertically

) {

Text(

text = "${strings.progressLabel}: $learnedCountValue / $totalCountValue",

style = MaterialTheme.typography.titleMedium,

fontWeight = FontWeight.SemiBold

)

Text(

text = String.format("%.1f%%", progressPercentage),

style = MaterialTheme.typography.titleMedium,

fontWeight = FontWeight.Bold,

color = MaterialTheme.colorScheme.primary

)

}

LinearProgressIndicator(

progress = { progressPercentage / 100f },

modifier = Modifier

.fillMaxWidth()

.height(8.dp)

.padding(top = 6.dp),

color = MaterialTheme.colorScheme.primary,

trackColor = MaterialTheme.colorScheme.surfaceVariant

)

}

}

if (totalCountValue > 0) {

Row(

modifier = Modifier

.fillMaxWidth()

.padding(vertical = 2.dp),

horizontalArrangement = Arrangement.SpaceAround

) {

Text(

text = "${strings.charactersLabel}: $characterCount",

style = MaterialTheme.typography.bodyMedium,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

Text(

text = "${strings.wordsLabel}: $wordCount",

style = MaterialTheme.typography.bodyMedium,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

}

}

if (totalCountValue > 0) {

PrimaryTabRow(

selectedTabIndex = if (studyTab == StudyTab.STUDYING) 0 else 1,

modifier = Modifier.padding(vertical = 4.dp)

) {

Tab(

selected = studyTab == StudyTab.STUDYING,

onClick = {

studyTab = StudyTab.STUDYING

viewModel.switchStudyStatus(StudyStatus.STUDYING)

},

text = { Text("${strings.tabStudying} ($studyingCountValue)", fontWeight = FontWeight.SemiBold) }

)

Tab(

selected = studyTab == StudyTab.LEARNED,

onClick = {

studyTab = StudyTab.LEARNED

viewModel.switchStudyStatus(StudyStatus.LEARNED)

},

text = { Text("${strings.tabLearned} ($learnedCountValue)", fontWeight = FontWeight.SemiBold) }

)

}

}

}

Box(

modifier = Modifier

.fillMaxWidth()

.weight(1f)

.padding(vertical = 8.dp),

contentAlignment = Alignment.Center

) {

when {

totalCountValue == 0 -> {

Text(

text = strings.emptyStudyHint,

textAlign = TextAlign.Center,

style = MaterialTheme.typography.bodyLarge,

color = MaterialTheme.colorScheme.onSurfaceVariant,

modifier = Modifier.padding(16.dp)

)

}

uiState.currentItem != null -> {

EnhancedFlashcardCard(

item = uiState.currentItem,

enhancedInfo = uiState.enhancedCharacterInfo,

isRevealed = uiState.isRevealed,

recognitionEngine = viewModel.recognitionEngine,

onCardClick = { viewModel.toggleRevealCard() },

onSpeakClick = { text -> viewModel.speak(text) }

)

}

studyTab == StudyTab.STUDYING && studyingCountValue == 0 -> {

Text(

text = strings.congratulationsCompleted,

textAlign = TextAlign.Center,

style = MaterialTheme.typography.bodyLarge,

color = MaterialTheme.colorScheme.onSurfaceVariant,

modifier = Modifier.padding(16.dp)

)

}

studyTab == StudyTab.LEARNED && learnedCountValue == 0 -> {

Text(

text = strings.keepPracticingEmpty,

textAlign = TextAlign.Center,

style = MaterialTheme.typography.bodyLarge,

color = MaterialTheme.colorScheme.onSurfaceVariant,

modifier = Modifier.padding(16.dp)

)

}

else -> {

CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

}

}

}

if (uiState.currentItem != null) {

ReviewControls(

isRevealed = uiState.isRevealed,

currentTab = studyTab,

onForgotClick = { viewModel.handleReviewAction(knewIt = false) },

onKnewItClick = { viewModel.handleReviewAction(knewIt = true) },

onPrevClick = { viewModel.showPreviousItem() },

onNextClick = { viewModel.showNextItem() },

onFlipClick = { viewModel.toggleRevealCard() }

)

}

}

}

}

}

}

@Composable
fun DetailScreenContent(
    uiState: FlashcardUiState,
    viewModel: FlashcardViewModel,
    itemType: String = "",
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val strings = LocalAppStrings.current

if (uiState.isLoadingDetail) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (itemType == "word" || uiState.detailDictionaryItem is DictionaryItem.WordItem) {
        val word = (uiState.detailDictionaryItem as? DictionaryItem.WordItem)?.word
        if (word != null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = word.simplified,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (word.traditional != word.simplified && word.traditional.isNotEmpty()) {
                            Text(
                                text = "${strings.traditionalLabel}: ${word.traditional}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = PinyinFormatter.format(word.pinyin),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { viewModel.speak("${word.simplified}, ${word.pinyin}") }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = strings.listenPronunciation,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = strings.formatDefinition(word.definition),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    } else if (uiState.enhancedCharacterInfo != null) {
        val info = uiState.enhancedCharacterInfo
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CharacterHeaderCard(
                info = info,
                showFlipButton = false,
                onPlayAudioClick = { viewModel.speak("${info.simplified}, ${info.pinyin}") }
            )

StrokeOrderAnimationCard(info = info)

HandwritingPracticeCard(
                info = info,
                recognitionEngine = viewModel.recognitionEngine
            )

CharacterIllustrationCard(info = info)

GraphicDecompositionCard(info = info)

MnemonicStoryCard(info = info)

EtymologyHistoryCard(info = info)

ExampleSentencesCard(info = info)

Spacer(modifier = Modifier.height(24.dp))
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable

fun EnhancedFlashcardCard(

item: ReviewPair?,

enhancedInfo: com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo?,

isRevealed: Boolean,

recognitionEngine: com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine,

onCardClick: () -> Unit,

onSpeakClick: (String) -> Unit = {}

) {

val strings = LocalAppStrings.current

val scrollState = rememberScrollState()

Card(

modifier = Modifier

.fillMaxSize()

.then(if (!isRevealed) Modifier.clickable(onClick = onCardClick) else Modifier),

shape = RoundedCornerShape(24.dp),

colors = CardDefaults.cardColors(

containerColor = MaterialTheme.colorScheme.surface

),

elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

) {

Box(

modifier = Modifier

.fillMaxSize()

.padding(16.dp),

contentAlignment = Alignment.Center

) {

if (item == null) {

CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

} else {

val dictionaryItem = item.dictionaryItem

if (!isRevealed) {

// --- ANVERSO EQUILIBRADO Y PEDAGÓGICO (SIN VACÍOS INNECESARIOS) ---

val isChar = dictionaryItem is DictionaryItem.CharacterItem

val frontText = when (dictionaryItem) {

is DictionaryItem.CharacterItem -> dictionaryItem.character.simplified

is DictionaryItem.WordItem -> dictionaryItem.word.simplified

}

val speechText = when (dictionaryItem) {

is DictionaryItem.CharacterItem -> "${dictionaryItem.character.simplified}, ${dictionaryItem.character.pinyin}"

is DictionaryItem.WordItem -> "${dictionaryItem.word.simplified}, ${dictionaryItem.word.pinyin}"

}

Column(

horizontalAlignment = Alignment.CenterHorizontally,

verticalArrangement = Arrangement.SpaceBetween,

modifier = Modifier

.fillMaxSize()

.padding(vertical = 8.dp)

) {

// Barra Superior del Anverso: Tipo y Metadatos Pedagógicos

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.SpaceBetween,

verticalAlignment = Alignment.CenterVertically

) {

Surface(

shape = RoundedCornerShape(8.dp),

color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)

) {

Text(

text = if (isChar) strings.badgeCharacter else strings.badgeWord,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

color = MaterialTheme.colorScheme.onPrimaryContainer,

modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)

)

}

if (enhancedInfo != null && enhancedInfo.radical.isNotBlank()) {

Surface(

shape = RoundedCornerShape(8.dp),

color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)

) {

Text(

text = strings.badgeRadical(enhancedInfo.radical),

fontSize = 12.sp,

fontWeight = FontWeight.SemiBold,

color = MaterialTheme.colorScheme.onSecondaryContainer,

modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

)

}

}

// Botón de audio para escuchar pronunciación en el anverso

FilledTonalIconButton(

onClick = { onSpeakClick(speechText) },

modifier = Modifier.size(36.dp)

) {

Icon(

imageVector = Icons.AutoMirrored.Filled.VolumeUp,

contentDescription = strings.listenPronunciation,

tint = MaterialTheme.colorScheme.primary,

modifier = Modifier.size(20.dp)

)

}

}

// Contenedor Estilizado del Carácter Hanzi (Encuadre Caligráfico)

Surface(

modifier = Modifier

.size(230.dp)

.padding(vertical = 12.dp),

shape = RoundedCornerShape(24.dp),

color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),

border = androidx.compose.foundation.BorderStroke(

width = 1.5.dp,

color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)

)

) {

Box(

contentAlignment = Alignment.Center,

modifier = Modifier.fillMaxSize()

) {

Text(

text = frontText,

fontSize = if (frontText.length <= 2) 96.sp else 64.sp,

fontWeight = FontWeight.Bold,

textAlign = TextAlign.Center,

color = MaterialTheme.colorScheme.primary

)

}

}

// Indicador y Botón de Volteo

Column(

horizontalAlignment = Alignment.CenterHorizontally,

verticalArrangement = Arrangement.spacedBy(8.dp),

modifier = Modifier.fillMaxWidth()

) {

if (enhancedInfo != null && enhancedInfo.strokeCount > 0) {

Text(

text = "${strings.badgeStrokes(enhancedInfo.strokeCount)} • ${strings.tapToFlip}",

fontSize = 13.sp,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

}

Button(

onClick = onCardClick,

shape = RoundedCornerShape(16.dp),

colors = ButtonDefaults.buttonColors(

containerColor = MaterialTheme.colorScheme.primaryContainer,

contentColor = MaterialTheme.colorScheme.onPrimaryContainer

)

) {

Icon(

imageVector = Icons.Default.Flip,

contentDescription = null,

modifier = Modifier.size(18.dp)

)

Spacer(modifier = Modifier.width(8.dp))

Text(

text = strings.viewLearningDetails,

fontSize = 14.sp,

fontWeight = FontWeight.SemiBold

)

}

}

}

} else {

// --- REVERSO ENRIQUECIDO CON TODAS LAS SECCIONES MNEMOTÉCNICAS ---

Column(

modifier = Modifier

.fillMaxSize()

.verticalScroll(scrollState),

horizontalAlignment = Alignment.CenterHorizontally

) {

if (enhancedInfo != null) {

CharacterHeaderCard(

info = enhancedInfo,

onPlayAudioClick = { onSpeakClick("${enhancedInfo.simplified}, ${enhancedInfo.pinyin}") },

onFlipClick = onCardClick

)

StrokeOrderAnimationCard(info = enhancedInfo)

HandwritingPracticeCard(

info = enhancedInfo,

recognitionEngine = recognitionEngine

)

CharacterIllustrationCard(info = enhancedInfo)

GraphicDecompositionCard(info = enhancedInfo)

MnemonicStoryCard(info = enhancedInfo)

EtymologyHistoryCard(info = enhancedInfo)

ExampleSentencesCard(info = enhancedInfo)

Spacer(modifier = Modifier.height(12.dp))

OutlinedButton(

onClick = onCardClick,

shape = RoundedCornerShape(12.dp),

modifier = Modifier.padding(bottom = 16.dp)

) {

Icon(imageVector = Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(16.dp))

Spacer(modifier = Modifier.width(6.dp))

Text(strings.viewFront, fontSize = 13.sp)

}

} else {

val pinyin: String

val definition: String

val traditional: String

val simplified: String

when (dictionaryItem) {

is DictionaryItem.CharacterItem -> {

simplified = dictionaryItem.character.simplified

pinyin = dictionaryItem.character.pinyin

definition = dictionaryItem.character.definition

traditional = dictionaryItem.character.traditional

}

is DictionaryItem.WordItem -> {

simplified = dictionaryItem.word.simplified

pinyin = dictionaryItem.word.pinyin

definition = dictionaryItem.word.definition

traditional = dictionaryItem.word.traditional

}

}

// Reverso de Palabras y Fichas sin detalle gráfico

Column(

modifier = Modifier

.fillMaxWidth()

.padding(16.dp),

horizontalAlignment = Alignment.CenterHorizontally,

verticalArrangement = Arrangement.spacedBy(16.dp)

) {

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.SpaceBetween,

verticalAlignment = Alignment.CenterVertically

) {

Surface(

shape = RoundedCornerShape(8.dp),

color = MaterialTheme.colorScheme.secondaryContainer

) {

Text(

text = strings.studyDetailBadge,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

color = MaterialTheme.colorScheme.onSecondaryContainer,

modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

)

}

FilledTonalIconButton(

onClick = { onSpeakClick("$simplified, $pinyin") },

modifier = Modifier.size(36.dp)

) {

Icon(

imageVector = Icons.AutoMirrored.Filled.VolumeUp,

contentDescription = strings.listenPronunciation,

tint = MaterialTheme.colorScheme.primary,

modifier = Modifier.size(20.dp)

)

}

}

Surface(

shape = RoundedCornerShape(20.dp),

color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),

modifier = Modifier.padding(vertical = 8.dp)

) {

Text(

text = simplified,

fontSize = 72.sp,

fontWeight = FontWeight.Bold,

color = MaterialTheme.colorScheme.primary,

modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)

)

}

Text(

text = pinyin,

fontSize = 28.sp,

fontWeight = FontWeight.Bold,

color = MaterialTheme.colorScheme.secondary

)

if (traditional != simplified) {

Surface(

shape = RoundedCornerShape(8.dp),

color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)

) {

Text(

text = "${strings.traditionalLabel}: $traditional",

fontSize = 14.sp,

color = MaterialTheme.colorScheme.onSurfaceVariant,

modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)

)

}

}

HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

Card(

shape = RoundedCornerShape(16.dp),

colors = CardDefaults.cardColors(

containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

),

modifier = Modifier.fillMaxWidth()

) {

Text(

text = strings.formatDefinition(definition),

fontSize = 16.sp,

lineHeight = 24.sp,

fontWeight = FontWeight.Medium,

color = MaterialTheme.colorScheme.onSurface,

textAlign = TextAlign.Center,

modifier = Modifier.padding(16.dp)

)

}

OutlinedButton(

onClick = onCardClick,

shape = RoundedCornerShape(12.dp)

) {

Icon(imageVector = Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(16.dp))

Spacer(modifier = Modifier.width(6.dp))

Text(strings.viewFront, fontSize = 13.sp)

}

}

}

}

}

}

}

}

}

@Composable

fun ReviewControls(

isRevealed: Boolean,

currentTab: StudyTab,

onForgotClick: () -> Unit,

onKnewItClick: () -> Unit,

onPrevClick: () -> Unit,

onNextClick: () -> Unit,

onFlipClick: () -> Unit = {}

) {

val strings = LocalAppStrings.current

val btnPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)

Column(

modifier = Modifier

.fillMaxWidth()

.padding(vertical = 4.dp),

horizontalAlignment = Alignment.CenterHorizontally,

verticalArrangement = Arrangement.spacedBy(8.dp)

) {

if (!isRevealed) {

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.spacedBy(8.dp),

verticalAlignment = Alignment.CenterVertically

) {

OutlinedButton(

onClick = onPrevClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1f)

.height(44.dp),

shape = RoundedCornerShape(14.dp)

) {

Text(

text = strings.prevCard,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

if (currentTab == StudyTab.STUDYING) {

Button(

onClick = onKnewItClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1f)

.height(44.dp),

colors = ButtonDefaults.buttonColors(

containerColor = MaterialTheme.colorScheme.primary,

contentColor = Color.White

),

shape = RoundedCornerShape(14.dp)

) {

Text(

text = strings.iKnewIt,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

} else {

Button(

onClick = onForgotClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1f)

.height(44.dp),

colors = ButtonDefaults.buttonColors(

containerColor = MaterialTheme.colorScheme.error,

contentColor = Color.White

),

shape = RoundedCornerShape(14.dp)

) {

Text(

text = strings.iForgotIt,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

}

OutlinedButton(

onClick = onNextClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1f)

.height(44.dp),

shape = RoundedCornerShape(14.dp)

) {

Text(

text = strings.nextCard,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

}

} else {

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.spacedBy(8.dp),

verticalAlignment = Alignment.CenterVertically

) {

OutlinedButton(

onClick = onPrevClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1f)

.height(44.dp),

shape = RoundedCornerShape(14.dp)

) {

Text(

text = strings.prevCard,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

FilledTonalButton(

onClick = onFlipClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1.2f)

.height(44.dp),

colors = ButtonDefaults.filledTonalButtonColors(

containerColor = MaterialTheme.colorScheme.primaryContainer,

contentColor = MaterialTheme.colorScheme.onPrimaryContainer

),

shape = RoundedCornerShape(14.dp)

) {

Icon(

imageVector = Icons.Default.Flip,

contentDescription = strings.viewFront,

modifier = Modifier.size(16.dp)

)

Spacer(modifier = Modifier.width(4.dp))

Text(

text = strings.viewFront,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

Button(

onClick = onNextClick,

contentPadding = btnPadding,

modifier = Modifier

.weight(1f)

.height(44.dp),

colors = ButtonDefaults.buttonColors(

containerColor = MaterialTheme.colorScheme.primary,

contentColor = Color.White

),

shape = RoundedCornerShape(14.dp)

) {

Text(

text = strings.nextCard,

fontSize = 12.sp,

fontWeight = FontWeight.Bold,

maxLines = 1,

overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

)

}

}

}

}

}

