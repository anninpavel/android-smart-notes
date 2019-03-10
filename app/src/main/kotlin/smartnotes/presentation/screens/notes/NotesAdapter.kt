package smartnotes.presentation.screens.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.presentation.screens.notes.NotesAdapter.ViewType.GRID
import smartnotes.presentation.screens.notes.NotesAdapter.ViewType.LIST
import smartnotes.utils.kotlin.Consumer

/**
 * Адаптер списка заметок.
 *
 * @property viewType Тип представления списка.
 * @property onItemClick Событие, выбррана заметка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesAdapter(var viewType: ViewType) : ListAdapter<Note, ItemNotesViewHolder>(DIFF_CALLBACK) {

    var onItemClick: Consumer<Note>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeIndex: Int): ItemNotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = when (viewTypeIndex.toViewType()) {
            ViewType.LIST -> inflater.inflate(R.layout.item_notes_list, parent, false)
            ViewType.GRID -> inflater.inflate(R.layout.item_notes_grid, parent, false)
            else -> throw IllegalArgumentException("Unknown view type index $viewTypeIndex")
        }
        return ItemNotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemNotesViewHolder, position: Int) {
        getItem(position)?.let { item ->
            with(holder) {
                onClick = { onItemClick?.invoke(item) }
                onBind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    /**
     * Тип представления списка.
     *  - [LIST] Отображеине в виде списка;
     *  - [GRID] Отображеине в виде таблицы;
     */
    enum class ViewType { LIST, GRID }

    /**
     * Возвращает [ViewType] в сооствествии с индексом,
     * если тип представления по индексу не найден, будет возвращено `null`.
     */
    private fun Int.toViewType(): ViewType? {
        return ViewType.values().getOrNull(this)
    }

    private companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}
