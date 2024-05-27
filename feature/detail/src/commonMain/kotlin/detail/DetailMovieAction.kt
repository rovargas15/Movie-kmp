package detail

import model.Movie

sealed interface DetailMovieAction {
    data object OnBackPress : DetailMovieAction

    data object CleanStatus : DetailMovieAction

    data class SetMovieFavorite(val movie: Movie) : DetailMovieAction

    data class RemoveMovieFavorite(val movie: Movie) : DetailMovieAction

    data class ConfirmRemoveMovieFavorite(val movie: Movie) : DetailMovieAction
}
