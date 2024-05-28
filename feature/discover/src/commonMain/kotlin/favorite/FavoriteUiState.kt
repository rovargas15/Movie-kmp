package favorite

import model.Movie
import movies.BottomNavRoute

sealed interface FavoriteUiState {
    data object Init : FavoriteUiState

    data object Loading : FavoriteUiState

    data object Success : FavoriteUiState

    data class OnShowDetail(val movie: Movie) : FavoriteUiState

    data class OnConfirmRemoveFavorite(val movie: Movie) : FavoriteUiState

    data class OnShowOptionMenu(val bottomNavRoute: BottomNavRoute) : FavoriteUiState

    data object Error : FavoriteUiState
}
