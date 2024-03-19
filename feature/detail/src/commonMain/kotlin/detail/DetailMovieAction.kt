package detail

import model.Movie

sealed interface DetailMovieAction {
    data object OnBackPress : DetailMovieAction

    data object CleanStatus : DetailMovieAction

    data class AddFavorite(val movie: Movie) : DetailMovieAction
}
