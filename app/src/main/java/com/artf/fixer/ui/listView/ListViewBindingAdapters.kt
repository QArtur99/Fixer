package com.artf.fixer.ui.listView

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.artf.fixer.data.model.Rate
import com.artf.fixer.ui.listView.ListViewPagingAdapter
import com.artf.fixer.data.repository.NetworkState

@BindingAdapter("listData")
fun bindMoviesRecyclerView(recyclerView: RecyclerView, data: PagedList<Rate>?) {
    data?.let {
        val adapter = recyclerView.adapter as ListViewPagingAdapter
        adapter.submitList(data)
        adapter.notifyDataSetChanged()
    }
}

@BindingAdapter("networkState")
fun setNetworkState(recyclerView: RecyclerView, data: NetworkState?) {
    val adapter = recyclerView.adapter as ListViewPagingAdapter
    adapter.setNetworkState(data)
}