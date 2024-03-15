package movies

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import usecase.GetMovieByCategory

class MoviesViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getMovieByCategory: GetMovieByCategory,
    val bottomNavRoute: BottomNavRoute,
) : ViewModel() {
    private val movieUiState = MutableStateFlow<MovieUiState>(MovieUiState.Init)
    val uiState: StateFlow<MovieUiState>
        get() = movieUiState

    private fun getMovies() {
        viewModelScope.launch(coroutineDispatcher) {
            getMovieByCategory.invoke(bottomNavRoute.route).fold(
                onSuccess = {
                    movieUiState.value = MovieUiState.Success(it.results)
                },
                onFailure = {
                    movieUiState.value = MovieUiState.Error
                },
            )
        }
    }

    fun managerAction(action: MovieAction) {
        when (action) {
            MovieAction.CleanStatus -> {
                movieUiState.value = MovieUiState.Init
            }

            is MovieAction.OnSelectMenu -> {
                if (bottomNavRoute.route != action.navRoute.route) {
                    movieUiState.value = MovieUiState.OnShowOptionMenu(action.navRoute)
                }
            }

            is MovieAction.OnSelectMovie -> {
                movieUiState.value = MovieUiState.OnShowDetail(action.movie)
            }
            MovieAction.Init -> {
                getMovies()
            }
        }
    }
}
