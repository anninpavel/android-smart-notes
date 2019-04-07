package smartnotes.presentation.screens.notes

import android.content.res.Resources
import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.selection.ItemDetailsLookup
import kotlinx.android.synthetic.main.item_notes_grid.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.presentation.common.viewholder.ItemViewHolder
import smartnotes.presentation.share.widgets.recyclerview.selection.SelectionItemDetail
import smartnotes.presentation.share.widgets.recyclerview.selection.SelectionItemViewHolder

/**
 * Представление элемента списка "Заметок".
 *
 * @property cacheItemDetail Информация об элементе, для поддержки выделения элементов списка.
 * @property title Заголовок заметки, отображаемый на представлении.
 * @property text Текст заметки, отображаемый на представлении.
 * @property isChecked Флаг определяющий состояние индикатора выделеного элемента на представлении.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemNotesViewHolder(rootView: View) : ItemViewHolder<Note>(rootView), SelectionItemViewHolder<Note> {

    private var cacheItemDetail: ItemDetailsLookup.ItemDetails<Note>? = null

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

    var isChecked: Boolean
        get() = itemView.itemNotesMainContainer.isActivated
        set(value) = with (itemView.itemNotesMainContainer) {
            isActivated = value
        }

    override fun onBind(data: Note) {
        title = data.title
        text = data.text

        cacheItemDetail = SelectionItemDetail(data, adapterPosition)
    }

    override fun getItemDetails(): ItemDetailsLookup.ItemDetails<Note>? {
        return cacheItemDetail
    }

    /** Возвращает текстовую заглушку для заголовка заметки. */
    private fun Resources.titleStub(): CharSequence {
        return getString(R.string.notes_label_untitled)
    }
}
