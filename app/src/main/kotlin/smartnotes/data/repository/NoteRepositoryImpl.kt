package smartnotes.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import smartnotes.data.local.DatabaseSource
import smartnotes.data.mapper.NoteMapper
import smartnotes.domain.models.Note
import smartnotes.domain.repository.NoteRepository

/**
 * Реализация хранилища данных [NoteRepository].
 *
 * @property db Экземпляр базы данных.
 * @property mapper Конвертор заметок.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteRepositoryImpl(
    private val db: DatabaseSource,
    private val mapper: NoteMapper
) : NoteRepository {

    override fun liveFetchAll(): LiveData<List<Note>> {
        return Transformations.map(db.noteDao().liveAllOrderByCreatedDesc()) { notes ->
            return@map notes.map { mapper.to(value = it) }
        }
    }

    override fun create(value: Note): Completable {
        return Completable.fromAction { db.noteDao().insert(element = mapper.from(value)) }
    }

    override fun edit(value: Note): Completable {
        return Completable.fromAction { db.noteDao().update(element = mapper.from(value)) }
    }

    override fun delete(value: Note): Completable {
        return Completable.fromAction { db.noteDao().delete(element = mapper.from(value)) }
    }
}
