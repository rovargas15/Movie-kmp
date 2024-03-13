package util

import exception.DomainException
import exception.TimeOutException
import exception.UnknownException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException

open class BaseRepository {
    inline fun <T, R> T.launchResultSafe(block: T.() -> R): Result<R> {
        return runCatching {
            block()
        }.onFailure()
    }

    /**
     * The method onFailure
     * uses methods getDomainExceptionFromRetrofitException
     * and getDomainNativeException to convert the appropriate
     * type of exception to a domain exception.
     */
    fun <T> Result<T>.onFailure(): Result<T> {
        return exceptionOrNull()?.let { error ->
            when (error) {
                is IOException -> {
                    Result.failure(getDomainException(error))
                }

                else -> {
                    Result.failure(UnknownException(error.message))
                }
            }
        } ?: run {
            return this
        }
    }

    /**
     * The method getDomainException
     * uses methods getDomainExceptionFromRetrofitException
     * and getDomainNativeException to convert the appropriate
     * type of exception to a domain exception.
     */
    private fun getDomainException(error: Throwable): DomainException {
        return when (error) {
            is ConnectTimeoutException -> {
                TimeOutException(error.message)
            }

            else -> {
                UnknownException(error.message)
            }
        }
    }
}
