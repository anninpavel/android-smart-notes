package smartnotes.data.cache

import android.content.Context
import smartnotes.domain.values.ViewType
import smartnotes.utils.android.string

/**
 * Источник данных предоставляющий доступ к хранимым настройка.
 *
 * @param context Контекст приложения.
 * @property pref Экземпляр [android.content.SharedPreferences].
 * @property _userViewType Пользовательская отображение списков.
 * @property userViewType Общедоступный интерфейс [_userViewType].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class PreferenceSource(context: Context) {

    private val pref by lazy { context.getSharedPreferences(NAME, Context.MODE_PRIVATE) }
    private var _userViewType by pref.string(defaultValue = ViewType.LIST.name, key = KEY_VIEW_TYPE)

    var userViewType: ViewType
        get() = ViewType.valueOf(_userViewType)
        set(value) {
            _userViewType = value.name
        }

    private companion object {
        private const val NAME = "Settings"
        private const val KEY_VIEW_TYPE = "user_view_type"
    }
}
