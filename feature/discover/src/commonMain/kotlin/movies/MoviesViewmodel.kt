package movies

import Category.POPULAR
import Category.TOP_RATED
import Category.UPCOMING
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Movie
import usecase.GetMovieByCategory

class MoviesViewmodel(
    private val getMovieByCategory: GetMovieByCategory,
    private val coroutineDispatcher: CoroutineDispatcher,
    val bottomNavRoute: BottomNavRoute,
) : ViewModel() {
    private val movieUiState = MutableStateFlow<MovieUiState>(MovieUiState.Init)
    val uiState: StateFlow<MovieUiState>
        get() = movieUiState

    var moviesPopular by mutableStateOf(listOf<Movie>())
        private set

    var moviesTop by mutableStateOf(listOf<Movie>())
        private set

    var moviesUpComing by mutableStateOf(listOf<Movie>())
        private set

    private suspend fun getMovies(category: String) =
        getMovieByCategory.invoke(category).fold(
            onSuccess = {
                when (category) {
                    POPULAR -> {
                        moviesPopular = it.results
                    }

                    TOP_RATED -> {
                        moviesTop = it.results
                    }

                    UPCOMING -> {
                        moviesUpComing = it.results
                    }
                }
                movieUiState.value = MovieUiState.Success
            },
            onFailure = {
                movieUiState.value = MovieUiState.Error
            },
        )

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
                viewModelScope.launch(coroutineDispatcher) {
                    getMovies(POPULAR)
                    getMovies(TOP_RATED)
                    getMovies(UPCOMING)
                }
            }

            MovieAction.OnSearchView -> {
                movieUiState.value = MovieUiState.OnSearchView
            }
        }
    }
}
