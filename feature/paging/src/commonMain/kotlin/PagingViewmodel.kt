import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import model.Movie
import usecase.GetPagingMovieByCategory

class PagingViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val useCase: GetPagingMovieByCategory,
) : ViewModel(), DefaultLifecycleObserver {
    private val movieUiState = MutableStateFlow<PagingUiState>(PagingUiState.Init)
    val uiState: StateFlow<PagingUiState>
        get() = movieUiState

    var flow: Flow<PagingData<Movie>> = flowOf()
        private set

    fun onLoad(category: String) {
        flow =
            Pager(
                config = PagingConfig(pageSize = 1),
                pagingSourceFactory = {
                    ResultPagingSource { page: Int, _: Int ->
                        useCase.invoke(
                            page = page,
                            category = category,
                        ).map {
                            it.results
                        }
                    }
                },
            ).flow.flowOn(coroutineDispatcher)
    }

    fun handleAction(action: PagingAction) {
        when (action) {
            PagingAction.Init -> {
                movieUiState.value = PagingUiState.Init
            }

            is PagingAction.OnSelectMovie -> {
                movieUiState.value = PagingUiState.OnShowDetail(action.movie)
            }

            PagingAction.OnBackPress -> {
                movieUiState.value = PagingUiState.OnBackPress
            }
        }
    }
}
