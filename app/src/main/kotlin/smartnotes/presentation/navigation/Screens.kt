@file:JvmMultifileClass
@file:Suppress(names = ["CanSealedSubClassBeObject"])

package smartnotes.presentation.navigation

import android.content.Context
import android.content.Intent
import ru.terrakok.cicerone.android.support.SupportAppScreen
import smartnotes.domain.models.Note
import smartnotes.presentation.screens.note.NoteDetailActivity
import smartnotes.presentation.screens.notes.NotesActivity
import smartnotes.utils.extensions.intentFor

/**
 * Перечисление экранов используемых для навигации по приложению.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
sealed class Screens : SupportAppScreen() {

    override fun getScreenKey(): String {
        return javaClass.simpleName
    }

    override fun getActivityIntent(context: Context): Intent {
        return when (this) {
            is Notes -> context.intentFor<NotesActivity>()
            is CreateNote -> NoteDetailActivity.newInstanceWithCreateMode(context)
            is EditNote -> NoteDetailActivity.newInstanceWithEditMode(context, note = value)
        }
    }

    /** Экран списка заметок. */
    class Notes : Screens()

    /** Экран создания заметки. */
    class CreateNote : Screens()

    /**
     * Экран редактирования заметки.
     *
     * @property value Экземпляр заметки, который должен быть отредактирован.
     */
    class EditNote(val value: Note) : Screens()
}
