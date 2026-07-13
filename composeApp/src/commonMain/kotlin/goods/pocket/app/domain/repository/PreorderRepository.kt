package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus

interface PreorderRepository {
    suspend fun getPreorders(status: PreorderStatus? = null): List<Preorder>
    suspend fun cancelPreorder(preorderId: String, canceledAt: String)
    suspend fun countActivePreorders(): Int
}
