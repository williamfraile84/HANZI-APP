package com.jukhg10.hanzimemo.ui.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// PALETA DE DISEÑO MODERNO MATERIAL 3: INSPIRACIÓN CALIGRÁFICA NEO-ASIÁTICA
// =========================================================================

// --- Colores Primarios (Jade Imperial) ---
val ImperialJadePrimaryLight = Color(0xFF006C51)
val ImperialJadeOnPrimaryLight = Color(0xFFFFFFFF)
val ImperialJadeContainerLight = Color(0xFF91F5D9)
val ImperialJadeOnContainerLight = Color(0xFF002017)

val ImperialJadePrimaryDark = Color(0xFF74D8BE)
val ImperialJadeOnPrimaryDark = Color(0xFF003828)
val ImperialJadeContainerDark = Color(0xFF00513C)
val ImperialJadeOnContainerDark = Color(0xFF91F5D9)

// --- Colores Secundarios (Cinabrio / Bermellón de Sello Caligráfico) ---
val CinnabarSecondaryLight = Color(0xFFB3261E)
val CinnabarOnSecondaryLight = Color(0xFFFFFFFF)
val CinnabarContainerLight = Color(0xFFFFDAD6)
val CinnabarOnContainerLight = Color(0xFF410002)

val CinnabarSecondaryDark = Color(0xFFFFB4AB)
val CinnabarOnSecondaryDark = Color(0xFF690005)
val CinnabarContainerDark = Color(0xFF93000A)
val CinnabarOnContainerDark = Color(0xFFFFDAD6)

// --- Colores Terciarios (Ámbar y Madera Calígrafa) ---
val AmberTertiaryLight = Color(0xFF825500)
val AmberOnTertiaryLight = Color(0xFFFFFFFF)
val AmberContainerLight = Color(0xFFFFDDB3)
val AmberOnContainerLight = Color(0xFF291800)

val AmberTertiaryDark = Color(0xFFFFB951)
val AmberOnTertiaryDark = Color(0xFF452B00)
val AmberContainerDark = Color(0xFF633F00)
val AmberOnContainerDark = Color(0xFFFFDDB3)

// --- Superficies y Fondos - TEMA CLARO (Papel de Arroz / Seda Tibia) ---
val RicePaperBackgroundLight = Color(0xFFFAF9F5)
val RicePaperOnBackgroundLight = Color(0xFF191C1B)
val RicePaperSurfaceLight = Color(0xFFFFFFFF)
val RicePaperOnSurfaceLight = Color(0xFF191C1B)
val RicePaperSurfaceVariantLight = Color(0xFFEBE8E1)
val RicePaperOnSurfaceVariantLight = Color(0xFF404945)
val RicePaperOutlineLight = Color(0xFF707974)
val RicePaperOutlineVariantLight = Color(0xFFBFC9C3)

// --- Superficies y Fondos - TEMA OSCURO (Tinta China / Carbón Profundo) ---
val DarkInkBackgroundDark = Color(0xFF101413)
val DarkInkOnBackgroundDark = Color(0xFFE1E3DF)
val DarkInkSurfaceDark = Color(0xFF181C1B)
val DarkInkOnSurfaceDark = Color(0xFFE1E3DF)
val DarkInkSurfaceVariantDark = Color(0xFF242A28)
val DarkInkOnSurfaceVariantDark = Color(0xFFBFC9C3)
val DarkInkOutlineDark = Color(0xFF89938D)
val DarkInkOutlineVariantDark = Color(0xFF404945)

// --- Estados y Retroalimentación ---
val SuccessEmerald = Color(0xFF00875A)
val SuccessEmeraldContainer = Color(0xFFB5F3D6)
val ErrorCrimson = Color(0xFFBA1A1A)
val WarningAmber = Color(0xFFF39C12)

// =========================================================================
// PALETA PEDAGÓGICA PARA TONOS PINYIN (CON CONTRASTE ACCESIBLE WCAG)
// =========================================================================

val Tone1Light = Color(0xFFC0392B) // Tono 1 (Alto y Plano): Rojo Bermellón
val Tone1Dark = Color(0xFFFF897D)

val Tone2Light = Color(0xFF0E8A5F) // Tono 2 (Ascendente): Verde Jade
val Tone2Dark = Color(0xFF5CD8A5)

val Tone3Light = Color(0xFF1976D2) // Tono 3 (Descendente-Ascendente): Azul Zafiro
val Tone3Dark = Color(0xFF82B1FF)

val Tone4Light = Color(0xFF7B1FA2) // Tono 4 (Descendente): Púrpura Imperial
val Tone4Dark = Color(0xFFCE93D8)

val Tone5Light = Color(0xFF5C605E) // Tono 5 (Neutro): Gris Tinta
val Tone5Dark = Color(0xFFB0BEC5)

/**
 * Retorna el color accesible asociado al número de tono Pinyin (1 a 5).
 */
fun getPinyinToneColor(tone: Int, isDark: Boolean = false): Color {
    return if (!isDark) {
        when (tone) {
            1 -> Tone1Light
            2 -> Tone2Light
            3 -> Tone3Light
            4 -> Tone4Light
            else -> Tone5Light
        }
    } else {
        when (tone) {
            1 -> Tone1Dark
            2 -> Tone2Dark
            3 -> Tone3Dark
            4 -> Tone4Dark
            else -> Tone5Dark
        }
    }
}
