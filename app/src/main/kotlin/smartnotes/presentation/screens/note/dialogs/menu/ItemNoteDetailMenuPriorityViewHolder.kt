package smartnotes.presentation.screens.note.dialogs.menu

import android.content.res.ColorStateList
import android.view.View
import kotlinx.android.synthetic.main.item_note_detail_menu_priority.view.*
import smartnotes.domain.values.NotePriority
import smartnotes.domain.values.color
import smartnotes.presentation.common.viewholder.ItemViewHolder
import smartnotes.utils.kotlin.Action

/**
 * Представление элемента списка "Приоритет заметки".
 *
 * @property priorityIndicator Индикатор приоритета отображаемый на представлении.
 * @property isChecked Флаг отпределяющий состояние выюранного приоритета на представлении.
 *
 * @property onClick Событие, выбран приортиет.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemNoteDetailMenuPriorityViewHolder(rootView: View) : ItemViewHolder<NotePriority>(rootView) {

    private var priorityIndicator: NotePriority = NotePriority.NO_PRIORITY
        set(value) = with(itemView.itemNoteDetailMenuPriorityImageView) {
            field = value
            backgroundTintList = priorityIndicator.color(context)?.let { ColorStateList.valueOf(it) }
        }

    var isChecked: Boolean
        get() = itemView.itemNoteDetailMenuPriorityImageView.isSelected
        set(value) = with(itemView.itemNoteDetailMenuPriorityImageView) {
            isSelected = value
        }

    var onClick: Action? = null

    init {
        itemView.itemNoteDetailMenuPriorityMainContainer.setOnClickListener { onClick?.invoke() }
    }

    override fun onBind(data: NotePriority) {
        priorityIndicator = data
    }
}
