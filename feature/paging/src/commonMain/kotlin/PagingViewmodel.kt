import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import usecase.GetPagingMovieByCategory

class PagingViewmodel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val useCase: GetPagingMovieByCategory,
) : ViewModel(), DefaultLifecycleObserver {

    private val movieUiState = MutableStateFlow<PagingUiState>(PagingUiState.Init)
    val uiState: StateFlow<PagingUiState>
        get() = movieUiState

    fun onLoad(category: String) = Pager(
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
    ).flow

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
