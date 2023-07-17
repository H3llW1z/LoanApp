package com.panassevich.panassevich.feature.loans.newloan.presentation

import androidx.lifecycle.Observer
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.testutils.InstantTaskExecutorExtension
import com.panassevich.panassevich.component.loans.commonclasses.testutils.TestCoroutineExtension
import com.panassevich.panassevich.feature.loans.newloan.NewLoanScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanConditions
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.CreateNewLoanUseCase
import com.panassevich.panassevich.shared.loans.core.domain.usecase.GetLoanConditionsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.stream.Stream

@ExtendWith(
    InstantTaskExecutorExtension::class, TestCoroutineExtension::class, MockitoExtension::class
)
class NewLoanViewModelTest {

    companion object {

        @JvmStatic
        fun conditionsUseCaseErrors(): Stream<Pair<RequestResult<LoanConditions>, ErrorEvent>> =
            Stream.of(
                RequestResult.Error(RequestError.NetworkError<LoanConditions>(null)) to ErrorEvent.NetworkError,
                RequestResult.Error(RequestError.ServerError<LoanConditions>(null)) to ErrorEvent.ServerError,
            )

        @JvmStatic
        fun getLoanRequestEmptyFields(): Stream<RequestInputData> = Stream.of(
            RequestInputData("", "", "", ""),
            RequestInputData("100", "test", "test", ""),
            RequestInputData("", "test", "test", "phone"),
            RequestInputData("100", "", "test", "phone"),
            RequestInputData("100", "test", "", "phone")
        )

        @JvmStatic
        fun createLoanUseCaseErrors(): Stream<Pair<RequestResult<out Loan>, ErrorEvent>> =
            Stream.of(
                RequestResult.Error(RequestError.ServerError<Loan>(null)) to ErrorEvent.ServerError,
                RequestResult.Error(RequestError.NetworkError<Loan>(null)) to ErrorEvent.NetworkError,
                RequestResult.Error(RequestError.InvalidInput) to ErrorEvent.MaxAmountExceeded
            )
    }

    private val conditionsUseCase: GetLoanConditionsUseCase = mock()
    private val createLoanUseCase: CreateNewLoanUseCase = mock()
    private val router: NewLoanScreenRouter = mock()

    private val viewModel = NewLoanViewModel(conditionsUseCase, createLoanUseCase, router)
    private val stateObserver: Observer<ScreenState<LoanConditions>> = mock()
    private val errorEventObserver: Observer<ErrorEvent> = mock()

    private val sampleConditions = LoanConditions(25000, 7.8, 45)
    private val sampleLoan =
        Loan(1, 1000, "date", "test", "test", 5.8, 45, "phone", LoanStatus.REGISTERED)

    @Test
    fun `viewModel created EXPECT Initial state`() = runTest {
        viewModel.state.observeForever(stateObserver)

        verify(stateObserver).onChanged(ScreenState.Initial)
    }

    @Test
    fun `invoke loadLoanConditions(), useCase returns success EXPECT Content state`() = runTest {
        viewModel.state.observeForever(stateObserver)

        whenever(conditionsUseCase()) doReturn RequestResult.Success(sampleConditions)

        viewModel.loadLoanConditions()

        verify(stateObserver).onChanged(ScreenState.Content(sampleConditions))
    }

    @ParameterizedTest
    @MethodSource("conditionsUseCaseErrors")
    fun `invoke loadLoanConditions(), useCase returns error EXPECT corresponding error event`(
        data: Pair<RequestResult<LoanConditions>, ErrorEvent>
    ) = runTest {
        viewModel.state.observeForever(stateObserver)
        viewModel.errorEvent.observeForever(errorEventObserver)

        whenever(conditionsUseCase()) doReturn data.first

        viewModel.loadLoanConditions()

        verify(stateObserver).onChanged(ScreenState.Error)
        verify(errorEventObserver).onChanged(data.second)
    }

    @ParameterizedTest
    @MethodSource("getLoanRequestEmptyFields")
    fun `invoke sendLoanCreateRequest with empty strings EXPECT EmptyFields error event`(data: RequestInputData) =
        runTest {
            viewModel.errorEvent.observeForever(errorEventObserver)
            whenever(conditionsUseCase()) doReturn RequestResult.Success(sampleConditions)

            viewModel.loadLoanConditions()
            viewModel.sendLoanCreateRequest(data.amount, data.name, data.lastName, data.phone)

            verify(errorEventObserver).onChanged(ErrorEvent.EmptyFields)
        }

    @Test
    fun `invoke sendLoanCreateRequest, loan created successfully EXPECT router call`() = runTest {
        whenever(createLoanUseCase(any())) doReturn RequestResult.Success(sampleLoan)
        whenever(conditionsUseCase()) doReturn RequestResult.Success(sampleConditions)
        viewModel.loadLoanConditions()
        viewModel.sendLoanCreateRequest("1000", "test", "test", "phone")

        verify(router).openLoanCreatedScreen(any())
    }

    @ParameterizedTest
    @MethodSource("createLoanUseCaseErrors")
    fun `invoke sendLoanCreateRequest, useCase returns error EXPECT corresponding Error event`(
        data: Pair<RequestResult<Loan>, ErrorEvent>
    ) = runTest {
        viewModel.errorEvent.observeForever(errorEventObserver)

        whenever(conditionsUseCase()) doReturn RequestResult.Success(sampleConditions)
        whenever(createLoanUseCase(any())) doReturn data.first

        viewModel.loadLoanConditions()
        viewModel.sendLoanCreateRequest("1000", "test", "test", "phone")

        verify(errorEventObserver).onChanged(data.second)
    }

}