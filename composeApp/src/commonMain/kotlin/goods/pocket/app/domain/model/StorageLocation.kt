package goods.pocket.app.domain.model

data class StorageLocation(
    val id: String,
    val name: String,
    val parentId: String? = null,
    val memo: String? = null,
    val createdAt: String,
)
