package com.example.testapp.bean

import com.example.testapp.activity.SensorActivity
import com.example.testapp.chart.LinerChartFragment
import com.example.testapp.fragment.ProgressBarFragment
import com.example.testapp.fragment.SystemInfoFragment
import com.example.testapp.navigation.BottomNavigationActivity
import com.example.testapp.navigation.NavigationActivity
import com.example.testapp.navigation.NavigationRailActivity

enum class PagerBean(
    val pager_name: String,
    val target: () -> Any?,
    val desc: String = ""
) {
    ProgressBar("ProgressBar", { ProgressBarFragment() }, "ProgressBar"),
    SystemInfo("SystemInfo", { SystemInfoFragment() }, "SystemInfo"),
    GSensor("G-Sensor", { SensorActivity::class.java }, "G-Sensor"),
    NavigationRail(
        "NavigationRail",
        { NavigationRailActivity::class.java },
        "a navigation rail demo"
    ),
    Navigation(
        "NavigationActivity",
        { NavigationActivity::class.java },
        "a navigation activity demo"
    ),
    BottomNavigation(
        "BottomNavigationActivity",
        { BottomNavigationActivity::class.java },
        "a bottom navigation activity demo"
    ),
    LinerChart(
        "LinerChart",
        { LinerChartFragment.newInstance() },
        "LinerChart"
    ),
}