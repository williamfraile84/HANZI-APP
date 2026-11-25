package com.jukhg10.hanzimemo.ui.features.flashcard.components

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.VolumeUp

import androidx.compose.material.icons.filled.Flip

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo

import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings

import com.jukhg10.hanzimemo.ui.theme.LocalIsDarkTheme

import com.jukhg10.hanzimemo.ui.theme.getPinyinToneColor

@Composable

fun CharacterHeaderCard(

info: EnhancedCharacterInfo,

showFlipButton: Boolean = true,

onPlayAudioClick: () -> Unit = {},

onFlipClick: () -> Unit = {}

) {

val strings = LocalAppStrings.current

val isDark = LocalIsDarkTheme.current

val toneColor = getPinyinToneColor(info.pinyinTone, isDark)

Card(

modifier = Modifier

.fillMaxWidth()

.padding(top = 4.dp, bottom = 2.dp),

shape = RoundedCornerShape(20.dp),

colors = CardDefaults.cardColors(

containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.45f else 0.6f)

),

elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

) {

Column(

modifier = Modifier

.fillMaxWidth()

.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),

horizontalAlignment = Alignment.CenterHorizontally

) {

// Insignias Superiores (Nivel HSK, Trazos, Radical y Botón Rápido Anverso)

Row(

modifier = Modifier.fillMaxWidth(),

horizontalArrangement = Arrangement.SpaceBetween,

verticalAlignment = Alignment.CenterVertically

) {

Surface(

shape = RoundedCornerShape(10.dp),

color = toneColor.copy(alpha = if (isDark) 0.22f else 0.15f)

) {

Text(

text = if (info.hskLevel in 1..6) "HSK ${info.hskLevel}" else "General",

fontSize = 11.sp,

fontWeight = FontWeight.Bold,

color = toneColor,

modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)

)

}

Row(

horizontalArrangement = Arrangement.spacedBy(6.dp),

verticalAlignment = Alignment.CenterVertically

) {

if (info.radical.isNotBlank()) {

Text(

text = strings.badgeRadical(info.radical),

fontSize = 12.sp,

fontWeight = FontWeight.Medium,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

Text(

text = "•",

fontSize = 12.sp,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

}

Text(

text = strings.badgeStrokes(info.strokeCount),

fontSize = 12.sp,

fontWeight = FontWeight.Medium,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

if (showFlipButton) {

Surface(

onClick = onFlipClick,

shape = RoundedCornerShape(8.dp),

color = toneColor.copy(alpha = if (isDark) 0.25f else 0.15f)

) {

Row(

verticalAlignment = Alignment.CenterVertically,

horizontalArrangement = Arrangement.spacedBy(4.dp),

modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)

) {

Icon(

imageVector = Icons.Default.Flip,

contentDescription = strings.flipToFront,

tint = toneColor,

modifier = Modifier.size(13.dp)

)

Text(

text = strings.flipToFront,

fontSize = 11.sp,

fontWeight = FontWeight.Bold,

color = toneColor,

maxLines = 1,

softWrap = false

)

}

}

}

}

}

Spacer(modifier = Modifier.height(6.dp))

// Carácter Hanzi Principal

Text(

text = info.simplified,

fontSize = 76.sp,

fontWeight = FontWeight.ExtraBold,

color = toneColor,

textAlign = TextAlign.Center

)

Spacer(modifier = Modifier.height(4.dp))

// Pronunciación Pinyin y Botón Activo de Reproducción de Voz

Row(

verticalAlignment = Alignment.CenterVertically,

horizontalArrangement = Arrangement.Center

) {

Text(

text = info.pinyin,

fontSize = 26.sp,

fontWeight = FontWeight.Bold,

color = toneColor

)

Spacer(modifier = Modifier.width(8.dp))

IconButton(

onClick = onPlayAudioClick,

modifier = Modifier

.size(34.dp)

.clip(CircleShape)

.background(toneColor.copy(alpha = if (isDark) 0.25f else 0.18f))

) {

Icon(

imageVector = Icons.AutoMirrored.Filled.VolumeUp,

contentDescription = strings.listenPronunciation,

tint = toneColor,

modifier = Modifier.size(18.dp)

)

}

}

if (info.traditional != info.simplified && info.traditional.isNotEmpty()) {

Spacer(modifier = Modifier.height(2.dp))

Text(

text = "${strings.traditionalLabel}: ${info.traditional}",

fontSize = 12.sp,

fontWeight = FontWeight.Normal,

color = MaterialTheme.colorScheme.onSurfaceVariant

)

}

Spacer(modifier = Modifier.height(6.dp))

val currentLang = com.jukhg10.hanzimemo.ui.i18n.LocalAppLanguage.current
            val displayPrimary = if (currentLang == com.jukhg10.hanzimemo.data.preferences.AppLanguage.SPANISH && info.curatedSpanishMeanings.isNotEmpty()) {
                info.curatedSpanishMeanings.first()
            } else {
                strings.formatDefinition(info.primaryDefinition)
            }

val displaySecondary = if (currentLang == com.jukhg10.hanzimemo.data.preferences.AppLanguage.SPANISH && info.curatedSpanishMeanings.size > 1) {
                info.curatedSpanishMeanings.drop(1).joinToString(", ")
            } else {
                info.secondaryDefinitions.map { strings.formatDefinition(it) }.filter { it.isNotBlank() }.joinToString(", ")
            }

Text(
                text = displayPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

if (displaySecondary.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = displaySecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
