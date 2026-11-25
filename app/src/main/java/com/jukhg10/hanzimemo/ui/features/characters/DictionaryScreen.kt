package com.jukhg10.hanzimemo.ui.features.characters
import com.jukhg10.hanzimemo.data.PinyinFormatter

import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.Word

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Search
import com.jukhg10.hanzimemo.ui.features.characters.components.HandwritingSearchSheet
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel,
    onCharacterSelected: (Int) -> Unit = {},
    onWordSelected: (Int) -> Unit = {}
) {
    val strings = LocalAppStrings.current
    val dictionaryItems by viewModel.items.collectAsState()
    val mode by viewModel.mode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val studiedKeys by viewModel.studiedItemKeys.collectAsState()
    var showHandwritingSheet by remember { mutableStateOf(false) }

val listState = rememberLazyListState()

LaunchedEffect(searchQuery) {
        if (dictionaryItems.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📖", fontSize = 20.sp)
                        Text(strings.dictionaryTitle, fontWeight = FontWeight.Bold)
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
        ) {
            // Modern Search Bar with Handwriting Button
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { 
                    Text(
                        text = strings.searchPlaceholder, 
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    ) 
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = strings.searchContentDescription)
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Clear, contentDescription = strings.clearSearch)
                            }
                        }
                        IconButton(
                            onClick = { showHandwritingSheet = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Draw,
                                contentDescription = strings.writeByHand,
                                tint = MaterialTheme.colorScheme.primary
                            )
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

// Mode Selector (Characters / Words)
            ModeSelector(
                currentMode = mode,
                charactersLabel = strings.modeCharacters,
                wordsLabel = strings.modeWords,
                onModeChange = { newMode -> viewModel.setMode(newMode) }
            )

Spacer(modifier = Modifier.height(8.dp))

Box(modifier = Modifier.weight(1f)) {
                if (dictionaryItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isNotBlank()) 
                                "${strings.noResultsFor} '$searchQuery'"
                            else 
                                strings.dictionaryEmptyHint,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(dictionaryItems, key = { item ->
                            when (item) {
                                is DictionaryItem.CharacterItem -> "char_${item.character.id}"
                                is DictionaryItem.WordItem -> "word_${item.word.id}"
                            }
                        }) { item ->
                            when (item) {
                                is DictionaryItem.CharacterItem -> CharacterCard(
                                    item = item,
                                    isBeingStudied = "character_${item.character.id}" in studiedKeys,
                                    viewModel = viewModel,
                                    onCardClick = { onCharacterSelected(item.character.id) }
                                )
                                is DictionaryItem.WordItem -> WordCard(
                                    item = item,
                                    isBeingStudied = "word_${item.word.id}" in studiedKeys,
                                    viewModel = viewModel,
                                    onCardClick = { onWordSelected(item.word.id) }
                                )
                            }
                        }
                    }

}
            }
        }
    }

if (showHandwritingSheet) {
        HandwritingSearchSheet(
            recognitionEngine = viewModel.recognitionEngine,
            onDismissRequest = { showHandwritingSheet = false },
            onSelectCharacter = { char ->
                viewModel.onSearchQueryChange(char)
                showHandwritingSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeSelector(
    currentMode: DictionaryMode,
    charactersLabel: String,
    wordsLabel: String,
    onModeChange: (DictionaryMode) -> Unit
) {
    val tabs = listOf(charactersLabel, wordsLabel)
    val selectedIndex = if (currentMode == DictionaryMode.CHARACTERS) 0 else 1

PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = {
                    val newMode = if (index == 0) DictionaryMode.CHARACTERS else DictionaryMode.WORDS
                    onModeChange(newMode)
                },
                text = { Text(text = title, fontWeight = FontWeight.SemiBold) }
            )
        }
    }
}

@Composable
fun CharacterCard(
    item: DictionaryItem.CharacterItem,
    isBeingStudied: Boolean = false,
    viewModel: DictionaryViewModel?,
    onCardClick: () -> Unit = {}
) {
    val strings = LocalAppStrings.current
    val titleText = if (item.character.traditional.isNotEmpty() && item.character.traditional != item.character.simplified) {
        "${item.character.simplified} (${item.character.traditional})"
    } else {
        item.character.simplified
    }

Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = titleText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = PinyinFormatter.format(item.character.pinyin),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = strings.formatDefinition(item.character.definition),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (viewModel != null) {
                IconButton(
                    onClick = { viewModel.addOrRemoveItem(item, isBeingStudied) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isBeingStudied) Icons.Outlined.CheckCircle else Icons.Outlined.AddCircleOutline,
                        contentDescription = strings.studyStatusLabel,
                        tint = if (isBeingStudied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WordCard(
    item: DictionaryItem.WordItem,
    isBeingStudied: Boolean = false,
    viewModel: DictionaryViewModel?,
    onCardClick: () -> Unit = {}
) {
    val strings = LocalAppStrings.current
    val titleText = if (item.word.traditional.isNotEmpty() && item.word.traditional != item.word.simplified) {
        "${item.word.simplified} (${item.word.traditional})"
    } else {
        item.word.simplified
    }

Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = titleText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = PinyinFormatter.format(item.word.pinyin),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = strings.formatDefinition(item.word.definition),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (viewModel != null) {
                IconButton(
                    onClick = { viewModel.addOrRemoveItem(item, isBeingStudied) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isBeingStudied) Icons.Outlined.CheckCircle else Icons.Outlined.AddCircleOutline,
                        contentDescription = strings.studyStatusLabel,
                        tint = if (isBeingStudied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}
