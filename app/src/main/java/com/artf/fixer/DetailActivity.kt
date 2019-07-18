package com.artf.fixer

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.artf.fixer.databinding.ActivityDetailBinding
import com.artf.fixer.detailView.DetailViewViewModel
import com.artf.fixer.extension.getVm
import com.artf.fixer.utility.Constants.Companion.INTENT_LIST_ITEM_ID
import com.artf.fixer.utility.Constants.Companion.TRANSITION_TO_DETAIL
import com.artf.fixer.utility.convertFromString
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail_view.*

class DetailActivity : AppCompatActivity() {

    private val detailViewViewModel by lazy { getVm<DetailViewViewModel>() }
    private lateinit var binding: ActivityDetailBinding

    private var savedInstanceState: Bundle? = null
    private var isBackPressAvailable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewViewModel.setRateItem(convertFromString(intent.getStringExtra(INTENT_LIST_ITEM_ID)!!))
        binding.detailContainer.transitionName = TRANSITION_TO_DETAIL

        binding.root.post {
            if (savedInstanceState != null) {
                binding.toolbar.visibility = View.VISIBLE
                scrollview.visibility = View.VISIBLE
            } else {
                onViewCreated()
            }
        }
    }

    private fun onViewCreated() {
        val toolBarSize = binding.toolbar.height
        val frontLayoutSize = binding.frontLayout.height

        val objectAnimator1 =
            setObjectTranslation(binding.toolbar, "translationY", -toolBarSize.toFloat(), 0f, 800, 600)
        objectAnimator1.doOnStart {
            binding.toolbar.translationY = -toolBarSize.toFloat()
            binding.toolbar.visibility = View.VISIBLE
        }

        val objectAnimator2 =
            setObjectTranslation(binding.frontLayout, "translationY", frontLayoutSize.toFloat(), 0f, 800, 600)
        objectAnimator2.doOnStart {
            binding.frontLayout.translationY = frontLayoutSize.toFloat()
            scrollview.visibility = View.VISIBLE
        }
        objectAnimator1.start()
        objectAnimator2.start()
    }

    private fun setObjectTranslation(
        target: Any,
        propertyName: String,
        fromValue: Float,
        toValue: Float,
        duration: Long,
        startDelay: Long
    ): ObjectAnimator {
        val objectAnimator = ObjectAnimator()
        objectAnimator.target = target
        objectAnimator.interpolator = LinearOutSlowInInterpolator()
        objectAnimator.setPropertyName(propertyName)
        objectAnimator.setFloatValues(fromValue, toValue)
        objectAnimator.duration = duration
        objectAnimator.startDelay = startDelay
        return objectAnimator
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

    override fun onBackPressed() {
        if (isBackPressAvailable) {
            isBackPressAvailable = false
            val toolBarSize = toolbar.height
            val frontLayoutSize = frontLayout.height

            val objectAnimator1 =
                setObjectTranslation(binding.toolbar, "translationY", 0f, -toolBarSize.toFloat(), 600, 0)
            val objectAnimator2 =
                setObjectTranslation(binding.frontLayout, "translationY", 0f, frontLayoutSize.toFloat(), 600, 0)

            objectAnimator2.doOnEnd {
                super.onBackPressed()
            }

            objectAnimator1.start()
            objectAnimator2.start()
        }
    }
}