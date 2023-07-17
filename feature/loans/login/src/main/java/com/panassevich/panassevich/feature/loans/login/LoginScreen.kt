package com.panassevich.panassevich.feature.loans.login

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.login.ui.LoginFragment

fun getLoginScreen(): FragmentScreen =
    FragmentScreen { LoginFragment.newInstance() }