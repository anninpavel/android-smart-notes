package smartnotes.domain.usecase

import smartnotes.data.cache.PreferenceSource
import smartnotes.domain.values.ViewType
import smartnotes.presentation.usecase.UserUseCase

/**
 * Реализация [UserUseCase].
 *
 * @property preferences Источник хранимых настроек приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class UserUseCaseImpl(
    private val preferences: PreferenceSource
) : UserUseCase {

    override fun fetchViewType(): ViewType {
        return preferences.userViewType
    }

    override fun saveViewType(value: ViewType) {
        preferences.userViewType = value
    }
}
