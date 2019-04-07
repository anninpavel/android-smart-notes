package smartnotes.presentation.share.widgets.recyclerview.selection

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/** @author Pavel Annin (https://github.com/anninpavel). */
inline fun <reified T, U> createSelectionTracker(
    recyclerView: RecyclerView,
    provider: U,
    storageStrategy: StorageStrategy<T>,
    selectionId: String = UUID.randomUUID().toString(),
    @ItemKeyProvider.Scope scope: Int = ItemKeyProvider.SCOPE_CACHED,
    block: SelectionTracker.Builder<T>.() -> Unit = {}
): SelectionTracker<T> where U : SelectionItemKeyProvider<T> {
    return SelectionTracker.Builder<T>(
        selectionId,
        recyclerView,
        SimpleItemKeyProvider(scope, provider),
        SimpleItemDetailsLookup(recyclerView),
        storageStrategy
    ).apply(block).build()
}
