package com.panassevich.panassevich.feature.loans.loanslist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.ui.BaseFragment
import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.loanslist.databinding.FragmentLoansListBinding
import com.panassevich.panassevich.feature.loans.loanslist.presentation.LoansListViewModel
import com.panassevich.panassevich.feature.loans.loanslist.ui.adapter.LoansListAdapter
import com.panassevich.panassevich.util.loans.ui.showSnackbar
import com.panassevich.panassevich.feature.loans.loanslist.R as LocalR


class LoansListFragment : BaseFragment<FragmentLoansListBinding, LoansListViewModel>(
    R.string.your_loans_title, LoansListViewModel::class.java, FragmentLoansListBinding::inflate
) {
    companion object {
        @JvmStatic
        fun newInstance() = LoansListFragment()
    }

    private val loansListAdapter = LoansListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        setUpButtons()
        setUpSwipeRefresh()
        setUpMenu()
        observeState()
        observeErrorEvents()

    }

    private fun setUpRecyclerView() {
        loansListAdapter.onItemClick = { loan ->
            viewModel.openLoanDetailsScreen(loan)
        }
        binding.recyclerViewLoans.adapter = loansListAdapter
    }

    private fun setUpButtons() {
        binding.buttonCreateLoan.setOnClickListener {
            viewModel.openCreateLoanScreen()
        }
        binding.buttonTryAgain.setOnClickListener {
            viewModel.loadAllLoans()
        }
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadAllLoans()
        }
    }

    private fun setUpMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(LocalR.menu.settings_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    LocalR.id.settings -> {
                        viewModel.openSettings()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Initial -> viewModel.loadAllLoans()

                is ScreenState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is ScreenState.Content -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    setErrorViewsVisibility(View.GONE)
                    val loans = state.content
                    if (loans.isNotEmpty()) {
                        binding.textViewNoLoansLabel.visibility = View.GONE
                        loansListAdapter.submitList(loans)
                    } else {
                        binding.textViewNoLoansLabel.visibility = View.VISIBLE
                    }
                }

                is ScreenState.Error -> {
                    with(binding) {
                        swipeRefreshLayout.isRefreshing = false
                        textViewNoLoansLabel.visibility = View.GONE
                        setErrorViewsVisibility(View.VISIBLE)
                    }
                }

                is ScreenState.Success -> {}
            }
        }
    }

    private fun observeErrorEvents() {
        viewModel.errorEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ErrorEvent.ServerError -> view?.showSnackbar(getString(R.string.server_error_occurred))
                is ErrorEvent.NetworkError -> view?.showSnackbar(getString(R.string.network_error_occurred))
                else -> throw RuntimeException("Unexpected event during getting all loans: $event")
            }
        }
    }

    private fun setErrorViewsVisibility(visibility: Int) {
        with(binding) {
            imageViewError.visibility = visibility
            textViewError.visibility = visibility
            buttonTryAgain.visibility = visibility
        }
    }
}