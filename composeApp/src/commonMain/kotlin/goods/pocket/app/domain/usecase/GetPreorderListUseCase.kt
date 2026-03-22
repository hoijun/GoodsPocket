package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.repository.PreorderRepository

class GetPreorderListUseCase(
    private val preorderRepository: PreorderRepository,
) {
    operator fun invoke(status: PreorderStatus? = null): List<Preorder> = preorderRepository.getPreorders(status)
}
