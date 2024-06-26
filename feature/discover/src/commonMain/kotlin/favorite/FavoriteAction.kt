package favorite

import model.Movie
import movies.BottomNavRoute

sealed interface FavoriteAction {
    data object Init : FavoriteAction

    data class OnSelectMenu(val navRoute: BottomNavRoute) : FavoriteAction

    data class OnSelectMovie(val movie: Movie) : FavoriteAction

    data class RemoveMovieFavorite(val movie: Movie) : FavoriteAction

    data class ConfirmRemoveMovieFavorite(val movie: Movie) : FavoriteAction

    data object CleanStatus : FavoriteAction
}
