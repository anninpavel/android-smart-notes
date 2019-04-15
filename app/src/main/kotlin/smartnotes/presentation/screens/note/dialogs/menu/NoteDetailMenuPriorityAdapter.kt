package smartnotes.presentation.screens.note.dialogs.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.values.NotePriority
import smartnotes.utils.kotlin.Consumer

private typealias PriorityChecked = Pair<NotePriority, Boolean>

/**
 * Адаптер списка приоритетов заметки.
 *
 * @property onItemClick Событие, выбран приоритет.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailMenuPriorityAdapter :
    ListAdapter<PriorityChecked, ItemNoteDetailMenuPriorityViewHolder>(DIFF_CALLBACK) {

    var onItemClick: Consumer<NotePriority>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNoteDetailMenuPriorityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemRootView = inflater.inflate(R.layout.item_note_detail_menu_priority, parent, false)
        return ItemNoteDetailMenuPriorityViewHolder(itemRootView)
    }

    override fun onBindViewHolder(holder: ItemNoteDetailMenuPriorityViewHolder, position: Int) {
        getItem(position)?.let { (priority, checked) ->
            with(holder) {
                onClick = { onItemClick?.invoke(priority) }
                onBind(priority)
                isChecked = checked
            }
        }
    }

    /** Отправляет список приоритетов, для обновления представления. */
    fun submitList(list: List<NotePriority>, checked: NotePriority) {
        val newList = list.map { PriorityChecked(it, it == checked) }
        super.submitList(newList)
    }

    private companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PriorityChecked>() {
            override fun areItemsTheSame(oldItem: PriorityChecked, newItem: PriorityChecked): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(oldItem: PriorityChecked, newItem: PriorityChecked): Boolean {
                return oldItem.second == newItem.second
            }
        }
    }
}
