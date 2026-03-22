package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.repository.PreorderRepository

class CancelPreorderUseCase(
    private val preorderRepository: PreorderRepository,
) {
    operator fun invoke(preorderId: String) {
        preorderRepository.cancelPreorder(preorderId)
    }
}
