package smartnotes.presentation.share.widgets.recyclerview.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * Реализация [ItemDetailsLookup].
 *
 * @property recyclerView [RecyclerView] для выделения элементов.
 *
 * @see ItemDetailsLookup
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class SimpleItemDetailsLookup<T>(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<T>() {

    override fun getItemDetails(event: MotionEvent): ItemDetailsLookup.ItemDetails<T>? {
        return recyclerView.findChildViewUnder(event.x, event.y)?.let { view ->
            recyclerView.getChildViewHolder(view)?.asItemDetails()
        }
    }
}

/**
 * Приводит [RecyclerView.ViewHolder] к [ItemDetailsLookup.ItemDetails].
 *
 * @receiver [RecyclerView.ViewHolder].
 */
private inline fun <T, reified U> RecyclerView.ViewHolder.asItemDetails(): ItemDetailsLookup.ItemDetails<T>?
        where U : SelectionItemViewHolder<T> {
    return (this as? U)?.getItemDetails()
}
