package com.jukhg10.hanzimemo.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.jukhg10.hanzimemo.data.preferences.AppLanguage
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo

/**
 * Contrato de cadenas localizadas multiidioma para la interfaz de HanziMemo.
 */
interface AppStrings {
    // --- Navegación Inferior ---
    val navDictionary: String
    val navFlashcards: String
    val navCalligraphy: String
    val navCamera: String
    val navProgress: String
    val navAbout: String

    // --- Diccionario ---
    val dictionaryTitle: String
    val searchPlaceholder: String
    val searchContentDescription: String
    val clearSearch: String
    val writeByHand: String
    val modeCharacters: String
    val modeWords: String
    val noResultsFor: String
    val dictionaryEmptyHint: String
    val addToStudyList: String
    val inStudyList: String

    // --- Flashcards (Anverso) ---
    val flashcardsTitle: String
    val characterDetailTitle: String
    val wordDetailTitle: String
    val backToDictionary: String
    val studyStatusLabel: String
    val badgeCharacter: String
    val badgeWord: String
    fun badgeRadical(radical: String): String
    fun badgeStrokes(count: Int): String
    val tapToFlip: String
    val viewLearningDetails: String
    val listenPronunciation: String
    val progressLabel: String
    val tabStudying: String
    val tabLearned: String
    val congratulationsCompleted: String
    val keepPracticingEmpty: String
    val emptyStudyHint: String

    // --- Flashcards (Reverso & Secciones) ---
    val flipToFront: String
    val viewFront: String
    val strokeOrderTitle: String
    fun strokeStepFormat(current: Int, total: Int): String
    val playAnimation: String
    val pauseAnimation: String
    val prevStroke: String
    val nextStroke: String
    val restartStroke: String
    val handwritingSectionTitle: String
    val handwritingInstruction: String
    val writeModeActive: String
    val scrollModeActive: String
    val visualIllustrationTitle: String
    val graphicDecompTitle: String
    val mnemonicRuleTitle: String
    val etymologyTitle: String
    val exampleSentencesTitle: String
    val traditionalLabel: String
    val prevCard: String
    val nextCard: String
    val iKnewIt: String
    val iForgotIt: String

    val roleRadical: String
    val rolePhonetic: String
    val rolePictograph: String
    val roleStructural: String
    val etymologyOriginLabel: String
    val etymologyEvolutionLabel: String
    val markAsLearnedDesc: String
    val markAsStudyingDesc: String
    val removeItemDesc: String
    val cameraScreenSubtitle: String
    val studyDetailBadge: String

    // --- Taller de Caligrafía ---
    val calligraphyScreenTitle: String
    val calligraphySelectHint: String
    val freeMode: String
    val strokeEvaluatedSuccess: String
    val strokeEvaluatedRetry: String
    val clearCanvas: String
    val undoStroke: String
    val showGuide: String
    val hideGuide: String

    // --- Reconocimiento por Cámara ---
    val cameraScreenTitle: String
    val cameraCapture: String
    val gallerySelect: String
    val cropTitle: String
    val cropInstruction: String
    val cropConfirm: String
    val cropCancel: String
    val cameraPermissionTitle: String
    val cameraPermissionMsg: String
    val grantPermission: String
    val scanningProcessing: String
    val matchesFound: String
    val noMatchesFound: String

    // --- Progreso y SRS ---
    val progressScreenTitle: String
    val progressSearchPlaceholder: String
    val progressCardTitle: String
    val totalStudied: String
    val charactersLabel: String
    val wordsLabel: String

    // --- Acerca de & Configuración ---
    val aboutScreenTitle: String
    val appSubtitle: String
    val appVersionBadge: String
    val settingsTitle: String
    val themeSelectorTitle: String
    val themeSystem: String
    val themeLight: String
    val themeDark: String
    val languageSelectorTitle: String
    val langSpanish: String
    val langEnglish: String
    val langChinese: String
    val projectPurposeTitle: String
    val projectPurposeContent: String
    val devTeamTitle: String
    val featuresTitle: String
    val copyrightFooter: String

    // --- Localización Dinámica de Contenidos y Definiciones ---
    fun formatDefinition(raw: String): String
    fun formatMnemonicStory(info: EnhancedCharacterInfo): String
    fun formatEtymology(info: EnhancedCharacterInfo): String
    fun formatEvolution(info: EnhancedCharacterInfo): String
    fun formatIllustrationHelp(info: EnhancedCharacterInfo): String
    fun formatExampleSentence(chinese: String, pinyin: String, originalSpanish: String): String
    fun getRadicalMeaning(symbol: String, defaultMeaning: String = ""): String
    val frequentWordsTitle: String

    // --- Características de Acerca De ---
    val featureHandwritingTitle: String
    val featureHandwritingDesc: String
    val featureNeuralTitle: String
    val featureNeuralDesc: String
    val featureStrokesTitle: String
    val featureStrokesDesc: String
    val featureFlashcardsTitle: String
    val featureFlashcardsDesc: String
    val featureCameraTitle: String
    val featureCameraDesc: String
    val featureDictionaryTitle: String
    val featureDictionaryDesc: String
    val teamRoleSoftwareDev: String
    fun studentIdBadge(code: String): String

    // --- OCR y Cámara Dinámica ---
    val cameraNoCharsDetectedTitle: String
    val cameraNoCharsDetectedDesc: String
    val cameraDetectedTextLabel: String
    fun cameraResultsCount(count: Int): String
    val cameraClearBtn: String
    fun cameraScanError(msg: String): String

    // --- Búsqueda por Escritura y Lienzo ---
    val invalidQueryError: String
    val handwritingSearchTitle: String
    val handwritingSearchSubtitle: String
    val candidatesDetected: String
    val changeGridContentDesc: String
    val toggleGuideContentDesc: String
    val undoStrokeContentDesc: String
    val clearCanvasContentDesc: String
    val actionClose: String
}

// =========================================================================
// IMPLEMENTACIÓN EN ESPAÑOL (PREDETERMINADO)
// =========================================================================
object StringsEs : AppStrings {
    override val navDictionary = "Buscar"
    override val navFlashcards = "Fichas"
    override val navCalligraphy = "Trazos"
    override val navCamera = "Cámara"
    override val navProgress = "Avance"
    override val navAbout = "Ajustes"

    override val dictionaryTitle = "Diccionario Hanzi"
    override val searchPlaceholder = "Buscar Hanzi, Pinyin o significado..."
    override val searchContentDescription = "Buscar"
    override val clearSearch = "Limpiar búsqueda"
    override val writeByHand = "Escribir a mano"
    override val modeCharacters = "Caracteres"
    override val modeWords = "Palabras"
    override val noResultsFor = "No se encontraron resultados para"
    override val dictionaryEmptyHint = "Ingresa un término en el buscador o pulsa ✍️ para trazar"
    override val addToStudyList = "Agregar a lista de estudio"
    override val inStudyList = "En estudio"

    override val flashcardsTitle = "Flashcards Interactivas"
    override val characterDetailTitle = "Detalle de Carácter"
    override val wordDetailTitle = "Detalle de Palabra"
    override val backToDictionary = "Volver al Diccionario"
    override val studyStatusLabel = "Estado de estudio"
    override val badgeCharacter = "Carácter Hanzi"
    override val badgeWord = "Palabra Compuesta"
    override fun badgeRadical(radical: String) = "Radical: $radical"
    override fun badgeStrokes(count: Int) = "$count trazos"
    override val tapToFlip = "Toca para voltear"
    override val viewLearningDetails = "Ver detalles pedagógicos"
    override val listenPronunciation = "Escuchar pronunciación"
    override val progressLabel = "Progreso"
    override val tabStudying = "En Estudio"
    override val tabLearned = "Aprendidas"
    override val congratulationsCompleted = "¡Felicidades! Has completado el repaso de todos los elementos."
    override val keepPracticingEmpty = "¡Sigue practicando! Aún no has marcado elementos como aprendidos."
    override val emptyStudyHint = "Selecciona un carácter o palabra desde el diccionario para iniciar tu estudio mnemotécnico."

    override val flipToFront = "Anverso"
    override val viewFront = "Ver Anverso"
    override val strokeOrderTitle = "Orden de Escritura y Trazos"
    override fun strokeStepFormat(current: Int, total: Int) = "Trazo $current / $total"
    override val playAnimation = "Reproducir Animación"
    override val pauseAnimation = "Pausar Animación"
    override val prevStroke = "Trazo Anterior"
    override val nextStroke = "Siguiente Trazo"
    override val restartStroke = "Reiniciar"
    override val handwritingSectionTitle = "Práctica de Escritura a Mano Caligráfica"
    override val handwritingInstruction = "Traza el carácter en la cuadrícula 米字格. El motor evaluará tus trazos en tiempo real."
    override val writeModeActive = "Escribir (Activo)"
    override val scrollModeActive = "Desplazar (Lectura)"
    override val visualIllustrationTitle = "Representación Mnemotécnica Visual"
    override val graphicDecompTitle = "Descomposición y Estructura Gráfica"
    override val mnemonicRuleTitle = "Regla Mnemotécnica de Retención"
    override val etymologyTitle = "Origen Etimológico y Evolución Histórica"
    override val exampleSentencesTitle = "Oraciones de Ejemplo y Contexto"
    override val traditionalLabel = "Tradicional"
    override val prevCard = "← Anterior"
    override val nextCard = "Siguiente →"
    override val iKnewIt = "Lo Sabía"
    override val iForgotIt = "Lo Olvidé"

    override val roleRadical = "Radical"
    override val rolePhonetic = "Fonético"
    override val rolePictograph = "Pictograma"
    override val roleStructural = "Estructural"
    override val etymologyOriginLabel = "Origen:"
    override val etymologyEvolutionLabel = "Evolución Lingüística:"
    override val markAsLearnedDesc = "Marcar como aprendida"
    override val markAsStudyingDesc = "Marcar como en estudio"
    override val removeItemDesc = "Eliminar de lista de estudio"
    override val cameraScreenSubtitle = "Toma una foto o elige una imagen de la galería para reconocer caracteres Hanzi al instante"
    override val studyDetailBadge = "Detalle de Estudio"

    override val calligraphyScreenTitle = "Taller de Caligrafía y Escritura"
    override val calligraphySelectHint = "Selecciona un carácter de práctica o escribe libremente:"
    override val freeMode = "Modo Libre ✍️"
    override val strokeEvaluatedSuccess = "¡Excelente coincidencia caligráfica!"
    override val strokeEvaluatedRetry = "Sigue practicando las proporciones"
    override val clearCanvas = "Limpiar"
    override val undoStroke = "Deshacer"
    override val showGuide = "Mostrar Guía"
    override val hideGuide = "Ocultar Guía"

    override val cameraScreenTitle = "Reconocimiento Óptico por Cámara"
    override val cameraCapture = "Tomar Foto"
    override val gallerySelect = "Elegir de Galería"
    override val cropTitle = "Ajustar y Recortar Área"
    override val cropInstruction = "Enfoca el carácter chino dentro del encuadre para un OCR preciso"
    override val cropConfirm = "Aceptar Recorte"
    override val cropCancel = "Cancelar"
    override val cameraPermissionTitle = "Permiso de Cámara Requerido"
    override val cameraPermissionMsg = "Se requiere acceso a la cámara para digitalizar caracteres chinos impresos o manuscritos."
    override val grantPermission = "Conceder Permiso"
    override val scanningProcessing = "Analizando texto chino..."
    override val matchesFound = "Caracteres detectados"
    override val noMatchesFound = "No se detectaron caracteres chinos en la imagen seleccionada"

    override val progressScreenTitle = "Gestión e Historial de Estudio"
    override val progressSearchPlaceholder = "Buscar en lista de estudio..."
    override val progressCardTitle = "Progreso General"
    override val totalStudied = "Total en estudio"
    override val charactersLabel = "Caracteres"
    override val wordsLabel = "Palabras"

    override val aboutScreenTitle = "Acerca de HanziMemo"
    override val appSubtitle = "Plataforma Inteligente de Aprendizaje de Caracteres Chinos"
    override val appVersionBadge = "Versión 2.1.0 • Neo-Asiática M3"
    override val settingsTitle = "Preferencias de Apariencia e Idioma"
    override val themeSelectorTitle = "Tema Visual"
    override val themeSystem = "Sistema ⚙️"
    override val themeLight = "Claro ☀️"
    override val themeDark = "Oscuro 🌙"
    override val languageSelectorTitle = "Idioma de la Interfaz"
    override val langSpanish = "Español 🇪🇸"
    override val langEnglish = "English 🇺🇸"
    override val langChinese = "中文 🇨🇳"
    override val projectPurposeTitle = "Propósito del Proyecto"
    override val projectPurposeContent = "HanziMemo 2 es una aplicación móvil avanzada diseñada para facilitar y enriquecer el dominio de los caracteres chinos (Hanzi). Integra reconocimiento óptico (OCR), escritura caligráfica a mano alzada con reconocimiento digital neuronal on-device, animación canónica del orden de trazos y flashcards pedagógicas enriquecidas con etimología auténtica, historias mnemotécnicas y pronunciación nativa."
    override val devTeamTitle = "Equipo de Desarrollo"
    override val featuresTitle = "Características Principales"
    override val copyrightFooter = "© 2026 Proyecto HanziMemo • Adquisición y maestría del idioma chino"

    override fun formatDefinition(raw: String): String = RadicalL10nData.formatL10nDefinition(raw, AppLanguage.SPANISH)
    override fun formatMnemonicStory(info: EnhancedCharacterInfo): String = RadicalL10nData.formatMnemonicStory(info, AppLanguage.SPANISH)
    override fun formatEtymology(info: EnhancedCharacterInfo): String = RadicalL10nData.formatEtymology(info, AppLanguage.SPANISH)
    override fun formatEvolution(info: EnhancedCharacterInfo): String = RadicalL10nData.formatEvolution(info, AppLanguage.SPANISH)
    override fun formatIllustrationHelp(info: EnhancedCharacterInfo): String = RadicalL10nData.formatIllustrationHelp(info, AppLanguage.SPANISH)
    override fun formatExampleSentence(chinese: String, pinyin: String, originalSpanish: String): String = RadicalL10nData.formatL10nExampleSentence(chinese, pinyin, originalSpanish, AppLanguage.SPANISH)
    override fun getRadicalMeaning(symbol: String, defaultMeaning: String): String = RadicalL10nData.getRadicalMeaning(symbol, defaultMeaning, AppLanguage.SPANISH)
    override val frequentWordsTitle = "Palabras Frecuentes:"

    override val featureHandwritingTitle = "Escritura Caligráfica / Handwriting"
    override val featureHandwritingDesc = "Lienzo interactivo con cuadrículas 米字格 y 田字格, suavizado Bézier y silueta guía para perfeccionar el trazo."
    override val featureNeuralTitle = "Reconocimiento Neuronal On-Device"
    override val featureNeuralDesc = "Motor de reconocimiento de trazos manuscritos en tiempo real con ML Kit sin conexión a Internet."
    override val featureStrokesTitle = "Orden de Trazos Canónico"
    override val featureStrokesDesc = "Animaciones vectoriales SVG oficiales (AnimCJK y MakeMeAHanzi) con controles multimedia completos."
    override val featureFlashcardsTitle = "Flashcards Mnemotécnicas Enriquecidas"
    override val featureFlashcardsDesc = "Tarjetas pedagógicas con etimología profunda, historias para la memoria, oraciones de ejemplo y audio nativo."
    override val featureCameraTitle = "Reconocimiento Óptico OCR"
    override val featureCameraDesc = "Escaneo de caracteres chinos en vivo con cámara y recorte interactivo de alta precisión."
    override val featureDictionaryTitle = "Diccionario Integral HSK 1 - 6"
    override val featureDictionaryDesc = "Base de datos clasificada con más de 9.900 entradas, pinyin tonal y definiciones precisas."
    override val teamRoleSoftwareDev = "Desarrollador de Software"
    override fun studentIdBadge(code: String) = "ID: $code"

    override val cameraNoCharsDetectedTitle = "No se detectaron caracteres en el recorte"
    override val cameraNoCharsDetectedDesc = "Asegúrate de recortar cerca del carácter, con buena iluminación y sin reflejos."
    override val cameraDetectedTextLabel = "Texto Detectado:"
    override fun cameraResultsCount(count: Int) = "Resultados ($count)"
    override val cameraClearBtn = "Limpiar"
    override fun cameraScanError(msg: String) = "Error al reconocer imagen: $msg"

    override val invalidQueryError = "⚠ Consulta inválida. Revisa la sintaxis (p: d: h:) o los tonos pinyin."
    override val handwritingSearchTitle = "Búsqueda por Escritura a Mano"
    override val handwritingSearchSubtitle = "Dibuja el trazo para buscar sin saber su pinyin"
    override val candidatesDetected = "Candidatos detectados:"
    override val changeGridContentDesc = "Cambiar cuadrícula"
    override val toggleGuideContentDesc = "Alternar guía de trazo"
    override val undoStrokeContentDesc = "Deshacer último trazo"
    override val clearCanvasContentDesc = "Limpiar lienzo"
    override val actionClose = "Cerrar"
}

// =========================================================================
// IMPLEMENTACIÓN EN INGLÉS (ENGLISH)
// =========================================================================
object StringsEn : AppStrings {
    override val navDictionary = "Search"
    override val navFlashcards = "Cards"
    override val navCalligraphy = "Write"
    override val navCamera = "Camera"
    override val navProgress = "Stats"
    override val navAbout = "Settings"

    override val dictionaryTitle = "Hanzi Dictionary"
    override val searchPlaceholder = "Search Hanzi, Pinyin or meaning..."
    override val searchContentDescription = "Search"
    override val clearSearch = "Clear search"
    override val writeByHand = "Handwrite"
    override val modeCharacters = "Characters"
    override val modeWords = "Words"
    override val noResultsFor = "No results found for"
    override val dictionaryEmptyHint = "Type a term in the search bar or tap ✍️ to write"
    override val addToStudyList = "Add to study list"
    override val inStudyList = "Studying"

    override val flashcardsTitle = "Interactive Flashcards"
    override val characterDetailTitle = "Character Detail"
    override val wordDetailTitle = "Word Details"
    override val backToDictionary = "Back to Dictionary"
    override val studyStatusLabel = "Study status"
    override val badgeCharacter = "Hanzi Character"
    override val badgeWord = "Compound Word"
    override fun badgeRadical(radical: String) = "Radical: $radical"
    override fun badgeStrokes(count: Int) = "$count strokes"
    override val tapToFlip = "Tap to flip"
    override val viewLearningDetails = "View learning details"
    override val listenPronunciation = "Play pronunciation"
    override val progressLabel = "Progress"
    override val tabStudying = "Studying"
    override val tabLearned = "Learned"
    override val congratulationsCompleted = "Congratulations! You completed review for all selected items."
    override val keepPracticingEmpty = "Keep practicing! No items marked as learned yet."
    override val emptyStudyHint = "Select a character or word from the dictionary to begin your mnemonic study."

    override val flipToFront = "Front"
    override val viewFront = "View Front"
    override val strokeOrderTitle = "Stroke Order & Writing"
    override fun strokeStepFormat(current: Int, total: Int) = "Stroke $current / $total"
    override val playAnimation = "Play Animation"
    override val pauseAnimation = "Pause Animation"
    override val prevStroke = "Previous Stroke"
    override val nextStroke = "Next Stroke"
    override val restartStroke = "Restart"
    override val handwritingSectionTitle = "Calligraphic Handwriting Practice"
    override val handwritingInstruction = "Trace the character on the canvas grid. The neural engine evaluates strokes in real time."
    override val writeModeActive = "Write (Active)"
    override val scrollModeActive = "Scroll (Reading)"
    override val visualIllustrationTitle = "Visual Mnemonic Representation"
    override val graphicDecompTitle = "Graphic Decomposition & Structure"
    override val mnemonicRuleTitle = "Retention Mnemonic Rule"
    override val etymologyTitle = "Etymological Origin & Evolution"
    override val exampleSentencesTitle = "Example Sentences & Context"
    override val traditionalLabel = "Traditional"
    override val prevCard = "← Prev"
    override val nextCard = "Next →"
    override val iKnewIt = "I Knew It"
    override val iForgotIt = "I Forgot"

    override val roleRadical = "Radical"
    override val rolePhonetic = "Phonetic"
    override val rolePictograph = "Pictograph"
    override val roleStructural = "Structural"
    override val etymologyOriginLabel = "Origin:"
    override val etymologyEvolutionLabel = "Linguistic Evolution:"
    override val markAsLearnedDesc = "Mark as learned"
    override val markAsStudyingDesc = "Mark as studying"
    override val removeItemDesc = "Remove from study list"
    override val cameraScreenSubtitle = "Take a photo or pick an image from gallery to recognize Hanzi characters instantly"
    override val studyDetailBadge = "Study Details"

    override val calligraphyScreenTitle = "Calligraphy & Writing Workshop"
    override val calligraphySelectHint = "Choose a practice character or write freely:"
    override val freeMode = "Free Canvas ✍️"
    override val strokeEvaluatedSuccess = "Excellent stroke matching!"
    override val strokeEvaluatedRetry = "Keep practicing proper stroke proportions"
    override val clearCanvas = "Clear"
    override val undoStroke = "Undo"
    override val showGuide = "Show Guide"
    override val hideGuide = "Hide Guide"

    override val cameraScreenTitle = "Optical Camera Recognition (OCR)"
    override val cameraCapture = "Take Photo"
    override val gallerySelect = "Gallery"
    override val cropTitle = "Adjust & Crop Area"
    override val cropInstruction = "Focus on the Chinese character inside the frame for accurate OCR"
    override val cropConfirm = "Confirm Crop"
    override val cropCancel = "Cancel"
    override val cameraPermissionTitle = "Camera Permission Required"
    override val cameraPermissionMsg = "Camera access is needed to digitize printed or handwritten Chinese characters."
    override val grantPermission = "Grant Permission"
    override val scanningProcessing = "Analyzing Chinese text..."
    override val matchesFound = "Detected Characters"
    override val noMatchesFound = "No Chinese characters recognized in the image"

    override val progressScreenTitle = "Study Management & History"
    override val progressSearchPlaceholder = "Search study items..."
    override val progressCardTitle = "Overall Progress"
    override val totalStudied = "Total items in study"
    override val charactersLabel = "Characters"
    override val wordsLabel = "Words"

    override val aboutScreenTitle = "About HanziMemo"
    override val appSubtitle = "Intelligent Chinese Character Mastery Platform"
    override val appVersionBadge = "Version 2.1.0 • Neo-Asian M3"
    override val settingsTitle = "Appearance & Language Preferences"
    override val themeSelectorTitle = "Visual Theme"
    override val themeSystem = "System ⚙️"
    override val themeLight = "Light ☀️"
    override val themeDark = "Dark 🌙"
    override val languageSelectorTitle = "App Language"
    override val langSpanish = "Español 🇪🇸"
    override val langEnglish = "English 🇺🇸"
    override val langChinese = "中文 🇨🇳"
    override val projectPurposeTitle = "Project Purpose"
    override val projectPurposeContent = "HanziMemo 2 is an advanced mobile application built to master Chinese Hanzi characters. It brings together on-device optical character recognition (OCR), interactive handwriting recognition, canonical stroke-order animations, and mnemonic flashcards powered by authentic etymology, vivid stories, and native Mandarin audio."
    override val devTeamTitle = "Development Team"
    override val featuresTitle = "Key Features"
    override val copyrightFooter = "© 2026 HanziMemo Project • Chinese language mastery platform"

    override fun formatDefinition(raw: String): String = RadicalL10nData.formatL10nDefinition(raw, AppLanguage.ENGLISH)
    override fun formatMnemonicStory(info: EnhancedCharacterInfo): String = RadicalL10nData.formatMnemonicStory(info, AppLanguage.ENGLISH)
    override fun formatEtymology(info: EnhancedCharacterInfo): String = RadicalL10nData.formatEtymology(info, AppLanguage.ENGLISH)
    override fun formatEvolution(info: EnhancedCharacterInfo): String = RadicalL10nData.formatEvolution(info, AppLanguage.ENGLISH)
    override fun formatIllustrationHelp(info: EnhancedCharacterInfo): String = RadicalL10nData.formatIllustrationHelp(info, AppLanguage.ENGLISH)
    override fun formatExampleSentence(chinese: String, pinyin: String, originalSpanish: String): String = RadicalL10nData.formatL10nExampleSentence(chinese, pinyin, originalSpanish, AppLanguage.ENGLISH)
    override fun getRadicalMeaning(symbol: String, defaultMeaning: String): String = RadicalL10nData.getRadicalMeaning(symbol, defaultMeaning, AppLanguage.ENGLISH)
    override val frequentWordsTitle = "Frequent Words:"

    override val featureHandwritingTitle = "Calligraphic Handwriting"
    override val featureHandwritingDesc = "Interactive canvas with Mi Zi Ge and Tian Zi Ge grids, Bézier stroke smoothing, and guide silhouette to master writing."
    override val featureNeuralTitle = "On-Device Neural Handwriting OCR"
    override val featureNeuralDesc = "Real-time handwritten stroke evaluation and recognition powered by ML Kit completely offline."
    override val featureStrokesTitle = "Canonical Stroke Order"
    override val featureStrokesDesc = "Official vector SVG animations (AnimCJK & MakeMeAHanzi) with step-by-step playback controls."
    override val featureFlashcardsTitle = "Mnemonic Pedagogical Flashcards"
    override val featureFlashcardsDesc = "Pedagogical cards with deep etymology, memory stories, contextual sentences, and native Mandarin pronunciation."
    override val featureCameraTitle = "Optical Camera Recognition (OCR)"
    override val featureCameraDesc = "Live scanning of Chinese characters via camera with high-precision interactive cropping."
    override val featureDictionaryTitle = "Comprehensive HSK 1 - 6 Dictionary"
    override val featureDictionaryDesc = "Classified lexicon with over 9,900 entries, marked tonal pinyin, and rich definitions."
    override val teamRoleSoftwareDev = "Software Engineer"
    override fun studentIdBadge(code: String) = "ID: $code"

    override val cameraNoCharsDetectedTitle = "No characters detected in crop"
    override val cameraNoCharsDetectedDesc = "Make sure to crop tightly around the character with good lighting and no glare."
    override val cameraDetectedTextLabel = "Detected Text:"
    override fun cameraResultsCount(count: Int) = "Results ($count)"
    override val cameraClearBtn = "Clear"
    override fun cameraScanError(msg: String) = "Error recognizing image: $msg"

    override val invalidQueryError = "⚠ Invalid query. Check syntax (p: d: h:) or pinyin tone numbers."
    override val handwritingSearchTitle = "Handwriting Search"
    override val handwritingSearchSubtitle = "Draw strokes to search without knowing the pinyin"
    override val candidatesDetected = "Detected candidates:"
    override val changeGridContentDesc = "Change grid style"
    override val toggleGuideContentDesc = "Toggle stroke guide"
    override val undoStrokeContentDesc = "Undo last stroke"
    override val clearCanvasContentDesc = "Clear canvas"
    override val actionClose = "Close"
}

// =========================================================================
// IMPLEMENTACIÓN EN CHINO (SIMPLIFICADO / 简体中文)
// =========================================================================
object StringsZh : AppStrings {
    override val navDictionary = "词典"
    override val navFlashcards = "卡片"
    override val navCalligraphy = "书法"
    override val navCamera = "相机"
    override val navProgress = "进度"
    override val navAbout = "设置"

    override val dictionaryTitle = "汉字词典"
    override val searchPlaceholder = "搜索汉字、拼音或释义..."
    override val searchContentDescription = "搜索"
    override val clearSearch = "清空"
    override val writeByHand = "手写输入"
    override val modeCharacters = "单字"
    override val modeWords = "词语"
    override val noResultsFor = "未找到相关结果："
    override val dictionaryEmptyHint = "在搜索框输入文字或点击 ✍️ 开启手写检索"
    override val addToStudyList = "加入学习库"
    override val inStudyList = "学习中"

    override val flashcardsTitle = "交互式记忆卡"
    override val characterDetailTitle = "汉字详情"
    override val wordDetailTitle = "词语详情"
    override val backToDictionary = "返回词典"
    override val studyStatusLabel = "学习状态"
    override val badgeCharacter = "汉字单字"
    override val badgeWord = "复合成语"
    override fun badgeRadical(radical: String) = "部首: $radical"
    override fun badgeStrokes(count: Int) = "$count 画"
    override val tapToFlip = "轻触翻转卡片"
    override val viewLearningDetails = "查看学习详情"
    override val listenPronunciation = "播放真人发音"
    override val progressLabel = "学习进度"
    override val tabStudying = "复习中"
    override val tabLearned = "已掌握"
    override val congratulationsCompleted = "太棒了！已完成当前所有记忆卡复习。"
    override val keepPracticingEmpty = "继续加油！暂无标记为已掌握的生词。"
    override val emptyStudyHint = "请从词典选择汉字或词语开启记忆练习。"

    override val flipToFront = "正面"
    override val viewFront = "查看正面"
    override val strokeOrderTitle = "笔画笔顺规范演示"
    override fun strokeStepFormat(current: Int, total: Int) = "笔画 $current / $total"
    override val playAnimation = "播放动画"
    override val pauseAnimation = "暂停动画"
    override val prevStroke = "上一步"
    override val nextStroke = "下一步"
    override val restartStroke = "重置"
    override val handwritingSectionTitle = "米字格书法临摹练习"
    override val handwritingInstruction = "在米字格内书写汉字笔画，端侧神经网络实时评测临摹质量。"
    override val writeModeActive = "书写模式 (启用)"
    override val scrollModeActive = "滑动模式 (浏览)"
    override val visualIllustrationTitle = "图像化联想记忆"
    override val graphicDecompTitle = "汉字造字与结构分解"
    override val mnemonicRuleTitle = "汉字记忆口诀"
    override val etymologyTitle = "字源探究与历史演变"
    override val exampleSentencesTitle = "实用例句与情境"
    override val traditionalLabel = "繁体"
    override val prevCard = "← 上一个"
    override val nextCard = "下一个 →"
    override val iKnewIt = "我记住了"
    override val iForgotIt = "忘记了"

    override val roleRadical = "偏旁部首"
    override val rolePhonetic = "声旁"
    override val rolePictograph = "象形"
    override val roleStructural = "构件"
    override val etymologyOriginLabel = "来源："
    override val etymologyEvolutionLabel = "演变过程："
    override val markAsLearnedDesc = "标记为已掌握"
    override val markAsStudyingDesc = "标记为复习中"
    override val removeItemDesc = "从学习库移除"
    override val cameraScreenSubtitle = "拍照或从相册选择图片，即刻体验离线汉字光学识别"
    override val studyDetailBadge = "学习详情"

    override val calligraphyScreenTitle = "汉字书法临摹工作坊"
    override val calligraphySelectHint = "选择临摹范字或进入自由练习："
    override val freeMode = "自由练习 ✍️"
    override val strokeEvaluatedSuccess = "笔画临摹非常标准！"
    override val strokeEvaluatedRetry = "注意汉字间架结构与笔顺"
    override val clearCanvas = "清空画布"
    override val undoStroke = "撤销上笔"
    override val showGuide = "显示米字格外框"
    override val hideGuide = "隐藏指南"

    override val cameraScreenTitle = "相机离线文字识别 (OCR)"
    override val cameraCapture = "拍照识别"
    override val gallerySelect = "相册选图"
    override val cropTitle = "裁剪与对齐"
    override val cropInstruction = "调整取景框框定目标汉字以获得精准识别"
    override val cropConfirm = "确认识别"
    override val cropCancel = "取消"
    override val cameraPermissionTitle = "需要相机权限"
    override val cameraPermissionMsg = "需要相机访问权限以扫描并识别印刷体或手写汉字。"
    override val grantPermission = "授权权限"
    override val scanningProcessing = "正在分析图像中的汉字..."
    override val matchesFound = "识别到的汉字"
    override val noMatchesFound = "所选图像中未检测到有效汉字"

    override val progressScreenTitle = "学习记录与掌握分析"
    override val progressSearchPlaceholder = "在生词本中搜索..."
    override val progressCardTitle = "学习概览"
    override val totalStudied = "总生词数"
    override val charactersLabel = "单字"
    override val wordsLabel = "词语"

    override val aboutScreenTitle = "关于 HanziMemo"
    override val appSubtitle = "智能汉字沉浸式学习与记忆平台"
    override val appVersionBadge = "版本 2.1.0 • 新中式 Material 3"
    override val settingsTitle = "外观与语言偏好设置"
    override val themeSelectorTitle = "视觉主题"
    override val themeSystem = "跟随系统 ⚙️"
    override val themeLight = "宣纸暖白 ☀️"
    override val themeDark = "玄墨深炭 🌙"
    override val languageSelectorTitle = "界面语言"
    override val langSpanish = "Español 🇪🇸"
    override val langEnglish = "English 🇺🇸"
    override val langChinese = "简体中文 🇨🇳"
    override val projectPurposeTitle = "项目愿景"
    override val projectPurposeContent = "HanziMemo 2 是一款专为高效掌握汉字打造的现代移动应用。深度结合设备端离线汉字 OCR 识别、手写笔迹自适应评测、标准笔顺动态演示以及涵盖字源形声、趣味故事口诀的交互式闪卡体系。"
    override val devTeamTitle = "研发团队"
    override val featuresTitle = "核心功能特色"
    override val copyrightFooter = "© 2026 HanziMemo 团队 • 汉字认知与书法学习平台"

    override fun formatDefinition(raw: String): String = RadicalL10nData.formatL10nDefinition(raw, AppLanguage.CHINESE)
    override fun formatMnemonicStory(info: EnhancedCharacterInfo): String = RadicalL10nData.formatMnemonicStory(info, AppLanguage.CHINESE)
    override fun formatEtymology(info: EnhancedCharacterInfo): String = RadicalL10nData.formatEtymology(info, AppLanguage.CHINESE)
    override fun formatEvolution(info: EnhancedCharacterInfo): String = RadicalL10nData.formatEvolution(info, AppLanguage.CHINESE)
    override fun formatIllustrationHelp(info: EnhancedCharacterInfo): String = RadicalL10nData.formatIllustrationHelp(info, AppLanguage.CHINESE)
    override fun formatExampleSentence(chinese: String, pinyin: String, originalSpanish: String): String = RadicalL10nData.formatL10nExampleSentence(chinese, pinyin, originalSpanish, AppLanguage.CHINESE)
    override fun getRadicalMeaning(symbol: String, defaultMeaning: String): String = RadicalL10nData.getRadicalMeaning(symbol, defaultMeaning, AppLanguage.CHINESE)
    override val frequentWordsTitle = "常用词汇："

    override val featureHandwritingTitle = "米字格书法临摹"
    override val featureHandwritingDesc = "提供米字格与田字格辅线，具备贝塞尔曲线压感笔锋与标准笔顺轮廓临摹。"
    override val featureNeuralTitle = "端侧离线神经网络手写识别"
    override val featureNeuralDesc = "基于 Google ML Kit 深度学习模型，无需网络即可秒级评测手写笔迹。"
    override val featureStrokesTitle = "规范笔画与动态笔顺"
    override val featureStrokesDesc = "基于 AnimCJK 与 MakeMeAHanzi 官方矢量笔顺库，支持单步微调与连续动画。"
    override val featureFlashcardsTitle = "多维联想记忆闪卡"
    override val featureFlashcardsDesc = "融入字源演变、声旁形旁分解、趣味联想口诀、实用例句与真人发音。"
    override val featureCameraTitle = "相机光学字符识别 (OCR)"
    override val featureCameraDesc = "支持相机实时拍照与相册取图，搭配毫米级精确裁剪框快速检索汉字。"
    override val featureDictionaryTitle = "HSK 1 - 6 权威综合词典"
    override val featureDictionaryDesc = "精选收录 9,900+ 常用汉字与复合词，标注精准拼音声调与多语言释义。"
    override val teamRoleSoftwareDev = "软件开发工程师"
    override fun studentIdBadge(code: String) = "学号: $code"

    override val cameraNoCharsDetectedTitle = "裁剪区域未检测到汉字"
    override val cameraNoCharsDetectedDesc = "请尽量贴合汉字边缘裁剪，并确保光线充足、无反光遮挡。"
    override val cameraDetectedTextLabel = "识别文字："
    override fun cameraResultsCount(count: Int) = "识别结果 ($count)"
    override val cameraClearBtn = "清空"
    override fun cameraScanError(msg: String) = "图像识别发生错误: $msg"

    override val invalidQueryError = "⚠ 查询语法无效。请检查检索前缀 (p: d: h:) 或拼音声调。"
    override val handwritingSearchTitle = "手写笔画检索"
    override val handwritingSearchSubtitle = "直接手写临摹查字，无需预先掌握拼音"
    override val candidatesDetected = "识别匹配候选项："
    override val changeGridContentDesc = "切换格线样式"
    override val toggleGuideContentDesc = "显示或隐藏临摹笔影"
    override val undoStrokeContentDesc = "撤回上一笔画"
    override val clearCanvasContentDesc = "清空画布"
    override val actionClose = "关闭"
}

val LocalAppStrings = staticCompositionLocalOf<AppStrings> { StringsEs }
val LocalAppLanguage = staticCompositionLocalOf<AppLanguage> { AppLanguage.SPANISH }

fun getStringsForLanguage(language: AppLanguage): AppStrings {
    return when (language) {
        AppLanguage.SPANISH -> StringsEs
        AppLanguage.ENGLISH -> StringsEn
        AppLanguage.CHINESE -> StringsZh
    }
}

@Composable
@ReadOnlyComposable
fun appStrings(): AppStrings = LocalAppStrings.current

