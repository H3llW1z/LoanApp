package com.panassevich.panassevich.shared.loans.core.data.implementation

import android.content.SharedPreferences
import androidx.core.content.edit
import com.panassevich.panassevich.shared.loans.core.data.api.LoanApiService
import com.panassevich.panassevich.shared.loans.core.data.db.LoansDao
import com.panassevich.panassevich.shared.loans.core.data.toDbModel
import com.panassevich.panassevich.shared.loans.core.data.toDto
import com.panassevich.panassevich.shared.loans.core.data.toEntity
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanConditions
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanRequest
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.repository.LoanRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class LoanRepositoryImpl @Inject constructor(
    private val loanApiService: LoanApiService,
    private val loansDao: LoansDao,
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) : LoanRepository {

    companion object {
        private const val TOKEN_KEY = "token_key"
    }

    private var _token: String? = sharedPreferences.getString(TOKEN_KEY, null)
    private val token: String
        get() = _token ?: throw RuntimeException("Log in first. Token is null.")

    override suspend fun register(authInfo: AuthInfo): RequestResult<Unit> =
        withContext(ioDispatcher) {
            if (authInfo.name.isBlank() || authInfo.password.isBlank())
                return@withContext RequestResult.Error(RequestError.InvalidInput)

            val response = try {
                loanApiService.register(authInfo.toDto())
            } catch (exception: Exception) {
                return@withContext mapExceptionToRequestResult(exception, Unit)
            }

            return@withContext if (response.isSuccessful) {
                RequestResult.Success(Unit)
            } else when (val code = response.code()) {
                400 -> RequestResult.Error(RequestError.UserAlreadyExists)
                in 500..599 -> RequestResult.Error(RequestError.ServerError(Unit))
                else -> throwUnknownCodeException(code)
            }
        }

    override suspend fun logIn(authInfo: AuthInfo): RequestResult<Unit> =
        withContext(ioDispatcher) {
            if (authInfo.name.isBlank() || authInfo.password.isBlank())
                return@withContext RequestResult.Error(RequestError.InvalidInput)

            val response = try {
                loanApiService.logIn(authInfo.toDto())
            } catch (exception: Exception) {
                return@withContext mapExceptionToRequestResult(exception, Unit)
            }

            return@withContext if (response.isSuccessful) {
                val userToken = response.requireBody()

                _token = userToken
                sharedPreferences.edit {
                    putString(TOKEN_KEY, userToken)
                }

                RequestResult.Success(Unit)
            } else when (val code = response.code()) {
                404 -> RequestResult.Error(RequestError.UserNotFound)
                in 500..599 -> RequestResult.Error(RequestError.ServerError(Unit))
                else -> throwUnknownCodeException(code)
            }
        }

    override fun checkIfLoggedIn(): Boolean =
        sharedPreferences.contains(TOKEN_KEY)

    override suspend fun logOut(): Boolean =
        withContext(ioDispatcher) {
            val isRemoved = with(sharedPreferences.edit()) {
                remove(TOKEN_KEY)
                commit()
            }
            return@withContext if (isRemoved) {
                _token = null
                loansDao.removeAll()
                true
            } else false
        }

    override suspend fun getLoanConditions(): RequestResult<LoanConditions> =
        withContext(ioDispatcher) {
            val response = try {
                loanApiService.getLoanConditions(token)
            } catch (exception: Exception) {
                return@withContext mapExceptionToRequestResult(exception, null)
            }

            return@withContext if (response.isSuccessful) {
                val loanConditions = response.requireBody()
                RequestResult.Success(loanConditions.toEntity())
            } else when (val code = response.code()) {
                in 500..599 -> RequestResult.Error(RequestError.ServerError(null))
                else -> throwUnknownCodeException(code)
            }
        }

    override suspend fun createNewLoan(loanRequest: LoanRequest): RequestResult<Loan> =
        withContext(ioDispatcher) {
            val response = try {
                loanApiService.createNewLoan(loanRequest.toDto(), token)
            } catch (exception: Exception) {
                return@withContext mapExceptionToRequestResult(exception, null)
            }

            return@withContext if (response.isSuccessful) {
                val loan = response.requireBody()
                loansDao.insert(loan.toDbModel())
                RequestResult.Success(loan.toEntity())
            } else when (val code = response.code()) {
                400 -> RequestResult.Error<Loan>(RequestError.InvalidInput)
                in 500..599 -> RequestResult.Error(RequestError.ServerError(null))
                else -> throwUnknownCodeException(code)
            }
        }

    override suspend fun getAllLoans(): RequestResult<List<Loan>> =
        withContext(ioDispatcher) {
            val response = try {
                loanApiService.getAllLoans(token)
            } catch (exception: Exception) {
                return@withContext mapExceptionToRequestResult(exception, getAllCachedLoans())
            }

            return@withContext if (response.isSuccessful) {
                val loans = response.requireBody()
                loansDao.insert(loans.map { it.toDbModel() })
                RequestResult.Success(loans.map { it.toEntity() })
            } else when (val code = response.code()) {
                in 500..599 -> RequestResult.Error(RequestError.ServerError(getAllCachedLoans()))
                else -> throwUnknownCodeException(code)
            }
        }

    private suspend fun getAllCachedLoans() = loansDao.getAll().map { it.toEntity() }

    private fun throwUnknownCodeException(code: Int): Nothing =
        throw RuntimeException("Unknown error code: $code")

    private fun <T> mapExceptionToRequestResult(
        exception: Exception,
        cachedContent: T?
    ): RequestResult<T> =
        when (exception) {
            is UnknownHostException -> RequestResult.Error(RequestError.NetworkError(cachedContent))
            is SocketTimeoutException -> RequestResult.Error(RequestError.NetworkError(cachedContent))
            is SSLHandshakeException -> RequestResult.Error(RequestError.ServerError(cachedContent))
            else -> throw exception
        }

    private fun <T> Response<T>.requireBody() =
        this.body() ?: throw RuntimeException("Response body is null")

}