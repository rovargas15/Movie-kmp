package detail

import model.Movie

sealed interface DetailMovieUiState {
    data object Init : DetailMovieUiState

    data object Loading : DetailMovieUiState

    data class Success(val movie: Movie) : DetailMovieUiState

    data object Error : DetailMovieUiState
}
