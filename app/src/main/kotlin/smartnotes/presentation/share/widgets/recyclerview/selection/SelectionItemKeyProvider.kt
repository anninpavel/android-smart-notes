package smartnotes.presentation.share.widgets.recyclerview.selection

/**
 * Интерфейс провайдера ключей для [androidx.recyclerview.selection.SelectionTracker].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface SelectionItemKeyProvider<T> {

    /** @return Ключ элемента для [position], или `null`. */
    fun getKey(position: Int): T?

    /** @return Позицию клюса для экземпляра [key]. */
    fun getPosition(key: T): Int
}
