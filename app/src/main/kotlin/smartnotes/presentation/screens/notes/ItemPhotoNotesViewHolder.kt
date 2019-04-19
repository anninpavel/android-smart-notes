package smartnotes.presentation.screens.notes

import android.view.View
import kotlinx.android.synthetic.main.item_notes_photo.view.*
import smartnotes.domain.models.Photo
import smartnotes.presentation.common.GlideApp
import smartnotes.presentation.common.viewholder.ItemViewHolder

/**
 * Представление элемента списка "Снимок заметки".
 **
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemPhotoNotesViewHolder(rootView: View) : ItemViewHolder<Photo>(rootView) {

    init {
        itemView.itemNotesPhotoImageView.clipToOutline = true
    }

    override fun onBind(data: Photo) {
        with(itemView) {
            GlideApp.with(context)
                .load(data.uri)
                .into(itemNotesPhotoImageView)
        }
    }
}
