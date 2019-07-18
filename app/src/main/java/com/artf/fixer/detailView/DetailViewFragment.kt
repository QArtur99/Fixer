package com.artf.fixer.detailView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.artf.fixer.R
import com.artf.fixer.databinding.FragmentDetailViewBinding
import com.artf.fixer.extension.getVm

class DetailViewFragment : Fragment() {

    private val detailViewViewModel by lazy { getVm<DetailViewViewModel>() }
    private lateinit var binding: FragmentDetailViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_view, container, false)

        binding.detailViewViewModel = detailViewViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}