package detail

sealed interface DetailMovieUiState {
    data object Init : DetailMovieUiState

    data object Success : DetailMovieUiState

    data object OnBack : DetailMovieUiState
}
