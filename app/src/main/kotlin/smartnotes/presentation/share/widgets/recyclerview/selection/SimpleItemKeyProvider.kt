package smartnotes.presentation.share.widgets.recyclerview.selection

import androidx.recyclerview.selection.ItemKeyProvider

/**
 * Реализация [ItemKeyProvider].
 *
 * @param scope Область применения провайдера.
 * @property provider Провайдер ключей.
 *
 * @see ItemKeyProvider
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class SimpleItemKeyProvider<T>(
    scope: Int,
    private val provider: SelectionItemKeyProvider<T>
) : ItemKeyProvider<T>(scope) {

    override fun getKey(position: Int): T? {
        return provider.getKey(position)
    }

    override fun getPosition(key: T): Int {
        return provider.getPosition(key)
    }
}
