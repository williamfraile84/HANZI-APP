package com.jukhg10.hanzimemo.ui.features.flashcard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import com.jukhg10.hanzimemo.ui.theme.LocalIsDarkTheme
import com.jukhg10.hanzimemo.ui.theme.getPinyinToneColor

@Composable
fun CharacterIllustrationCard(
    info: EnhancedCharacterInfo
) {
    val strings = LocalAppStrings.current
    val isDark = LocalIsDarkTheme.current
    val toneColor = getPinyinToneColor(info.pinyinTone, isDark)

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
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = strings.visualIllustrationTitle,
                    tint = toneColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = strings.visualIllustrationTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Área de Representación Gráfica
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                toneColor.copy(alpha = if (isDark) 0.18f else 0.12f),
                                toneColor.copy(alpha = if (isDark) 0.05f else 0.03f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = toneColor.copy(alpha = if (isDark) 0.4f else 0.25f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = info.simplified,
                        fontSize = 62.sp,
                        fontWeight = FontWeight.Bold,
                        color = toneColor.copy(alpha = if (isDark) 0.95f else 0.85f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val currentLang = com.jukhg10.hanzimemo.ui.i18n.LocalAppLanguage.current
                    val defDisplay = if (currentLang == com.jukhg10.hanzimemo.data.preferences.AppLanguage.SPANISH && info.curatedSpanishMeanings.isNotEmpty()) {
                        info.curatedSpanishMeanings.first()
                    } else {
                        strings.formatDefinition(info.primaryDefinition)
                    }
                    Text(
                        text = "「 ${defDisplay.uppercase()} 」",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = strings.formatIllustrationHelp(info),
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}
