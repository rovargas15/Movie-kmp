import model.Movie

sealed interface PagingAction {
    data object Init : PagingAction

    data object OnBackPress : PagingAction

    data class OnSelectMovie(val movie: Movie) : PagingAction
}
