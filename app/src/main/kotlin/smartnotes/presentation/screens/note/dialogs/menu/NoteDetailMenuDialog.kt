package smartnotes.presentation.screens.note.dialogs.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.domain.values.NotePriority
import smartnotes.utils.extensions.require
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer
import kotlin.properties.Delegates

/**
 * Диалог "Меню заметки".
 *
 * @property onExport Событие, выбрано экспортирование заметки.
 * @property onRemove Событие, выбрано удаление заметки.
 * @property onPriorityChange Событие, выбран приоритет заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailMenuDialog : BottomSheetDialogFragment() {

    private var viewHolder by Delegates.notNull<NoteDetailMenuViewHolder>()
    private var note by Delegates.notNull<Note>()

    var onExport: Action? = null
    var onRemove: Action? = null
    var onPriorityChange: Consumer<NotePriority>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = requireNotNull(savedInstanceState ?: arguments).require(key = BUNDLE_NOTE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_note_detail_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder = NoteDetailMenuViewHolder(rootView = view).apply {
            onExportClick = { onExport?.invoke(); dismiss() }
            onRemoveClick = { onRemove?.invoke(); dismiss() }
            onPriorityClick = { onPriorityChange?.invoke(it); dismiss() }
            onBind(NotePriority.values().toList(), note.priority)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_NOTE, note)
    }

    companion object {
        const val TAG = "NoteDetailMenuDialog"
        private const val BUNDLE_NOTE = ".bundles.note"

        /** Возвращает новый экземпляр [NoteDetailMenuDialog]. */
        fun newInstance(note: Note): NoteDetailMenuDialog {
            return NoteDetailMenuDialog().apply {
                arguments = bundleOf(BUNDLE_NOTE to note)
            }
        }
    }
}
