@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import smartnotes.di.common.ViewModelFactory
import smartnotes.di.common.ViewModelKey
import smartnotes.presentation.screens.note.NoteDetailViewModel
import smartnotes.presentation.screens.notes.NotesViewModel

/** @author Pavel Annin (https://github.com/anninpavel). */
@Module
interface ViewModelBindingModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(value = NotesViewModel::class)
    fun bindNotesViewModel(viewModel: NotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = NoteDetailViewModel::class)
    fun bindNoteDetailViewModel(viewModel: NoteDetailViewModel): ViewModel
}
