package exception

sealed class DomainException(
    message: String? = null,
    cause: Throwable? = null,
) : Throwable(message, cause)

data class UnknownException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : DomainException(message, cause)

data class TimeOutException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : DomainException(message, cause)
