package smartnotes.presentation.share.widgets.recyclerview.selection

import androidx.recyclerview.selection.ItemDetailsLookup

/**
 * Интерфейс получения информации об элементе списка для [androidx.recyclerview.selection.SelectionTracker].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface SelectionItemViewHolder<T> {

    /** @return Информацию об элементе списка или `null`. */
    fun getItemDetails(): ItemDetailsLookup.ItemDetails<T>?
}
