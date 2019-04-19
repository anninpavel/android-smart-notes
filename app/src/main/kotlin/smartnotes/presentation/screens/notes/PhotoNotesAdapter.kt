package smartnotes.presentation.screens.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Photo

/**
 * Адаптер списка снимков заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class PhotoNotesAdapter : ListAdapter<Photo, ItemPhotoNotesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhotoNotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_notes_photo, parent, false)
        return ItemPhotoNotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemPhotoNotesViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.onBind(item) }
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
