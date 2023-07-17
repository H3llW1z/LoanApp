package com.panassevich.panassevich.feature.loans.registration

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.registration.ui.RegistrationFragment

fun getRegistrationScreen(): FragmentScreen =
    FragmentScreen { RegistrationFragment.newInstance() }