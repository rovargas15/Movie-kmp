package favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Movie
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import movies.BottomNavRoute
import usecase.GetFavoriteMovie
import usecase.UpdateMovie

class FavoriteViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getFavoriteMovie: GetFavoriteMovie,
    val bottomNavRoute: BottomNavRoute,
    private val updateMovie: UpdateMovie,
) : ViewModel() {
    private val movieUiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Init)
    val uiState: StateFlow<FavoriteUiState>
        get() = movieUiState

    var moviesFavorite by mutableStateOf(listOf<Movie>())
        private set

    private fun getMovies() {
        viewModelScope.launch {
            getFavoriteMovie.invoke().collect {
                moviesFavorite = it
                movieUiState.value = FavoriteUiState.Success
            }
        }
    }

    fun managerAction(action: FavoriteAction) {
        when (action) {
            is FavoriteAction.CleanStatus -> {
                movieUiState.value = FavoriteUiState.Init
            }

            is FavoriteAction.OnSelectMovie -> {
                movieUiState.value = FavoriteUiState.OnShowDetail(action.movie)
            }

            is FavoriteAction.Init -> {
                getMovies()
            }

            is FavoriteAction.OnSelectMenu -> {
                if (bottomNavRoute.route != action.navRoute.route) {
                    movieUiState.value = FavoriteUiState.OnShowOptionMenu(action.navRoute)
                }
            }

            is FavoriteAction.OnRemoveFavorite -> {
                updateMovie(action.movie)
            }
        }
    }

    private fun updateMovie(movie: Movie) {
        viewModelScope.launch(coroutineDispatcher) {
            updateMovie.invoke(movie.copy(isFavorite = !movie.isFavorite))
        }
    }
}
