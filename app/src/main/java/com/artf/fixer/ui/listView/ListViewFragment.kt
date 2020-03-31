package com.artf.fixer.ui.listView

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.artf.fixer.ui.DetailActivity
import com.artf.fixer.R
import com.artf.fixer.databinding.FragmentListViewBinding
import com.artf.fixer.utility.extension.getVm
import com.artf.fixer.data.repository.NetworkState
import com.artf.fixer.data.repository.Status
import com.artf.fixer.utility.Constants.Companion.INTENT_LIST_ITEM_ID
import com.artf.fixer.utility.Constants.Companion.RECYCLER_VIEW_STATE_ID
import com.artf.fixer.utility.Constants.Companion.SCROLL_DIRECTION_UP
import com.artf.fixer.utility.Constants.Companion.TRANSITION_TO_DETAIL
import com.artf.fixer.utility.convertToString

class ListViewFragment : Fragment() {

    private val listViewViewModel by lazy { getVm<ListViewViewModel>() }
    private lateinit var binding: FragmentListViewBinding

    private var adapterItemCount: Int = 0
    private var savedInstanceState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.savedInstanceState = savedInstanceState
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_view, container, false)

        binding.listViewViewModel = listViewViewModel
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = ListViewPagingAdapter(
            ListViewPagingAdapter.OnClickListener { product -> listViewViewModel.onRecyclerItemClick(product) },
            ListViewPagingAdapter.OnSizeListener { adapterItemCount > 0 },
            ListViewPagingAdapter.OnBindListener { listViewViewModel.onBindRate(it) },
            ListViewPagingAdapter.OnNetworkStateClickListener { listViewViewModel.retry() }
        )

        listViewViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it?.let { (view, listItem) ->
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(INTENT_LIST_ITEM_ID, convertToString(listItem))

                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this.activity!!,
                    Pair<View, String>(view, TRANSITION_TO_DETAIL)
                )

                activity!!.startActivity(intent, activityOptions.toBundle())
                listViewViewModel.onRecyclerItemClick(null)
            }
        })

        listViewViewModel.ratesList.observe(viewLifecycleOwner, Observer {
            it?.let { listItem ->
                adapterItemCount = listItem.size
            }
        })

        listViewViewModel.date.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterItemCount = 0
            }
        })

        listViewViewModel.networkState.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                bindNetworkState(properties)
            }
        })

        initSwipeToRefresh()
        return binding.root
    }

    private fun initSwipeToRefresh() {
        listViewViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                binding.swipeRefresh.isRefreshing = properties == NetworkState.LOADING
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            listViewViewModel.refresh()
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(SCROLL_DIRECTION_UP)) {
                    binding.headerView.elevation = 8f
                    binding.divider.visibility = View.GONE
                } else {
                    binding.headerView.elevation = 0f
                    binding.divider.visibility = View.VISIBLE
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun bindNetworkState(networkState: NetworkState) {
        when (networkState.status) {
            Status.RUNNING -> {
                binding.emptyView.visibility = View.GONE
            }
            Status.FAILED -> {
                if (1 > adapterItemCount) {
                    binding.emptyView.visibility = View.VISIBLE
                    if (checkConnection()) {
                        binding.emptyTitleText.text = getString(R.string.server_problem)
                        binding.emptySubtitleText.text = getString(R.string.server_problem_sub_text)
                    } else {
                        binding.emptyTitleText.text = getString(R.string.no_connection)
                        binding.emptySubtitleText.text = getString(R.string.no_connection_sub_text)
                    }
                }
            }
            Status.SUCCESS -> {
                binding.emptyView.visibility = View.GONE
            }
        }
    }

    private fun checkConnection(): Boolean {
        val cm = requireNotNull(this.activity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.isDefaultNetworkActive
    }

    override fun onResume() {
        super.onResume()
        if (savedInstanceState != null) {
            val listState: Parcelable? = savedInstanceState!!.getParcelable(RECYCLER_VIEW_STATE_ID)
            binding.recyclerView.layoutManager?.onRestoreInstanceState(listState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val listState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE_ID, listState)
        super.onSaveInstanceState(outState)
    }
}