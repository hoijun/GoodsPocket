package goods.pocket.app.domain.repository

class RepositoryFailure(
    val operation: RepositoryOperation,
    cause: Throwable,
) : Exception("Repository ${operation.name.lowercase()} failed", cause)

enum class RepositoryOperation {
    READ,
    WRITE,
}
