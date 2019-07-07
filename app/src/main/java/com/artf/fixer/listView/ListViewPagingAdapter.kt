package com.artf.fixer.listView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.artf.fixer.R
import com.artf.fixer.databinding.RowCurrencyBinding
import com.artf.fixer.databinding.RowNetworkStateBinding
import com.artf.fixer.domain.Rate
import com.artf.fixer.repository.NetworkState
import com.artf.fixer.utility.ResultRate

class ListViewPagingAdapter(
    private val clickListener: OnClickListener,
    private val sizeListener: OnSizeListener,
    private val onBindListener: OnBindListener,
    private val networkStateClickListener: OnNetworkStateClickListener
) : PagedListAdapter<Rate, RecyclerView.ViewHolder>(ListViewDiffCallback) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.row_currency ->
                (holder as RateViewHolder).bind(clickListener, onBindListener, getItem(position)!!)
            R.layout.row_network_state ->
                (holder as NetworkStateViewHolder).bind(networkStateClickListener, networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.row_currency ->
                RateViewHolder(RowCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.row_network_state ->
                NetworkStateViewHolder(RowNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() =
        networkState != null && networkState != NetworkState.LOADED && sizeListener.onClick()

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) R.layout.row_network_state else R.layout.row_currency
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class RateViewHolder constructor(val binding: RowCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, bindListener: OnBindListener, item: Rate) {
            bindListener.onBind(item)
            binding.rate = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object ListViewDiffCallback : DiffUtil.ItemCallback<Rate>() {
        override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (resultRate: ResultRate) -> Unit) {
        fun onClick(v: View, product: Rate) = clickListener(ResultRate(v, product))
    }

    class OnBindListener(val bingListener: (product: Rate) -> Unit) {
        fun onBind(product: Rate) = bingListener(product)
    }

    class OnSizeListener(val clickListener: () -> Boolean) {
        fun onClick(): Boolean = clickListener()
    }

    class OnNetworkStateClickListener(val clickListener: () -> Unit) {
        fun onClick() = clickListener()
    }
}