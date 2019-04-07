package smartnotes.presentation.share.widgets.recyclerview.selection

import androidx.recyclerview.selection.ItemDetailsLookup

/**
 * Информация об элементе списка для [androidx.recyclerview.selection.SelectionTracker].
 *
 * @property key Ключ элемента.
 * @property position Позиция элемента.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class SelectionItemDetail<T>(
    private val key: T,
    private val position: Int
) : ItemDetailsLookup.ItemDetails<T>() {

    override fun getSelectionKey(): T {
        return key
    }

    override fun getPosition(): Int {
        return position
    }
}
