package com.panassevich.panassevich.feature.loans.settings.presentation

import com.panassevich.panassevich.component.loans.commonclasses.testutils.InstantTaskExecutorExtension
import com.panassevich.panassevich.component.loans.commonclasses.testutils.TestCoroutineExtension
import com.panassevich.panassevich.feature.loans.settings.SettingsScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.usecase.LogOutUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExtendWith(
    InstantTaskExecutorExtension::class,
    TestCoroutineExtension::class,
    MockitoExtension::class
)
class SettingsViewModelTest {

    private val logOutUseCase: LogOutUseCase = mock()
    private val router: SettingsScreenRouter = mock()

    private val viewModel: SettingsViewModel = SettingsViewModel(logOutUseCase, router)

    @Test
    fun `invoke logOut() EXPECT openLogInScreen() called`() = runTest {

        whenever(logOutUseCase()) doReturn true

        viewModel.logOut()

        verify(router).openLogInScreen()
    }

}