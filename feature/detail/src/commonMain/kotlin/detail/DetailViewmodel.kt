package detail

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
import model.MovieDetail
import model.MovieImage
import usecase.GetLocalMovieById
import usecase.GetMovieImageById
import usecase.GetRemoteMovieById
import usecase.UpdateMovie

class DetailViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getLocalMovieById: GetLocalMovieById,
    private val getRemoteMovieById: GetRemoteMovieById,
    private val getMovieImageById: GetMovieImageById,
    private val updateMovie: UpdateMovie,
) : ViewModel() {
    private val movieUiState = MutableStateFlow<DetailMovieUiState>(DetailMovieUiState.Init)

    var movie: Movie? by mutableStateOf(null)
        private set

    var movieDetail: MovieDetail? by mutableStateOf(null)
        private set

    var movieImage: MovieImage? by mutableStateOf(null)
        private set

    val uiState: StateFlow<DetailMovieUiState>
        get() = movieUiState

    fun onLoad(id: Int) {
        getMovieById(id)
    }

    fun handleAction(action: DetailMovieAction) {
        when (action) {
            is DetailMovieAction.CleanStatus -> {
                movieUiState.value = DetailMovieUiState.Init
            }

            is DetailMovieAction.OnBackPress -> {
                movieUiState.value = DetailMovieUiState.OnBack
            }

            is DetailMovieAction.SetMovieFavorite -> {
                updateMovie(action.movie)
            }

            is DetailMovieAction.RemoveMovieFavorite -> {
                updateMovie(action.movie)
                movieUiState.value = DetailMovieUiState.Init
            }

            is DetailMovieAction.ConfirmRemoveMovieFavorite -> {
                movieUiState.value = DetailMovieUiState.OnConfirmRemoveFavorite(action.movie)
            }
        }
    }

    private fun getMovieById(id: Int) {
        viewModelScope.launch {
            getLocalMovieById.invoke(id).collect {
                movie = it
                it.movieId.let { movieId ->
                    getRemoteMovieById(movieId)
                    getImage(movieId)
                }
            }
        }
    }

    private fun getRemoteMovieById(id: Int) {
        viewModelScope.launch(coroutineDispatcher) {
            getRemoteMovieById.invoke(id).fold(
                onSuccess = {
                    movieDetail = it
                },
                onFailure = {
                    println(it.message)
                },
            )
        }
    }

    private fun getImage(id: Int) {
        viewModelScope.launch(coroutineDispatcher) {
            getMovieImageById.invoke(id).fold(
                onSuccess = {
                    movieImage = it
                },
                onFailure = {
                    println(it.message)
                },
            )
        }
    }

    private fun updateMovie(movie: Movie) {
        viewModelScope.launch(coroutineDispatcher) {
            updateMovie.invoke(movie.copy(isFavorite = !movie.isFavorite))
        }
    }
}
