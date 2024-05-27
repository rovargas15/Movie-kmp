package detail

import model.Movie

sealed interface DetailMovieUiState {
    data object Init : DetailMovieUiState

    data object OnBack : DetailMovieUiState

    data class OnConfirmRemoveFavorite(val movie: Movie) : DetailMovieUiState
}
