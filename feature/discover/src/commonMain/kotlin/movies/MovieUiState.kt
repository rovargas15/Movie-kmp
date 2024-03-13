package movies

import model.Movie

sealed interface MovieUiState {
    data object Init : MovieUiState

    data object Loading : MovieUiState

    data class Success(val movies: List<Movie>) : MovieUiState

    data object Error : MovieUiState
}
