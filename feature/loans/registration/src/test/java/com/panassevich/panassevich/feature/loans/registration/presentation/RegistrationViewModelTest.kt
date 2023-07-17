package com.panassevich.panassevich.feature.loans.registration.presentation

import androidx.lifecycle.Observer
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.testutils.InstantTaskExecutorExtension
import com.panassevich.panassevich.component.loans.commonclasses.testutils.TestCoroutineExtension
import com.panassevich.panassevich.feature.loans.registration.RegistrationScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.RegisterUseCase
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
class RegistrationViewModelTest {

    private val useCase: RegisterUseCase = mock()
    private val router: RegistrationScreenRouter = mock()
    private val viewModel = RegistrationViewModel(useCase, router)

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
            RequestResult.Error(RequestError.UserAlreadyExists) to ErrorEvent.UserAlreadyExists
        )
    }

    @Test
    fun `viewModel created EXPECTED initial state`() = runTest {
        viewModel.state.observeForever(stateObserver)
        verify(stateObserver).onChanged(ScreenState.Initial)
    }

    @ParameterizedTest
    @MethodSource("emptyFieldsAuthInfo")
    fun `invoke register() with empty strings EXPECT ErrorEvent EmptyFields`(authInfo: Pair<String, String>) =
        runTest {
            viewModel.errorEvent.observeForever(errorEventObserver)
            viewModel.state.observeForever(stateObserver)

            viewModel.register(authInfo.first, authInfo.second)

            verify(errorEventObserver).onChanged(ErrorEvent.EmptyFields)
            verify(stateObserver).onChanged(ScreenState.Content(Unit))

        }

    @Test
    fun `invoke register(), useCase returns success EXPECT ScreenState Success`() = runTest {
        viewModel.state.observeForever(stateObserver)

        whenever(useCase(AuthInfo("test", "test"))) doReturn RequestResult.Success(Unit)

        viewModel.register("test", "test")

        verify(stateObserver).onChanged(ScreenState.Success)
    }


    @ParameterizedTest
    @MethodSource("useCaseErrors")
    fun `invoke register(), useCase returns error EXPECT correct ErrorEvent`(
        data: Pair<RequestResult<Unit>,
                ErrorEvent>
    ) = runTest {
        viewModel.errorEvent.observeForever(errorEventObserver)
        viewModel.state.observeForever(stateObserver)

        whenever(useCase(AuthInfo("test", "test"))) doReturn data.first

        viewModel.register("test", "test")

        verify(errorEventObserver).onChanged(data.second)
        verify(stateObserver).onChanged(ScreenState.Error)
    }
}