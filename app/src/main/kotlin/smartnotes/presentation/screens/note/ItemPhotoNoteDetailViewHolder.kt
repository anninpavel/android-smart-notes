package smartnotes.presentation.screens.note

import android.view.View
import kotlinx.android.synthetic.main.item_note_detail_photo.view.*
import smartnotes.domain.models.Photo
import smartnotes.presentation.common.GlideApp
import smartnotes.presentation.common.viewholder.ItemViewHolder
import smartnotes.utils.kotlin.Action

/**
 * Представление элемента списка "Снимок заметки".
 *
 * @property onClick Событие, выбран снимок.
 * @property onRemoveClick Событие, выбрано удаление снимка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemPhotoNoteDetailViewHolder(rootView: View) : ItemViewHolder<Photo>(rootView) {

    var onClick: Action? = null
    var onRemoveClick: Action? = null

    init {
        with(itemView) {
            itemNoteDetailPhotoImageView.clipToOutline = true
            itemNoteDetailPhotoImageView.setOnClickListener { onClick?.invoke() }
            itemNoteDetailPhotoRemoveImageButton.setOnClickListener { onRemoveClick?.invoke() }
        }
    }

    override fun onBind(data: Photo) {
        with(itemView) {
            GlideApp.with(context)
                .load(data.uri)
                .into(itemNoteDetailPhotoImageView)
        }
    }
}
