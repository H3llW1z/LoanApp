package com.panassevich.panassevich.feature.loans.settings

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.settings.ui.SettingsFragment

fun getSettingsScreen() =
    FragmentScreen { SettingsFragment.newInstance() }