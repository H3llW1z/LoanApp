package com.panassevich.panassevich.shared.loans.core.data

import android.content.SharedPreferences
import com.panassevich.panassevich.component.loans.commonclasses.testutils.InstantTaskExecutorExtension
import com.panassevich.panassevich.component.loans.commonclasses.testutils.TestCoroutineExtension
import com.panassevich.panassevich.shared.loans.core.data.api.LoanApiService
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanConditionsDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.UserEntityDto
import com.panassevich.panassevich.shared.loans.core.data.db.LoansDao
import com.panassevich.panassevich.shared.loans.core.data.db.model.LoanDbModel
import com.panassevich.panassevich.shared.loans.core.data.implementation.LoanRepositoryImpl
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanRequest
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.repository.LoanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.util.stream.Stream

@ExtendWith(
    InstantTaskExecutorExtension::class, TestCoroutineExtension::class, MockitoExtension::class
)
class LoanRepositoryImplTest {


    private val loanApiService: LoanApiService = mock()
    private val loansDao: LoansDao = mock()
    private lateinit var repository: LoanRepository

    companion object {
        @JvmStatic
        private fun emptyAuthInfo(): Stream<AuthInfo> = Stream.of(
            AuthInfo("", "test"), AuthInfo("test", ""), AuthInfo("", "")
        )

        private val sharedPreferences: SharedPreferences = mock()
        private val editor: SharedPreferences.Editor = mock()

        @BeforeAll
        @JvmStatic
        fun setUpSharedPreferences() {
            whenever(sharedPreferences.getString(any(), anyOrNull())) doReturn "token"
            whenever(sharedPreferences.edit()) doReturn editor
            whenever(
                editor.putString(
                    any(), any()
                )
            ) doReturn editor
            whenever(editor.remove(any())) doReturn editor
            whenever(editor.commit()) doReturn true
            whenever(sharedPreferences.contains(any())) doReturn true
        }
    }

    private fun getSampleLoanDto() = LoanDto(
        1, 2500, "date", "name", "lastname", 5.5, 45, "phone", "APPROVED"
    )

    private fun getSampleLoan() = Loan(
        1, 2500, "date", "name", "lastname", 5.5, 45, "phone", LoanStatus.APPROVED
    )

    private fun getSampleLoanDbModel() = LoanDbModel(
        1, 2500, "date", "name", "lastname", 5.5, 45, "phone", "APPROVED"
    )

    private fun getSampleAuthInfo() = AuthInfo("test", "test")

    private fun getSampleLoanConditionsDto() = LoanConditionsDto(25000, 5.8, 45)

    private fun getSampleLoanRequest() =
        LoanRequest(5000, "name", "lastname", 5.8, 45, "+11111111111")

    private fun <T> getErrorResponse(code: Int) = Response.error<T>(
        code, ResponseBody.create(
            MediaType.parse("application/json"), ""
        )
    )


    @BeforeEach
    fun setUpRepository() {
        repository = LoanRepositoryImpl(
            loanApiService, loansDao, sharedPreferences, Dispatchers.Main
        )
    }


    @Test
    fun `logIn returns success EXPECT Success`() = runTest {

        whenever(loanApiService.logIn(any())) doReturn Response.success("token")

        val expected = RequestResult.Success(Unit)

        val actual = repository.logIn(getSampleAuthInfo())

        Assertions.assertEquals(expected, actual)

    }

    @Test
    fun `logIn returns code 404 EXPECT UserNotFound`() = runTest {

        whenever(loanApiService.logIn(any())) doReturn getErrorResponse(404)

        val expected = RequestResult.Error(RequestError.UserNotFound)

        val actual = repository.logIn(getSampleAuthInfo())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `logIn returns code 501 EXPECT ServerError`() = runTest {

        whenever(loanApiService.logIn(any())) doReturn getErrorResponse(501)

        val expected = RequestResult.Error(RequestError.ServerError(Unit))

        val actual = repository.logIn(getSampleAuthInfo())

        Assertions.assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("emptyAuthInfo")
    fun `logIn gets empty strings EXPECT InputError`(authInfo: AuthInfo) = runTest {

        val expected = RequestResult.Error(RequestError.InvalidInput)

        val actual = repository.logIn(authInfo)

        Assertions.assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("emptyAuthInfo")
    fun `register gets empty strings EXPECT InputError`(authInfo: AuthInfo) = runTest {

        val expected = RequestResult.Error(RequestError.InvalidInput)

        val actual = repository.register(authInfo)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `register returns success EXPECT Success`() = runTest {
        whenever(loanApiService.register(any())) doReturn Response.success(
            UserEntityDto("test", "USER")
        )
        val expected = RequestResult.Success(Unit)

        val actual = repository.register(getSampleAuthInfo())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `register return code 400 EXPECT UserAlreadyExists`() = runTest {
        whenever(loanApiService.register(any())) doReturn getErrorResponse(400)

        val expected = RequestResult.Error(RequestError.UserAlreadyExists)

        val actual = repository.register(getSampleAuthInfo())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `register return code 501 EXPECT ServerError`() = runTest {
        whenever(loanApiService.register(any())) doReturn getErrorResponse(501)

        val expected = RequestResult.Error(RequestError.ServerError(Unit))

        val actual = repository.register(getSampleAuthInfo())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke checkIfLoggedIn, prefs has token EXPECT true`() = runTest {
        val expected = true

        val actual = repository.checkIfLoggedIn()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke getLoanConditions get result EXPECT Success`() = runTest {
        whenever(loanApiService.getLoanConditions(any())) doReturn Response.success(
            getSampleLoanConditionsDto()
        )
        val expected = RequestResult.Success(getSampleLoanConditionsDto().toEntity())

        val actual = repository.getLoanConditions()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke getLoanConditions returns 501 code EXPECT ServerError`() = runTest {
        whenever(loanApiService.getLoanConditions(any())) doReturn getErrorResponse(501)
        val expected = RequestResult.Error(RequestError.ServerError(null))

        val actual = repository.getLoanConditions()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke getAllLoans return loans EXPECT Success`() = runTest {
        val loanDto = getSampleLoanDto()
        whenever(loanApiService.getAllLoans(any())) doReturn Response.success(
            listOf(loanDto)
        )

        val expected = RequestResult.Success(listOf(getSampleLoan()))

        val actual = repository.getAllLoans()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke getAllLoans returns 503 EXPECT ServerError`() = runTest {

        whenever(loanApiService.getAllLoans(any())) doReturn getErrorResponse(503)
        whenever(loansDao.getAll()) doReturn emptyList()

        val expected = RequestResult.Error<List<Loan>>(RequestError.ServerError(emptyList()))

        val actual = repository.getAllLoans()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke getAllLoans meet server error EXPECT ServerError with cached loans`() = runTest {
        val loanDbModel = getSampleLoanDbModel()
        whenever(loanApiService.getAllLoans(any())) doReturn getErrorResponse(503)
        whenever(loansDao.getAll()) doReturn listOf(loanDbModel)

        val expected = RequestResult.Error(RequestError.ServerError(listOf(getSampleLoan())))

        val actual = repository.getAllLoans()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke logOut token deleted EXPECT true`() = runTest {
        val expected = true

        val actual = repository.logOut()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke createNewLoan gets result EXPECT Success`() = runTest {
        whenever(loanApiService.createNewLoan(any(), any())) doReturn Response.success(
            getSampleLoanDto()
        )

        val expected = RequestResult.Success(getSampleLoan())

        val actual = repository.createNewLoan(getSampleLoanRequest())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke createNewLoan returns code 400 EXPECT InputError`() = runTest {
        whenever(loanApiService.createNewLoan(any(), any())) doReturn getErrorResponse(400)

        val expected = RequestResult.Error(RequestError.InvalidInput)

        val actual = repository.createNewLoan(getSampleLoanRequest())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `invoke createNewLoan returns 503 code EXPECT ServerError`() = runTest {
        whenever(loanApiService.createNewLoan(any(), any())) doReturn getErrorResponse(503)

        val expected = RequestResult.Error(RequestError.ServerError(null))

        val actual = repository.createNewLoan(getSampleLoanRequest())

        Assertions.assertEquals(expected, actual)
    }
}