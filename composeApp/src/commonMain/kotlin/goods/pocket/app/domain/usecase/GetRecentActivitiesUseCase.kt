package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.i18n.DEFAULT_LANGUAGE_CODE
import goods.pocket.app.i18n.localizedRecentItemAddedSubtitle
import goods.pocket.app.i18n.localizedRecentPreorderTrackedSubtitle

class GetRecentActivitiesUseCase(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
) {
    suspend operator fun invoke(limit: Int, languageCode: String = DEFAULT_LANGUAGE_CODE): List<ActivityRecord> {
        val itemActivities = collectionRepository.getItems().map { it.toActivityRecord(languageCode) }
        val preorderActivities = preorderRepository.getPreorders().map { it.toActivityRecord(languageCode) }
        return (itemActivities + preorderActivities)
            .sortedByDescending(ActivityRecord::happenedAt)
            .take(limit)
    }
}

private fun Item.toActivityRecord(languageCode: String): ActivityRecord {
    return ActivityRecord(
        id = id,
        title = name,
        subtitle = localizedRecentItemAddedSubtitle(languageCode),
        happenedAt = updatedAt,
    )
}

private fun Preorder.toActivityRecord(languageCode: String): ActivityRecord {
    return ActivityRecord(
        id = id,
        title = name,
        subtitle = localizedRecentPreorderTrackedSubtitle(languageCode, storeName),
        happenedAt = updatedAt,
    )
}
