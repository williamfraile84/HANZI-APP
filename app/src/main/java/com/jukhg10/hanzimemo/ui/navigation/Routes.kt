package com.jukhg10.hanzimemo.ui.navigation

sealed class Routes(val route: String) {
    object Dictionary : Routes("dictionary")
    object Flashcards : Routes("flashcards")
    object Handwriting : Routes("handwriting")
    object Camera : Routes("camera")
    object Review : Routes("review")
    object About : Routes("about")
    
    object FlashcardWithId : Routes("flashcard/{type}/{id}") {
        fun createRoute(type: String, id: Int): String = "flashcard/$type/$id"
    }
}

// Compatibility object to allow legacy calls like Screen.Dictionary or Screen.FlashcardWithId
@Deprecated("Use Routes instead for idiomatic navigation", ReplaceWith("Routes"))
object Screen {
    val Dictionary = Routes.Dictionary
    val Flashcards = Routes.Flashcards
    val Handwriting = Routes.Handwriting
    val Camera = Routes.Camera
    val Review = Routes.Review
    val About = Routes.About
    val FlashcardWithId = Routes.FlashcardWithId
}
