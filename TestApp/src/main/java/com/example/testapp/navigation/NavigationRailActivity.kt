package com.example.testapp.navigation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testapp.R
import com.example.testapp.databinding.ActivityNavigationRailBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NavigationRailViewModel : ViewModel() {
    private var _homeBadgeNumber = 0
    private val _homeBadgeSize: MutableStateFlow<Int> = MutableStateFlow(_homeBadgeNumber)
    private var j: Job? = null

    val homeBadgeSize: StateFlow<Int> = _homeBadgeSize

    fun startIncreasingHomeBadge() {
        j = viewModelScope.launch {
            while (isActive) {
                delay(5_000L)
                _homeBadgeSize.emit(++_homeBadgeNumber)
            }
        }
    }

    fun resetHomeBadge() {
        viewModelScope.launch {
            _homeBadgeNumber = 0
            _homeBadgeSize.emit(0)
        }
    }

    fun stopIncreasingHomeBadge() {
        j?.cancel()
        j = null
    }
}

class NavigationRailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationRailBinding

    private val model by viewModels<NavigationRailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationRailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.init()
        model.startIncreasingHomeBadge()
    }

    private fun ActivityNavigationRailBinding.init() {
        val navRail = navRail
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        navRail.setupWithNavController(navController)
        val homeBadge = navRail.getOrCreateBadge(R.id.navigation_home)
        homeBadge.maxCharacterCount = 3
        model.homeBadgeSize.flowWithLifecycle(
            lifecycle,
            Lifecycle.State.RESUMED
        ).onEach {
            homeBadge.isVisible = it > 0
            homeBadge.number = it
        }.launchIn(lifecycleScope)

        navRail.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    //homeBadge.clearNumber()
                    model.resetHomeBadge()
                }
                else -> {

                }
            }
            true
        }
    }

    override fun onDestroy() {
        model.stopIncreasingHomeBadge()
        super.onDestroy()
    }
}