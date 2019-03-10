package smartnotes.presentation.common.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Обобщенное представление элемента списка.
 *
 * Используется в [RecyclerView.Adapter].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
abstract class ItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /** Этот метод вызывается для обновления контента представления [ItemViewHolder]. */
    abstract fun onBind(data: T)
}
