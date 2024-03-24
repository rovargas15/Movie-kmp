package movies

import model.Movie

sealed interface SearchUiState {
    data object Init : SearchUiState

    data object Loading : SearchUiState

    data object Success : SearchUiState

    data object Error : SearchUiState

    data object OnBackPress : SearchUiState

    data class OnDetailMovie(val movie: Movie) : SearchUiState
}
