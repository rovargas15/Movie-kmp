package favorite

import Category
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
import usecase.GetMovieByCategory

class FavoriteViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getMovieByCategory: GetMovieByCategory,
    val bottomNavRoute: BottomNavRoute,
) : ViewModel() {
    private val movieUiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Init)
    val uiState: StateFlow<FavoriteUiState>
        get() = movieUiState

    var moviesFavorite by mutableStateOf(listOf<Movie>())
        private set

    private fun getMovies() {
        viewModelScope.launch {
            getMovieByCategory.invoke(Category.POPULAR).fold(
                onSuccess = {
                    moviesFavorite = it.results
                    movieUiState.value = FavoriteUiState.Success
                },
                onFailure = {
                    movieUiState.value = FavoriteUiState.Error
                },
            )
        }
    }

    fun managerAction(action: FavoriteAction) {
        when (action) {
            FavoriteAction.CleanStatus -> {
                movieUiState.value = FavoriteUiState.Init
            }

            is FavoriteAction.OnSelectMovie -> {
                movieUiState.value = FavoriteUiState.OnShowDetail(action.movie)
            }
            FavoriteAction.Init -> {
                getMovies()
            }

            is FavoriteAction.OnSelectMenu -> {
                if (bottomNavRoute.route != action.navRoute.route) {
                    movieUiState.value = FavoriteUiState.OnShowOptionMenu(action.navRoute)
                }
            }
        }
    }
}
