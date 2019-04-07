package smartnotes.domain.usecase

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import smartnotes.domain.models.Note
import smartnotes.domain.repository.NoteRepository
import smartnotes.presentation.usecase.NoteUseCase
import smartnotes.utils.rx.SchedulerFacade

/**
 * Реализация [NoteUseCase].
 *
 * @property notes Репозиторий взаимодействия с заметками.
 * @property schedulers Планировщик.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteUseCaseImpl(
    private val notes: NoteRepository,
    private val schedulers: SchedulerFacade
) : NoteUseCase {

    override fun liveFetchAll(): LiveData<List<Note>> {
        return notes.liveFetchAll()
    }

    override fun create(value: Note): Completable {
        return notes.create(value)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun edit(value: Note): Completable {
        return notes.edit(value)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun delete(value: Note): Completable {
        return notes.delete(value)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun delete(values: List<Note>): Completable {
        return notes.delete(values)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }
}
