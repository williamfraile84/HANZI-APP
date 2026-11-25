package com.jukhg10.hanzimemo.ui.features.review
import com.jukhg10.hanzimemo.data.PinyinFormatter

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.material.icons.filled.Clear

import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem

import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings

import com.jukhg10.hanzimemo.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun ReviewScreen(

viewModel: ReviewViewModel,

navController: NavController? = null

) {

val strings = LocalAppStrings.current

val searchQuery by viewModel.searchQuery.collectAsState()

val characterCount by viewModel.characterCount.collectAsState()

val wordCount by viewModel.wordCount.collectAsState()

val progressPercentage by viewModel.progressPercentage.collectAsState()

val learnedCount by viewModel.learnedCount.collectAsState()

val totalCount by viewModel.totalCount.collectAsState()

val studyingItems by viewModel.studyingItems.collectAsState()

val learnedItems by viewModel.learnedItems.collectAsState()

var selectedTab by remember { mutableIntStateOf(0) }

Scaffold(

topBar = {

TopAppBar(

title = { Text(strings.progressScreenTitle, fontWeight = FontWeight.Bold) },

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

) {

// --- Barra de Búsqueda ---

OutlinedTextField(

value = searchQuery,

onValueChange = { viewModel.onSearchQueryChange(it) },

modifier = Modifier

.fillMaxWidth()

.padding(vertical = 8.dp),

placeholder = { Text(strings.progressSearchPlaceholder, fontSize = 14.sp) },

leadingIcon = {

Icon(Icons.Default.Search, contentDescription = strings.searchContentDescription)

},

trailingIcon = {

if (searchQuery.isNotEmpty()) {

IconButton(onClick = { viewModel.onSearchQueryChange("") }) {

Icon(Icons.Default.Clear, contentDescription = strings.clearSearch)

}

}

},

singleLine = true,

shape = RoundedCornerShape(28.dp),

colors = OutlinedTextFieldDefaults.colors(

focusedBorderColor = MaterialTheme.colorScheme.primary,

unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant

)

)

// --- Tarjeta de Progreso ---

if (totalCount > 0) {

Card(

modifier = Modifier

.fillMaxWidth()

.padding(vertical = 8.dp),

colors = CardDefaults.cardColors(

containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

),

shape = RoundedCornerShape(16.dp)

) {

Column(

modifier = Modifier

.fillMaxWidth()

.padding(16.dp)

) {

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.SpaceBetween,

verticalAlignment = Alignment.CenterVertically

) {

Text(

text = "${strings.progressLabel}: $learnedCount / $totalCount",

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

.clip(RoundedCornerShape(4.dp))

.padding(top = 8.dp),

color = MaterialTheme.colorScheme.primary,

trackColor = MaterialTheme.colorScheme.surfaceVariant

)

Spacer(modifier = Modifier.height(12.dp))

Row(

modifier = Modifier.fillMaxWidth(),

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

}

}

// --- Subpestañas de Estado ---

TabRow(

selectedTabIndex = selectedTab,

containerColor = MaterialTheme.colorScheme.surface,

contentColor = MaterialTheme.colorScheme.primary,

modifier = Modifier

.clip(RoundedCornerShape(12.dp))

.padding(vertical = 8.dp)

) {

Tab(

selected = selectedTab == 0,

onClick = { selectedTab = 0 },

text = { 

Text(

text = "${strings.tabStudying} (${studyingItems.size})", 

fontWeight = FontWeight.SemiBold 

) 

}

)

Tab(

selected = selectedTab == 1,

onClick = { selectedTab = 1 },

text = { 

Text(

text = "${strings.tabLearned} (${learnedItems.size})", 

fontWeight = FontWeight.SemiBold 

) 

}

)

}

// --- Lista de Contenido ---

val displayItems = if (selectedTab == 0) studyingItems else learnedItems

if (displayItems.isEmpty()) {

Box(

modifier = Modifier

.fillMaxSize()

.padding(32.dp),

contentAlignment = Alignment.Center

) {

Text(

text = if (searchQuery.isNotBlank()) {

"${strings.noResultsFor} '$searchQuery'"

} else {

if (selectedTab == 0) strings.emptyStudyHint else strings.keepPracticingEmpty

},

style = MaterialTheme.typography.bodyLarge,

color = MaterialTheme.colorScheme.onSurfaceVariant,

textAlign = TextAlign.Center

)

}

} else {

LazyColumn(

verticalArrangement = Arrangement.spacedBy(8.dp),

contentPadding = PaddingValues(vertical = 8.dp)

) {

items(displayItems, key = { item ->

when (item) {

is DictionaryItem.CharacterItem -> "char_${item.character.id}"

is DictionaryItem.WordItem -> "word_${item.word.id}"

}

}) { item ->

ReviewItemCard(

item = item,

isStudyingTab = selectedTab == 0,

onCardClick = {

if (navController != null) {

val route = when (item) {

is DictionaryItem.CharacterItem -> Routes.FlashcardWithId.createRoute("character", item.character.id)

is DictionaryItem.WordItem -> Routes.FlashcardWithId.createRoute("word", item.word.id)

}

navController.navigate(route)

}

},

onToggleLearned = {

val itemId = when (item) {

is DictionaryItem.CharacterItem -> item.character.id

is DictionaryItem.WordItem -> item.word.id

}

val itemType = when (item) {

is DictionaryItem.CharacterItem -> "character"

is DictionaryItem.WordItem -> "word"

}

viewModel.markAsLearned(itemId, itemType, learned = selectedTab == 0)

},

onRemove = {

viewModel.removeItem(item)

}

)

}

}

}

}

}

}

@Composable

fun ReviewItemCard(

item: DictionaryItem,

isStudyingTab: Boolean,

onCardClick: () -> Unit,

onToggleLearned: () -> Unit,

onRemove: () -> Unit

) {

val strings = LocalAppStrings.current

val title: String

val pinyin: String

val definition: String

when (item) {

is DictionaryItem.CharacterItem -> {

title = item.character.simplified

pinyin = PinyinFormatter.format(item.character.pinyin)

definition = item.character.definition

}

is DictionaryItem.WordItem -> {

title = item.word.simplified

pinyin = PinyinFormatter.format(item.word.pinyin)

definition = item.word.definition

}

}

Card(

modifier = Modifier

.fillMaxWidth()

.clickable { onCardClick() },

shape = RoundedCornerShape(16.dp),

colors = CardDefaults.cardColors(

containerColor = MaterialTheme.colorScheme.surface

),

elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

) {

Row(

modifier = Modifier

.padding(16.dp)

.fillMaxWidth(),

verticalAlignment = Alignment.CenterVertically

) {

Text(

text = title,

fontSize = 28.sp,

fontWeight = FontWeight.Bold,

color = MaterialTheme.colorScheme.primary,

modifier = Modifier.width(64.dp)

)

Column(modifier = Modifier.weight(1f)) {

Text(

text = pinyin,

style = MaterialTheme.typography.titleMedium,

fontWeight = FontWeight.SemiBold,

color = MaterialTheme.colorScheme.secondary

)

Spacer(modifier = Modifier.height(2.dp))

Text(

text = strings.formatDefinition(definition),

style = MaterialTheme.typography.bodyMedium,

maxLines = 1,

overflow = TextOverflow.Ellipsis,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

}

Row(verticalAlignment = Alignment.CenterVertically) {

IconButton(onClick = onToggleLearned) {

Icon(

imageVector = if (isStudyingTab) Icons.Default.CheckCircle else Icons.Default.Refresh,

contentDescription = if (isStudyingTab) strings.markAsLearnedDesc else strings.markAsStudyingDesc,

tint = if (isStudyingTab) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary

)

}

IconButton(onClick = onRemove) {

Icon(

imageVector = Icons.Default.Delete,

contentDescription = strings.removeItemDesc,

tint = MaterialTheme.colorScheme.error

)

}

}

}

}

}

