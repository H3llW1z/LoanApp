package com.panassevich.panassevich.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.loancreated.ui.LoanCreatedFragment
import com.panassevich.panassevich.feature.loans.loandetails.ui.LoanDetailsFragment
import com.panassevich.panassevich.feature.loans.loanslist.ui.LoansListFragment
import com.panassevich.panassevich.feature.loans.login.ui.LoginFragment
import com.panassevich.panassevich.feature.loans.newloan.ui.NewLoanFragment
import com.panassevich.panassevich.feature.loans.registration.ui.RegistrationFragment
import com.panassevich.panassevich.feature.loans.settings.ui.SettingsFragment
import com.panassevich.panassevich.feature.loans.tutorial.ui.TutorialFragment

class LoanAppNavigator(activity: AppCompatActivity, containerId: Int) :
    AppNavigator(activity, containerId) {

    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        when {
            currentFragment is LoginFragment && nextFragment is RegistrationFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_bottom,
                    R.anim.slide_out_top,
                    R.anim.slide_in_top,
                    R.anim.slide_out_bottom
                )
            }

            currentFragment is LoginFragment && nextFragment is TutorialFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )  //cant go back from tutorial to login
            }

            currentFragment is TutorialFragment && nextFragment is LoansListFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ) //cant go back from loans list to tutorial
            }

            currentFragment is LoansListFragment && nextFragment is LoanDetailsFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_bottom,
                    R.anim.slide_out_top,
                    R.anim.slide_in_top,
                    R.anim.slide_out_bottom
                )
            }

            currentFragment is LoansListFragment && nextFragment is NewLoanFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }

            currentFragment is LoansListFragment && nextFragment is SettingsFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_top,
                    R.anim.slide_out_bottom,
                    R.anim.slide_in_bottom,
                    R.anim.slide_out_top
                )
            }

            currentFragment is NewLoanFragment && nextFragment is LoanCreatedFragment -> {
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }

            currentFragment is SettingsFragment && nextFragment is LoginFragment -> {
                fragmentTransaction.setCustomAnimations(
                    androidx.appcompat.R.anim.abc_fade_in,
                    androidx.appcompat.R.anim.abc_fade_out
                )
            }
        }
    }
}