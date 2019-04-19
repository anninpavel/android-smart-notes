package smartnotes.presentation.screens.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.domain.values.ViewType
import smartnotes.presentation.share.widgets.recyclerview.selection.SelectionItemKeyProvider
import smartnotes.utils.kotlin.Predicate

/**
 * Адаптер списка заметок.
 *
 * @property viewType Тип представления списка.
 * @property isSelectedPredicate Событие, возвращает выбранное состояние, для передаваемого элемента.
 * @property photoViewPool Контейнер содержащий представления сников, для списка заметок.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesAdapter(
    var viewType: ViewType,
    var isSelectedPredicate: Predicate<Note> = { false }
) : ListAdapter<Note, ItemNotesViewHolder>(DIFF_CALLBACK), SelectionItemKeyProvider<Note> {

    private val photoViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeIndex: Int): ItemNotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = when (viewTypeIndex.toViewType()) {
            ViewType.LIST -> inflater.inflate(R.layout.item_notes_list, parent, false)
            ViewType.GRID -> inflater.inflate(R.layout.item_notes_grid, parent, false)
            else -> throw IllegalArgumentException("Unknown view type index $viewTypeIndex")
        }
        return ItemNotesViewHolder(itemView, photoViewPool)
    }

    override fun onBindViewHolder(holder: ItemNotesViewHolder, position: Int) {
        getItem(position)?.let { item ->
            with(holder) {
                isChecked = isSelectedPredicate(item)
                onBind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    override fun getKey(position: Int): Note? {
        return getItem(position)
    }

    override fun getPosition(key: Note): Int {
        return currentList.indexOf(key)
    }

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
