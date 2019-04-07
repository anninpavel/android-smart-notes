@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import dagger.Module
import dagger.Provides
import smartnotes.data.cache.PreferenceSource
import smartnotes.domain.repository.NoteRepository
import smartnotes.domain.usecase.NoteUseCaseImpl
import smartnotes.domain.usecase.UserUseCaseImpl
import smartnotes.presentation.usecase.NoteUseCase
import smartnotes.presentation.usecase.UserUseCase
import smartnotes.utils.rx.SchedulerFacade
import javax.inject.Singleton

/** @author Pavel Annin (https://github.com/anninpavel). */
@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun provideNoteUseCase(notes: NoteRepository, schedulers: SchedulerFacade): NoteUseCase {
        return NoteUseCaseImpl(notes, schedulers)
    }

    @Singleton
    @Provides
    fun provideUserUseCase(preference: PreferenceSource): UserUseCase {
        return UserUseCaseImpl(preference)
    }
}
