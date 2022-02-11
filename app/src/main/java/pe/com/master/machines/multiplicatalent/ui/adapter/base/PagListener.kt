package pe.com.master.machines.multiplicatalent.ui.adapter.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PagListener(private val layoutManager: LinearLayoutManager, private val size: Int) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= 0 &&
                totalItemCount >= size
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()

    abstract val isLastPage: Boolean

    abstract val isLoading: Boolean

    companion object {
        const val PAGE_START = 1
    }
}