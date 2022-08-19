package com.example.testapp.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.testapp.R
import com.example.testapp.databinding.ActivityNavigationBinding


class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        val navOptions: NavOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
//            .setPopUpTo(
//                navController.graph.startDestinationId,
//                inclusive = false,
//                saveState = true
//            ) // saveState
            .build()
        binding.apply {
            btHome.setOnClickListener {
                navController.navigate(R.id.navigation_home, null, navOptions)
            }
            btDash.setOnClickListener {
                navController.navigate(R.id.navigation_dashboard, null, navOptions)
            }
            btNotify.setOnClickListener {
                navController.navigate(R.id.navigation_notifications, null, navOptions)
            }
        }
    }
}