package movies

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
import usecase.GetSearchMovie

class SearchViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getSearchMovie: GetSearchMovie,
) : ViewModel() {
    private val searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Init)
    val uiState: StateFlow<SearchUiState>
        get() = searchUiState

    var movies by mutableStateOf(listOf<Movie>())
        private set

    var query by mutableStateOf("")
        private set

    private fun getMoviesByQuery(query: String) {
        viewModelScope.launch(coroutineDispatcher) {
            getSearchMovie.invoke(query).fold(
                onSuccess = {
                    movies = it.results
                    searchUiState.value = SearchUiState.Success
                },
                onFailure = {
                    searchUiState.value = SearchUiState.Error
                },
            )
        }
    }

    fun handlerAction(action: SearchAction) {
        when (action) {
            is SearchAction.CleanStatus -> {
                searchUiState.value = SearchUiState.Init
            }

            is SearchAction.Init -> {
            }

            is SearchAction.QueryMovie -> {
                query = action.query
            }

            is SearchAction.OnBackPress -> {
                searchUiState.value = SearchUiState.OnBackPress
            }

            is SearchAction.OnSelectMovie -> {
                searchUiState.value = SearchUiState.OnDetailMovie(action.movie)
            }

            is SearchAction.Search -> {
                getMoviesByQuery(query)
            }
        }
    }
}