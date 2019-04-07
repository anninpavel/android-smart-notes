package smartnotes.presentation.screens.notes

import android.content.Context
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_notes.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.domain.values.ViewType
import smartnotes.presentation.common.viewholder.ViewHolder
import smartnotes.presentation.share.widgets.recyclerview.selection.createSelectionTracker
import smartnotes.utils.extensions.isNull
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer
import smartnotes.utils.kotlin.Supplier
import smartnotes.utils.kotlin.weak

/**
 * Представление экрана "Список заметок".
 *
 * @param viewTypeSupplier Поставщик типа отображения списка заметок.
 *
 * @property notesTracker Трекер выбора заметок.
 * @property notesAdapter Адаптер представления списка заметок.
 * @property actionMode Режим выбора действия.
 * @property viewType Тип отображения списка заметок.
 * @property selectedCount Количество выбранных элементов отображаемое на представлении.
 *
 * @property onNoteClick Событие, выбрана заметка.
 * @property onCreateClick Событие, создание новой заметки.
 * @property onRemoveClick Событие, удаление выбранных заметок.
 * @property onViewTypeChange Событие, изменен тип отображения списка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesViewHolder(
    private val rootViewGroup: ViewGroup,
    viewTypeSupplier: Supplier<ViewType>
) : ViewHolder(rootViewGroup) {

    private val notesTracker: SelectionTracker<Note>
    private val notesAdapter = NotesAdapter(viewType = ViewType.GRID)
    private var actionMode by weak<ActionMode>()

    private var viewType: ViewType
        get() = notesAdapter.viewType
        set(value) {
            onViewTypeChange?.invoke(value)
            rootViewGroup.notesRecyclerView.layoutManager = value.layoutManager(rootViewGroup.context)
            notesAdapter.viewType = value
            with(rootViewGroup.notesBottomAppBar) {
                menu?.findItem(R.id.notesListAction)?.isVisible = value != ViewType.LIST
                menu?.findItem(R.id.notesGridAction)?.isVisible = value != ViewType.GRID
            }
        }

    private var selectedCount: Int = 0
        set(value) {
            field = value
            actionMode?.title = value.toString()
        }

    var onNoteClick: Consumer<Note>? = null
    var onCreateClick: Action? = null
    var onRemoveClick: Consumer<List<Note>>? = null
    var onViewTypeChange: Consumer<ViewType>? = null

    init {
        with(rootViewGroup.notesBottomAppBar) {
            menu?.clear()
            inflateMenu(R.menu.notes_bottom)
        }

        viewType = viewTypeSupplier()
        rootViewGroup.notesRecyclerView.adapter = notesAdapter
        notesTracker = createSelectionTracker(
            rootViewGroup.notesRecyclerView,
            notesAdapter,
            StorageStrategy.createParcelableStorage(Note::class.java)
        ) {
            withOnItemActivatedListener { item, _ ->
                item.selectionKey?.let { note -> onNoteClick?.invoke(note) }
                return@withOnItemActivatedListener true
            }
        }
        setEmptyContent(
            title = rootViewGroup.resources.getString(R.string.notes_label_empty_title),
            subTitle = rootViewGroup.resources.getString(R.string.notes_label_empty_subtitle)
        )

        notesAdapter.isSelectedPredicate = { note -> notesTracker.isSelected(note) }
        rootViewGroup.notesCreateFloatingActionButton.setOnClickListener { onCreateClick?.invoke() }
        rootViewGroup.notesBottomAppBar.setOnMenuItemClickListener { menuItem ->
            viewType = when (menuItem.itemId) {
                R.id.notesListAction -> ViewType.LIST
                R.id.notesGridAction -> ViewType.GRID
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }

        notesTracker.addObserver(object : SelectionTracker.SelectionObserver<Note>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                if (notesTracker.hasSelection() && !actionMode.isAvailable()) {
                    startActionMode()
                    selectedCount = notesTracker.selection.count()
                } else if (!notesTracker.hasSelection()) {
                    exitActionMode()
                } else {
                    selectedCount = notesTracker.selection.count()
                }
            }
        })
    }

    /** Этот метод вызывается для обновления контента представления. */
    fun onBind(data: List<Note>?) {
        notesAdapter.submitList(data)
        state = if (data.isNullOrEmpty()) State.Empty else State.Content
    }

    /** Запускает режим выбора действия. */
    private fun startActionMode(tracker: SelectionTracker<Note> = notesTracker) {
        actionMode = with(rootViewGroup.notesAppBarLayout) {
            val controller = ActionModeController(tracker, onRemoveClick, onDestroyed = { actionMode = null })
            return@with startActionMode(controller)
        }
    }

    /** Выходит из режима выбора действия. */
    private fun exitActionMode() {
        actionMode?.finish()

    }

    /** Возращает состояние достумности режима выбора действия. */
    private fun ActionMode?.isAvailable(): Boolean {
        return !isNull()
    }

    /**
     * Возвращает [RecyclerView.LayoutManager] в зависимости от [ViewType].
     *
     * @param context Контекст текущего представления.
     *
     * @receiver [ViewType]
     */
    private fun ViewType.layoutManager(context: Context): RecyclerView.LayoutManager {
        return when (this) {
            ViewType.LIST -> LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            ViewType.GRID -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    /**
     * Контроллер режима выбора действия.
     *
     * @property tracker Трекер выбора заметок.
     * @property onRemoveClick Событие, удаление выбранных заметок.
     * @property onDestroyed Событие, завершение жизненого цикла контроллера.
     *
     * @author Pavel Annin (https://github.com/anninpavel).
     */
    private class ActionModeController(
        private val tracker: SelectionTracker<Note>,
        private val onRemoveClick: Consumer<List<Note>>?,
        private val onDestroyed: Action? = null
    ) : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            mode.menuInflater.inflate(R.menu.notes_top_action_mode, menu)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.notesRemoveAction -> {
                    onRemoveClick?.invoke(tracker.selection.toList())
                    mode.finish()
                }
                else -> return false
            }
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            tracker.clearSelection()
            onDestroyed?.invoke()
        }
    }
}
