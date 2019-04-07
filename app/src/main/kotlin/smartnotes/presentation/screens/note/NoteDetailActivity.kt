package smartnotes.presentation.screens.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_note_detail.*
import ru.github.anninpavel.smartnotes.R
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import smartnotes.di.common.ViewModelFactory
import smartnotes.domain.models.Note
import smartnotes.presentation.common.Response
import smartnotes.presentation.navigation.observe
import smartnotes.presentation.screens.note.NoteDetailActivity.Mode.Create
import smartnotes.presentation.screens.note.NoteDetailActivity.Mode.Edit
import smartnotes.utils.extensions.NO_RESOURCE
import smartnotes.utils.extensions.injectViewModel
import smartnotes.utils.extensions.intentFor
import smartnotes.utils.extensions.requireExtra
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Экран создания и редактирования заметки.
 *
 * @property viewModelFactory Фабрика предоставления [NoteDetailViewModel].
 * @property navigatorHolder Привязывает навигатор к жизненому циклу экрана.
 * @property router Маршрутизатор по приложению.
 * @property viewHolder Представление экрана.
 * @property viewModel ViewModel экрана.
 * @property navigator Навигатор экрана.
 * @property startupMode Режим запуска экрана.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var navigatorHolder: NavigatorHolder
    @Inject lateinit var router: Router
    private var viewHolder by Delegates.notNull<NoteDetailViewHolder>()
    private var viewModel by Delegates.notNull<NoteDetailViewModel>()
    private val navigator = SupportAppNavigator(this, NO_RESOURCE)
    private var startupMode by Delegates.notNull<Mode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        startupMode = requireExtra(key = EXTRA_MODE)

        viewModel = injectViewModel(viewModelFactory)
        navigatorHolder.observe(this, navigator)

        viewHolder = NoteDetailViewHolder(rootViewGroup = noteDetailMainContainer).apply {
            onBackClick = { startupMode.makeSave() }
            onRemoveClick = { startupMode.makeRemove() }

            val data = startupMode.let { mode ->
                return@let when (mode) {
                    is Mode.Create -> null
                    is Mode.Edit -> mode.value
                }
            }
            onBind(data)
        }

        viewModel.observeNote()
    }

    override fun onBackPressed() {
        startupMode.makeSave()
    }

    /** Подписывает наблюдателей к текущей заметке. */
    private fun NoteDetailViewModel.observeNote() {
        val observer = Observer<Response<Unit>> { response ->
            when (response) {
                is Response.Progress -> viewHolder.isLocked = true
                is Response.Success -> router.exit()
                is Response.Failure -> {
                    viewHolder.isLocked = false
                    Timber.w(response.error)
                }
            }
        }
        liveCreateOrUpdateNote.observe(this@NoteDetailActivity, observer)
        liveDeleteNote.observe(this@NoteDetailActivity, observer)
    }

    /** Сохраняет заметку. */
    private fun Mode.makeSave(title: CharSequence? = viewHolder.title, text: CharSequence? = viewHolder.text) {
        when (this) {
            is Mode.Create -> viewModel.create(title, text)
            is Mode.Edit -> viewModel.save(value, title, text)
        }
    }

    /** Удаляет заметку. */
    private fun Mode.makeRemove() {
        when (this) {
            is Mode.Create -> router.exit()
            is Mode.Edit -> viewModel.delete(value)
        }
    }

    /**
     * Режим работы экрана.
     *
     * - [Create]
     * - [Edit]
     */
    @Suppress(names = ["CanSealedSubClassBeObject"])
    sealed class Mode : Parcelable {

        /** Режим создания новой заметки. */
        @Parcelize
        class Create : Mode()

        /**
         * Режим редактирвоания заметки.
         *
         * @property value Редактируемая заметка.
         */
        @Parcelize
        data class Edit(val value: Note) : Mode()
    }

    companion object {
        private const val EXTRA_MODE = ".extras.mode"

        /** Создает новый экземпляр [Intent], для открытия экрана в режиме создания заметки. */
        fun newInstanceWithCreateMode(context: Context): Intent {
            return context.intentFor<NoteDetailActivity>().apply {
                putExtra(EXTRA_MODE, Mode.Create())
            }
        }

        /** Создает новый экземпляр [Intent], для открытия экрана в режиме редактирования заметки. */
        fun newInstanceWithEditMode(context: Context, note: Note): Intent {
            return context.intentFor<NoteDetailActivity>().apply {
                putExtra(EXTRA_MODE, Mode.Edit(note))
            }
        }
    }
}
