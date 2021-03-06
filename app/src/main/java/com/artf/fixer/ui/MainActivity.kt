package com.artf.fixer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.artf.fixer.R
import com.artf.fixer.databinding.ActivityMainBinding
import com.artf.fixer.utility.extension.getVm
import com.artf.fixer.ui.listView.ListViewViewModel
import com.artf.fixer.utility.Constants
import com.artf.fixer.utility.getDateFormat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val listViewViewModel by lazy { getVm<ListViewViewModel>() }
    private lateinit var binding: ActivityMainBinding

    private val motionLayout by lazy { motionContainer }
    private val calendar by lazy { Calendar.getInstance() }
    private var isMenuExpended: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        restorePreviousState(savedInstanceState)

        binding.calendarView.maxDate = Date().time
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            calendar.set(year, month, day)
            val newDateString = getDateFormat().format(calendar.time)
            listViewViewModel.setSelectedDate(newDateString)

            setTransitionOnMotionLayout(
                R.id.s1,
                R.id.s2
            )
        }

        listViewViewModel.date.observe(this, Observer {
            it?.let { dateString ->
                getDateFormat().parse(dateString)?.let { date -> binding.calendarView.date = date.time }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                if (isMenuExpended) setTransitionOnMotionLayout(
                    R.id.s1,
                    R.id.s2
                )
                else setTransitionOnMotionLayout(
                    R.id.s2,
                    R.id.s1
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTransitionOnMotionLayout(outAnim: Int, inAnim: Int) {
        motionLayout.setTransition(outAnim, inAnim)
        motionLayout.transitionToEnd()
        isMenuExpended = !isMenuExpended
    }

    private fun restorePreviousState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            isMenuExpended = savedInstanceState.getBoolean(Constants.BACKDROP_STATE_ID)
            if (isMenuExpended) {
                motionLayout.setTransition(
                    R.id.s1,
                    R.id.s2
                )
                motionLayout.transitionToStart()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(Constants.BACKDROP_STATE_ID, isMenuExpended)
        super.onSaveInstanceState(outState)
    }
}
