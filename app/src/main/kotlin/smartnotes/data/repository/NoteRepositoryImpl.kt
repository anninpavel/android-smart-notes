package smartnotes.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.Maybe
import smartnotes.data.converters.NoteConverter
import smartnotes.data.converters.PhotoConverter
import smartnotes.data.local.DatabaseSource
import smartnotes.domain.models.Note
import smartnotes.domain.models.NoteId
import smartnotes.domain.repository.NoteRepository

/**
 * Реализация хранилища данных [NoteRepository].
 *
 * @property db Экземпляр базы данных.
 * @property noteConverter Конвертор заметок.
 * @property photoConverter Конвертор снимков.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteRepositoryImpl(
    private val db: DatabaseSource,
    private val noteConverter: NoteConverter,
    private val photoConverter: PhotoConverter
) : NoteRepository {

    override fun liveFetchAll(): LiveData<List<Note>> {
        return Transformations.map(db.noteWithPhotosDao().liveAllOrderByCreatedDesc()) { notes ->
            return@map notes.map { noteConverter.convert(value = it) }
        }
    }

    override fun findById(value: NoteId): Maybe<Note> {
        return Maybe.fromCallable { db.notesDao().byId(value = value.value.toString()) }
            .map { noteConverter.convert(value = it) }
    }

    override fun create(value: Note): Completable {
        return Completable.fromAction {
            db.notesDao().insert(element = noteConverter.convert(value))
            db.photosDao().insertAll(elements = value.photos.map { photoConverter.convert(it, value.id) })
        }
    }

    override fun edit(value: Note): Completable {
        return Completable.fromAction {
            db.notesDao().update(element = noteConverter.convert(value))
            db.photosDao().swap(elements = value.photos.map { photoConverter.convert(it, value.id) })
        }
    }

    override fun delete(value: Note): Completable {
        return Completable.fromAction { db.notesDao().delete(element = noteConverter.convert(value)) }
    }

    override fun delete(values: List<Note>): Completable {
        return Completable.fromAction {
            val elements = values.map { noteConverter.convert(value = it) }.toList()
            db.notesDao().deleteAll(elements)
        }
    }
}
