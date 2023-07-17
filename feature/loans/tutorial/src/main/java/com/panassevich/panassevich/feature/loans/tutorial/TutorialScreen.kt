package com.panassevich.panassevich.feature.loans.tutorial

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.tutorial.ui.TutorialFragment

fun getTutorialScreen(): FragmentScreen =
    FragmentScreen { TutorialFragment.newInstance() }