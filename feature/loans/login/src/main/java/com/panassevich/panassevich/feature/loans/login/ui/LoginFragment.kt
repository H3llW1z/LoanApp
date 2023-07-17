package com.panassevich.panassevich.feature.loans.login.ui

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.ui.BaseFragment
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.login.databinding.FragmentLoginBinding
import com.panassevich.panassevich.feature.loans.login.presentation.LoginViewModel
import com.panassevich.panassevich.util.loans.ui.enableTogglePasswordVisibility
import com.panassevich.panassevich.util.loans.ui.shakeAnimator
import com.panassevich.panassevich.util.loans.ui.showSnackbar

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    R.string.loans_title, LoginViewModel::class.java, FragmentLoginBinding::inflate
) {

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareButtons()
        prepareTogglePasswordVisibility()
        observeState()
        observeErrorEvents()
    }

    private fun prepareButtons() {
        with(binding) {
            buttonLogin.setOnClickListener {
                viewModel.logIn(
                    editTextName.text.toString(),
                    editTextPassword.text.toString()
                )
            }
            buttonNotRegistered.setOnClickListener {
                viewModel.openRegistrationScreen()
            }
        }
    }

    private fun prepareTogglePasswordVisibility() {
        enableTogglePasswordVisibility(
            binding.editTextPassword,
            binding.buttonTogglePasswordVisibility,
            R.drawable.ic_eye_off,
            R.drawable.ic_eye_on
        )
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Initial -> {}

                ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.constraintLayoutContent.visibility = View.GONE
                }

                is ScreenState.Success -> {
                    view?.showSnackbar(getString(R.string.log_in_successfully))
                    viewModel.openTutorialScreen()
                }

                is ScreenState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.constraintLayoutContent.visibility = View.VISIBLE
                }

                is ScreenState.Content -> {}  //no content on this screen
            }
        }
    }

    private fun observeErrorEvents() {
        viewModel.errorEvent.observe(viewLifecycleOwner) { event ->
            val message = when (event) {
                is ErrorEvent.EmptyFields -> {
                    shakeInputFields()
                    getString(R.string.fill_all_fields)
                }

                is ErrorEvent.NetworkError -> getString(R.string.network_error_occurred)
                is ErrorEvent.ServerError -> getString(R.string.server_error_occurred)
                is ErrorEvent.UserNotFound -> getString(R.string.wrong_name_or_password)
                else -> throw RuntimeException("Unexpected event: $event")
            }
            view?.showSnackbar(message)
        }
    }

    private fun shakeInputFields() {
        val animatorSet = AnimatorSet()
        val nameFieldAnimator = shakeAnimator(binding.editTextName)
        val passwordFieldAnimator =
            shakeAnimator(binding.editTextPassword)
        val buttonToggleAnimator =
            shakeAnimator(binding.buttonTogglePasswordVisibility)
        animatorSet.playTogether(nameFieldAnimator, passwordFieldAnimator, buttonToggleAnimator)
        animatorSet.start()
    }
}