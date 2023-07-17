package com.panassevich.panassevich.feature.loans.login.presentation

import androidx.lifecycle.Observer
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.testutils.InstantTaskExecutorExtension
import com.panassevich.panassevich.component.loans.commonclasses.testutils.TestCoroutineExtension
import com.panassevich.panassevich.feature.loans.login.LoginScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.LogInUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import java.util.stream.Stream

@ExtendWith(
    InstantTaskExecutorExtension::class,
    TestCoroutineExtension::class,
    MockitoExtension::class
)
class LoginViewModelTest {

    private val logInUseCase: LogInUseCase = mock()
    private val router: LoginScreenRouter = mock()
    private val viewModel = LoginViewModel(logInUseCase, router)

    private val stateObserver: Observer<ScreenState<Unit>> = mock()
    private val errorEventObserver: Observer<ErrorEvent> = mock()

    companion object {
        @JvmStatic
        fun emptyFieldsAuthInfo(): Stream<Pair<String, String>> = Stream.of(
            "" to "",
            "name" to "",
            "password" to ""
        )

        @JvmStatic
        fun useCaseErrors(): Stream<Pair<RequestResult<Unit>, ErrorEvent>> = Stream.of(
            RequestResult.Error(RequestError.ServerError(Unit)) to ErrorEvent.ServerError,
            RequestResult.Error(RequestError.NetworkError(Unit)) to ErrorEvent.NetworkError,
            RequestResult.Error(RequestError.UserNotFound) to ErrorEvent.UserNotFound
        )
    }

    @Test
    fun `viewModel created EXPECTED initial state`() = runTest {
        viewModel.state.observeForever(stateObserver)
        verify(stateObserver).onChanged(ScreenState.Initial)
    }

    @ParameterizedTest
    @MethodSource("emptyFieldsAuthInfo")
    fun `invoke logIn with empty strings EXPECT ErrorEvent EmptyFields`(authInfo: Pair<String, String>) =
        runTest {
            viewModel.errorEvent.observeForever(errorEventObserver)
            viewModel.state.observeForever(stateObserver)

            viewModel.logIn(authInfo.first, authInfo.second)

            verify(errorEventObserver).onChanged(ErrorEvent.EmptyFields)
            verify(stateObserver).onChanged(ScreenState.Content(Unit))
        }

    @Test
    fun `invoke logIn(), logInUseCase returns success EXPECT ScreenState Success`() = runTest {
        viewModel.state.observeForever(stateObserver)

        whenever(logInUseCase(AuthInfo("test", "test"))) doReturn RequestResult.Success(Unit)

        viewModel.logIn("test", "test")

        verify(stateObserver).onChanged(ScreenState.Success)
    }


    @ParameterizedTest
    @MethodSource("useCaseErrors")
    fun `invoke logIn(), LogInUseCase returns error EXPECT correct ErrorEvent`(
        data: Pair<RequestResult<Unit>,
                ErrorEvent>
    ) = runTest {
        viewModel.errorEvent.observeForever(errorEventObserver)
        viewModel.state.observeForever(stateObserver)

        whenever(logInUseCase(AuthInfo("test", "test"))) doReturn data.first

        viewModel.logIn("test", "test")

        verify(errorEventObserver).onChanged(data.second)
        verify(stateObserver).onChanged(ScreenState.Error)
    }
}