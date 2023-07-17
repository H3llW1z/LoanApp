package com.panassevich.panassevich.feature.loans.loanslist.presentation

import androidx.lifecycle.Observer
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.testutils.InstantTaskExecutorExtension
import com.panassevich.panassevich.component.loans.commonclasses.testutils.TestCoroutineExtension
import com.panassevich.panassevich.feature.loans.loanslist.LoansListScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.GetAllLoansUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.stream.Stream

@ExtendWith(
    InstantTaskExecutorExtension::class,
    TestCoroutineExtension::class,
    MockitoExtension::class
)
class LoansListViewModelTest {

    private val getAllLoansUseCase: GetAllLoansUseCase = mock()
    private val router: LoansListScreenRouter = mock()

    private val stateObserver: Observer<ScreenState<List<Loan>>> = mock()
    private val errorEventObserver: Observer<ErrorEvent> = mock()

    private val viewModel = LoansListViewModel(getAllLoansUseCase, router)

    companion object {
        private val sampleLoan =
            Loan(1, 100, "01.01.2002", "name", "lastName", 5.8, 45, "phone", LoanStatus.APPROVED)

        @JvmStatic
        fun useCaseErrors(): Stream<Pair<RequestResult<List<Loan>>, ErrorEvent>> = Stream.of(
            RequestResult.Error(RequestError.ServerError(listOf(sampleLoan))) to ErrorEvent.ServerError,
            RequestResult.Error(RequestError.NetworkError(listOf(sampleLoan))) to ErrorEvent.NetworkError
        )
    }

    @Test
    fun `create viewModel EXPECTED Initial State`() = runTest {
        viewModel.state.observeForever(stateObserver)

        verify(stateObserver).onChanged(ScreenState.Initial)
    }

    @Test
    fun `invoke getAllLoans() EXPECT Loading State`() = runTest {
        viewModel.state.observeForever(stateObserver)
        whenever(getAllLoansUseCase()) doReturn RequestResult.Success(listOf(sampleLoan))
        viewModel.loadAllLoans()
        verify(stateObserver).onChanged(ScreenState.Loading)
    }

    @Test
    fun `invoke getAllLoans(), useCase returns success EXPECT Content State`() = runTest {
        viewModel.state.observeForever(stateObserver)

        whenever(getAllLoansUseCase()) doReturn RequestResult.Success(listOf(sampleLoan))

        viewModel.loadAllLoans()

        verify(stateObserver).onChanged(ScreenState.Content(listOf(sampleLoan)))
    }

    @ParameterizedTest
    @MethodSource("useCaseErrors")
    fun `invoke getAllLoans(), useCase returns error EXPECT correct ErrorEvent`(
        data: Pair<RequestResult<List<Loan>>,
                ErrorEvent>
    ) = runTest {
        viewModel.state.observeForever(stateObserver)
        viewModel.errorEvent.observeForever(errorEventObserver)

        whenever(getAllLoansUseCase()) doReturn data.first

        viewModel.loadAllLoans()

        verify(errorEventObserver).onChanged(data.second)
    }
}