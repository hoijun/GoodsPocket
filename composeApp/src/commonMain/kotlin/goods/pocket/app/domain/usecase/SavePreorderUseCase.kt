package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.repository.PreorderRepository

class SavePreorderUseCase(
    private val preorderRepository: PreorderRepository,
) {
    operator fun invoke(preorder: Preorder) {
        preorderRepository.savePreorder(preorder)
    }
}
