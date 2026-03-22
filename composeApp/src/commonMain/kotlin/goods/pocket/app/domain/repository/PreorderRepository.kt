package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus

interface PreorderRepository {
    fun getPreorders(status: PreorderStatus? = null): List<Preorder>
    fun getPreorder(id: String): Preorder?
    fun savePreorder(preorder: Preorder)
    fun markAsReceived(preorderId: String, receiveDate: String)
    fun cancelPreorder(preorderId: String)
    fun countActivePreorders(): Int
}
