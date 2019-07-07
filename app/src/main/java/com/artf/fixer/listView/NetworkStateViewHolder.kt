package com.artf.fixer.listView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.artf.fixer.databinding.RowNetworkStateBinding
import com.artf.fixer.repository.NetworkState
import com.artf.fixer.repository.Status

class NetworkStateViewHolder constructor(val binding: RowNetworkStateBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: ListViewPagingAdapter.OnNetworkStateClickListener, networkState: NetworkState?) {
        binding.networkState = networkState
        binding.clickListener = clickListener

        binding.progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        binding.retryButton.visibility = toVisibility(networkState?.status == Status.FAILED)
        binding.errorMsg.visibility = toVisibility(networkState?.msg != null)
        binding.errorMsg.text = networkState?.msg

        binding.executePendingBindings()
    }

    companion object {
        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) View.VISIBLE else View.GONE
        }
    }
}
