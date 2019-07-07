package com.artf.fixer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.artf.fixer.databinding.ActivityDetailBinding
import com.artf.fixer.detailView.DetailViewViewModel
import com.artf.fixer.detailView.DetailViewViewModelFactory
import com.artf.fixer.utility.Constants.Companion.INTENT_LIST_ITEM_ID
import com.artf.fixer.utility.convertFromString

class DetailActivity : AppCompatActivity() {

    private val detailViewViewModel: DetailViewViewModel by lazy {
        val viewModelFactory = DetailViewViewModelFactory()
        ViewModelProviders.of(this, viewModelFactory).get(DetailViewViewModel::class.java)
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewViewModel.setRateItem(convertFromString(intent.getStringExtra(INTENT_LIST_ITEM_ID)!!))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}