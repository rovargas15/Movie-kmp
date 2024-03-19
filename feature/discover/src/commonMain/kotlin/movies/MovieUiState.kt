package movies

import model.Movie

sealed interface MovieUiState {
    data object Init : MovieUiState

    data object Loading : MovieUiState

    data object Success : MovieUiState

    data class OnShowDetail(val movie: Movie) : MovieUiState

    data class OnShowOptionMenu(val bottomNavRoute: BottomNavRoute) : MovieUiState

    data object Error : MovieUiState
}
