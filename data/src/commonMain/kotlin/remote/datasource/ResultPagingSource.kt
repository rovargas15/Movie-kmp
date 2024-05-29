package remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState

open class ResultPagingSource<T : Any>(
    private val pagingData: suspend (page: Int, pageSize: Int) -> Result<List<T>>,
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
        (params.key ?: 1).let { indexPage ->
            pagingData(indexPage, params.loadSize).fold(
                onSuccess = { value ->
                    LoadResult.Page(
                        data = value,
                        // no previous pagination int as page
                        prevKey = indexPage.takeIf { it > 1 }?.dec(),
                        // no pagination if no results found else next page as +1
                        nextKey = indexPage.takeIf { value.size >= params.loadSize }?.inc(),
                    )
                },
                onFailure = { error ->
                    LoadResult.Error(error)
                },
            )
        }
}
