package movies

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import usecase.GetMovieByCategory

class MoviesViewmodel(
    private val getMovieByCategory: GetMovieByCategory,
) : ViewModel() {
    private val movieUiState = MutableStateFlow<MovieUiState>(MovieUiState.Init)
    val uiState: StateFlow<MovieUiState>
        get() = movieUiState

    fun getMovies(category: String) {
        viewModelScope.launch {
            getMovieByCategory.invoke(category).fold(
                onSuccess = {
                    movieUiState.value = MovieUiState.Success(it.results)
                },
                onFailure = {
                    movieUiState.value = MovieUiState.Error
                },
            )
        }
    }
}
