package movies

import model.Movie

sealed interface MovieAction {
    data object Init : MovieAction

    data class OnSelectMenu(val navRoute: BottomNavRoute) : MovieAction

    data class OnSelectMovie(val movie: Movie) : MovieAction

    data object OnSearchView : MovieAction

    data class OnPagingView(val category: String) : MovieAction
}
