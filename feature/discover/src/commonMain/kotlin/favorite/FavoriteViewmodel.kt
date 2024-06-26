package favorite

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Movie
import movies.BottomNavRoute
import usecase.GetFavoriteMovie
import usecase.UpdateMovie

class FavoriteViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getFavoriteMovie: GetFavoriteMovie,
    val bottomNavRoute: BottomNavRoute,
    private val updateMovie: UpdateMovie,
) : ViewModel(), DefaultLifecycleObserver {

    init {
        getMovies()
    }

    private val movieUiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Init)
    val uiState: StateFlow<FavoriteUiState>
        get() = movieUiState

    var moviesFavorite = mutableStateOf(listOf<Movie>())
        private set

    private fun getMovies() {
        viewModelScope.launch(coroutineDispatcher) {
            getFavoriteMovie.invoke().collect {
                moviesFavorite.value = it
            }
        }
    }

    fun handleAction(action: FavoriteAction) {
        when (action) {
            is FavoriteAction.CleanStatus -> {
                movieUiState.value = FavoriteUiState.Init
            }

            is FavoriteAction.OnSelectMovie -> {
                movieUiState.value = FavoriteUiState.OnShowDetail(action.movie)
            }

            is FavoriteAction.Init -> {
            }

            is FavoriteAction.OnSelectMenu -> {
                if (bottomNavRoute.route != action.navRoute.route) {
                    movieUiState.value = FavoriteUiState.OnShowOptionMenu(action.navRoute)
                }
            }

            is FavoriteAction.RemoveMovieFavorite -> {
                updateMovie(action.movie)
                movieUiState.value = FavoriteUiState.Init
            }

            is FavoriteAction.ConfirmRemoveMovieFavorite -> {
                movieUiState.value = FavoriteUiState.OnConfirmRemoveFavorite(action.movie)
            }
        }
    }

    private fun updateMovie(movie: Movie) {
        viewModelScope.launch(coroutineDispatcher) {
            updateMovie.invoke(movie.copy(isFavorite = !movie.isFavorite))
        }
    }
}
