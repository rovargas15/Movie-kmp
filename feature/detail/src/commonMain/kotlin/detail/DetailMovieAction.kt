package detail

sealed interface DetailMovieAction {
    data object OnBackPress : DetailMovieAction

    data object CleanStatus : DetailMovieAction
}
