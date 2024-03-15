package detail

sealed interface DetailMovieUiState {
    data object Init : DetailMovieUiState

    data object Loading : DetailMovieUiState

    data object Success : DetailMovieUiState

    data object Error : DetailMovieUiState

    data object OnBack : DetailMovieUiState
}
