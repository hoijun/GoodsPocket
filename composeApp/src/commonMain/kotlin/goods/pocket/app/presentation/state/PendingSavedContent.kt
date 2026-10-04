package goods.pocket.app.presentation.state

/** Bridges a completed write until the read stream publishes its next snapshot. */
internal data class PendingSavedContent<T>(val value: T, val observationVersion: Long)
