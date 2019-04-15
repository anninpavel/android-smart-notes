package smartnotes.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.Maybe
import smartnotes.data.local.DatabaseSource
import smartnotes.data.local.entities.NoteEntity
import smartnotes.data.mapper.Mapper
import smartnotes.domain.models.Note
import smartnotes.domain.models.NoteId
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
    private val mapper: Mapper<Note, NoteEntity>
) : NoteRepository {

    override fun liveFetchAll(): LiveData<List<Note>> {
        return Transformations.map(db.noteDao().liveAllOrderByCreatedDesc()) { notes ->
            return@map notes.map { mapper.to(value = it) }
        }
    }

    override fun findById(value: NoteId): Maybe<Note> {
        return Maybe.fromCallable { db.noteDao().byId(value = value.value.toString()) }
            .map { mapper.to(value = it) }
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

    override fun delete(values: List<Note>): Completable {
        return Completable.fromAction {
            val elements = values.map { mapper.from(value = it) }.toList()
            db.noteDao().deleteAll(elements)
        }
    }
}
