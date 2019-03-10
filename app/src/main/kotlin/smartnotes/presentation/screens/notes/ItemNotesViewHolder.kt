package smartnotes.presentation.screens.notes

import android.content.res.Resources
import android.view.View
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.item_notes_grid.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.presentation.common.viewholder.ItemViewHolder
import smartnotes.utils.kotlin.Action

/**
 * Представление элемента списка "Заметок".
 *
 * @property title Заголовок заметки, отображаемый на представлении.
 * @property text Текст заметки, отображаемый на представлении.
 *
 * @property onClick Событие, выбрано представление.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemNotesViewHolder(rootView: View) : ItemViewHolder<Note>(rootView) {

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

    var onClick: Action? = null

    init {
        itemView.setOnClickListener { onClick?.invoke() }
    }

    override fun onBind(data: Note) {
        title = data.title
        text = data.text
    }

    /** Возвращает текстовую заглушку для заголовка заметки. */
    private fun Resources.titleStub(): CharSequence {
        return getString(R.string.notes_label_untitled)
    }
}
