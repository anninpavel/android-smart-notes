package smartnotes.presentation.screens.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Photo
import smartnotes.utils.kotlin.Consumer

/**
 * Адаптер списка снимков заметки.
 *
 * @property onItemClick Событие, выбран снимок.
 * @property onItemRemoveClick Событие, выбрано удаление снимка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class PhotoNoteDetailAdapter : ListAdapter<Photo, ItemPhotoNoteDetailViewHolder>(DIFF_CALLBACK) {

    var onItemClick: Consumer<Photo>? = null
    var onItemRemoveClick: Consumer<Photo>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhotoNoteDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_note_detail_photo, parent, false)
        return ItemPhotoNoteDetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemPhotoNoteDetailViewHolder, position: Int) {
        getItem(position)?.let { item ->
            with(holder) {
                onClick = { onItemClick?.invoke(item) }
                onRemoveClick = { onItemRemoveClick?.invoke(item) }
                onBind(item)
            }
        }
    }

    private companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}
