import model.Movie

sealed interface PagingUiState {
    data object Init : PagingUiState

    data object Loading : PagingUiState

    data object Success : PagingUiState

    data class OnShowDetail(val movie: Movie) : PagingUiState

    data object Error : PagingUiState

    data object OnBackPress : PagingUiState
}
