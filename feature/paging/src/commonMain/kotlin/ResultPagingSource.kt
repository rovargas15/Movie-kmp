import androidx.paging.PagingSource
import androidx.paging.PagingState

open class ResultPagingSource<T : Any>(
    private val pagingData: suspend (page: Int, pageSize: Int) -> Result<List<T>>,
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
        (params.key ?: 1).let { indexPage ->
            pagingData(indexPage, params.loadSize).fold(
                onSuccess = { value ->
                    LoadResult.Page(
                        data = value.distinct(),
                        prevKey = if (indexPage == 1) null else indexPage - 1,
                        nextKey = (indexPage + 1).takeIf { value.lastIndex >= indexPage },
                    )
                },
                onFailure = { error ->
                    LoadResult.Error(error)
                },
            )
        }
}
