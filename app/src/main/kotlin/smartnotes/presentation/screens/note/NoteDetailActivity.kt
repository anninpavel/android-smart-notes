package smartnotes.presentation.screens.note

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_note_detail.*
import ru.github.anninpavel.smartnotes.R
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import smartnotes.di.common.ViewModelFactory
import smartnotes.domain.models.Note
import smartnotes.domain.models.Photo
import smartnotes.presentation.common.Response
import smartnotes.presentation.navigation.observe
import smartnotes.presentation.screens.note.NoteDetailActivity.Mode.Create
import smartnotes.presentation.screens.note.NoteDetailActivity.Mode.Edit
import smartnotes.presentation.screens.note.dialogs.menu.NoteDetailMenuDialog
import smartnotes.utils.android.activityResultManager
import smartnotes.utils.android.permissionManager
import smartnotes.utils.extensions.NO_RESOURCE
import smartnotes.utils.extensions.debounce
import smartnotes.utils.extensions.documentFile
import smartnotes.utils.extensions.injectViewModel
import smartnotes.utils.extensions.intentFor
import smartnotes.utils.extensions.isSafe
import smartnotes.utils.extensions.requireExtra
import smartnotes.utils.extensions.takePersistablePermission
import smartnotes.utils.extensions.toast
import smartnotes.utils.kotlin.Consumer
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
 * @property permissions Менеджер запроса разрешений.
 * @property activityResults Менеджер обработки результатов [AppCompatActivity].
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
    private val disposables = CompositeDisposable()
    private val permissions by permissionManager()
    private val activityResults by activityResultManager()
    private var startupMode by Delegates.notNull<Mode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        startupMode = requireExtra(key = EXTRA_MODE)

        viewModel = injectViewModel(viewModelFactory)
        navigatorHolder.observe(this, navigator)

        savedInstanceState ?: (startupMode as? Edit)?.let { viewModel.editNote(value = it.value) }

        viewHolder = NoteDetailViewHolder(rootViewGroup = noteDetailMainContainer).apply {
            onBackClick = { onBackPressed() }
            onMenuClick = { openMenu() }
            onUndoClick = { viewModel.undo() }
            onPhotoClick = { photo -> openPhotoViewer(photo) }
            onPhotoRemoveClick = { photo -> viewModel.removePhoto(photo) }
            onTitleChange = { value: CharSequence? -> viewModel.editTitle(value) }.debounce(disposables)
            onTextChange = { value: CharSequence? -> viewModel.editText(value) }.debounce(disposables)

            onBind(viewModel.note)
        }

        with(viewModel) {
            observeSaveAndDeleteNote()
            observeExportNote()
            observeChangeNote()
            observeUndoNote()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!activityResults.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this@NoteDetailActivity.permissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        with(viewModel) {
            editTitle(viewHolder.title)
            editText(viewHolder.text)
            save()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    /** Открывает экран выбора директории. */
    private fun openSelectDirectory(action: Consumer<Uri?>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        if (intent.isSafe(context = this)) {
            activityResults.launch(
                intent = intent,
                onAccepted = { data ->
                    val outputFile = data?.data?.takePersistablePermission(this)
                    action(outputFile)
                },
                onDenied = { toast(R.string.note_detail_error_export_select_directory) }
            )
        } else {
            action(null)
        }
    }

    /** Открывает меню. */
    private fun openMenu(note: Note = viewModel.note) {
        val dialog = supportFragmentManager.findFragmentByTag(NoteDetailMenuDialog.TAG) as? NoteDetailMenuDialog
            ?: NoteDetailMenuDialog.newInstance(note).apply { show(supportFragmentManager, NoteDetailMenuDialog.TAG) }
        with(dialog) {
            onExport = { makeExport() }
            onRemove = { startupMode.makeRemove() }
            onTakePhoto = { takePhoto() }
            onPriorityChange = { viewModel.editPriority(value = it) }
        }
    }

    /** Открывает просмотр фотографии. */
    private fun openPhotoViewer(photo: Photo) {
        val intent = Intent(Intent.ACTION_VIEW, photo.uri).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (intent.isSafe(context = this)) {
            startActivity(Intent.createChooser(intent, getString(R.string.note_detail_label_photo_viewer_title)))
        } else {
            toast(R.string.note_detail_error_photo_viewer_no_app)
        }
    }

    /** Экспортирует заметку. */
    private fun makeExport() {
        permissions.requestThenRun(
            permissions = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            onAccepted = {
                openSelectDirectory { outputFile ->
                    viewModel.exportToFile(outputFile?.documentFile(this@NoteDetailActivity))
                }
            },
            onDenied = { toast(R.string.note_detail_error_export_no_permission) }
        )
    }

    /** Добавляет снимок к заметке. */
    private fun takePhoto(outputUri: Uri = viewModel.generatePhotoUri()) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        }
        if (intent.isSafe(context = this)) {
            activityResults.launch(
                intent = intent,
                onAccepted = { viewModel.addPhoto(outputUri) },
                onDenied = { /* Ignore */ }
            )
        } else {
            toast(R.string.note_detail_error_photo_no_app)
        }
    }

    /** Удаляет заметку. */
    private fun Mode.makeRemove() {
        when (this) {
            is Create -> router.exit()
            is Edit -> viewModel.delete()
        }
    }

    /** Подписывает наблюдателей к сохранению и удалению заметки. */
    private fun NoteDetailViewModel.observeSaveAndDeleteNote() {
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
        liveSaveNote.observe(this@NoteDetailActivity, observer)
        liveDeleteNote.observe(this@NoteDetailActivity, observer)
    }

    /** Подписывает наблюдателей к экспортированию заметки в файл. */
    private fun NoteDetailViewModel.observeExportNote() {
        val observer = Observer<Response<Unit>> { response ->
            when (response) {
                is Response.Progress -> viewHolder.isLocked = true
                is Response.Success -> {
                    viewHolder.isLocked = false
                    toast(R.string.note_detail_label_export_success)
                }
                is Response.Failure -> {
                    viewHolder.isLocked = false
                    toast(R.string.note_detail_error_export_unknown)
                    Timber.w(response.error)
                }
            }
        }
        liveExportToFile.observe(this@NoteDetailActivity, observer)
    }

    /** Подписывает наблюдателей к изменению заметки. */
    private fun NoteDetailViewModel.observeChangeNote() {
        val observer = Observer<Note> { viewHolder.onBind(data = it) }
        liveChangeNote.observe(this@NoteDetailActivity, observer)
    }

    /** Подписывает наблюдателей к отмене действию в заметке. */
    private fun NoteDetailViewModel.observeUndoNote() {
        val hasUndoObserver = Observer<Boolean> { viewHolder.isUndoVisibility = it }
        liveHasUndoNote.observe(this@NoteDetailActivity, hasUndoObserver)
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
                putExtra(EXTRA_MODE, Create())
            }
        }

        /** Создает новый экземпляр [Intent], для открытия экрана в режиме редактирования заметки. */
        fun newInstanceWithEditMode(context: Context, note: Note): Intent {
            return context.intentFor<NoteDetailActivity>().apply {
                putExtra(EXTRA_MODE, Edit(note))
            }
        }
    }
}
