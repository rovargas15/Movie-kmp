package movies

import model.Movie

sealed interface SearchAction {
    data object Init : SearchAction

    data class QueryMovie(val query: String) : SearchAction

    data object Search : SearchAction

    data object CleanStatus : SearchAction

    data class OnSelectMovie(val movie: Movie) : SearchAction

    data object OnBackPress : SearchAction
}
