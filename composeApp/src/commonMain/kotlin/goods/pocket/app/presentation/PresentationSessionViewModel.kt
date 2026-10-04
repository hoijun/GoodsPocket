package goods.pocket.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class PresentationSessionViewModel(
    factory: PresentationSessionFactory,
    private val closeResources: () -> Unit,
) : ViewModel() {
    val session: PresentationSession = factory.create(viewModelScope)

    override fun onCleared() {
        session.close()
        closeResources()
    }
}
