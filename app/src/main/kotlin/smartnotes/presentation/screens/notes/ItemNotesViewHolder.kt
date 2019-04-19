package smartnotes.presentation.screens.notes

import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notes_grid.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.domain.values.NotePriority
import smartnotes.domain.values.color
import smartnotes.presentation.common.viewholder.ItemViewHolder
import smartnotes.presentation.share.decorators.ItemOffsetDecoration
import smartnotes.presentation.share.widgets.recyclerview.selection.SelectionItemDetail
import smartnotes.presentation.share.widgets.recyclerview.selection.SelectionItemViewHolder
import smartnotes.utils.extensions.px

/**
 * Представление элемента списка "Заметок".
 *
 * @property cacheItemDetail Информация об элементе для поддержки выделения элементов списка.
 * @property title Заголовок заметки отображаемый на представлении.
 * @property text Текст заметки отображаемый на представлении.
 * @property priorityIndicator Индикатор приоритета отображаемый на представлении.
 * @property isChecked Флаг определяющий состояние индикатора выделеного элемента на представлении.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemNotesViewHolder(
    rootView: View,
    photosViewPool: RecyclerView.RecycledViewPool
) : ItemViewHolder<Note>(rootView), SelectionItemViewHolder<Note> {

    private var cacheItemDetail: ItemDetailsLookup.ItemDetails<Note>? = null
    private val photoAdapter = PhotoNotesAdapter()

    private var title: CharSequence?
        get() = itemView.itemNotesTitleTextView.text
        set(value) = with(itemView.itemNotesTitleTextView) {
            text = if (value.isNullOrBlank()) resources.titleStub() else value
        }

    private var text: CharSequence?
        get() = itemView.itemNotesTextTextView.text
        set(value) = with(itemView.itemNotesTextTextView) {
            text = value
            isGone = value.isNullOrBlank()
        }

    private var priorityIndicator: NotePriority = NotePriority.NO_PRIORITY
        set(value) = with(itemView.itemNotesPriorityIndicatorImageView) {
            field = value
            val color = priorityIndicator.color(context)
            imageTintList = color?.let { ColorStateList.valueOf(it) }
            isVisible = color != null
        }

    var isChecked: Boolean
        get() = itemView.itemNotesMainContainer.isActivated
        set(value) = with(itemView.itemNotesMainContainer) {
            isActivated = value
        }

    init {
        with(itemView.itemNotesPhotosRecyclerView) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(photosViewPool)
            addItemDecoration(ItemOffsetDecoration(offset = PHOTOS_ITEM_OFFSET.px))
            adapter = photoAdapter
        }
    }

    override fun onBind(data: Note) {
        title = data.title
        text = data.text
        priorityIndicator = data.priority
        photoAdapter.submitList(data.photos)

        cacheItemDetail = SelectionItemDetail(data, adapterPosition)
    }

    override fun getItemDetails(): ItemDetailsLookup.ItemDetails<Note>? {
        return cacheItemDetail
    }

    /** Возвращает текстовую заглушку для заголовка заметки. */
    private fun Resources.titleStub(): CharSequence {
        return getString(R.string.notes_label_untitled)
    }

    private companion object {
        private const val PHOTOS_ITEM_OFFSET = 4
    }
}
