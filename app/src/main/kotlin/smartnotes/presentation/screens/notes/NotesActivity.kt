package smartnotes.presentation.screens.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_notes.*
import ru.github.anninpavel.smartnotes.R
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import smartnotes.di.common.ViewModelFactory
import smartnotes.domain.models.Note
import smartnotes.presentation.navigation.Screens
import smartnotes.presentation.navigation.observe
import smartnotes.utils.extensions.NO_RESOURCE
import smartnotes.utils.extensions.injectViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Экрана списка заметок.
 *
 * @property viewHolder Представление экрана.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var navigatorHolder: NavigatorHolder
    @Inject lateinit var router: Router
    private var viewHolder by Delegates.notNull<NotesViewHolder>()
    private var viewModel by Delegates.notNull<NotesViewModel>()
    private val navigator = SupportAppNavigator(this, NO_RESOURCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        viewModel = injectViewModel(viewModelFactory)
        navigatorHolder.observe(this, navigator)

        viewHolder = NotesViewHolder(notesMainContainer) { NotesAdapter.ViewType.LIST }.apply {
            onNoteClick = { router.navigateTo(Screens.EditNote(value = it)) }
            onCreateClick = { router.navigateTo(Screens.CreateNote()) }
        }

        viewModel.observeNotes()
    }

    /** Подписывает наблюдателя к коллекции заметок. */
    private fun NotesViewModel.observeNotes() {
        val observer = Observer<List<Note>> { data -> viewHolder.onBind(data) }
        liveNotes.observe(this@NotesActivity, observer)
    }
}
