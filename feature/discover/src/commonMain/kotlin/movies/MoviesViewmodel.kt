package movies

import Category.POPULAR
import Category.TOP_RATED
import Category.UPCOMING
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
) : ViewModel(), DefaultLifecycleObserver {
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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch(coroutineDispatcher) {
            getMovies(POPULAR)
            getMovies(TOP_RATED)
            getMovies(UPCOMING)
        }
    }

    fun managerAction(action: MovieAction) {
        when (action) {
            MovieAction.Init -> {
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

            MovieAction.OnSearchView -> {
                movieUiState.value = MovieUiState.OnSearchView
            }

            is MovieAction.OnPagingView -> {
                movieUiState.value = MovieUiState.OnPagingView(action.category)
            }
        }
    }
}
