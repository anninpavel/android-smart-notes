package smartnotes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import smartnotes.data.local.converters.CommonConverter
import smartnotes.data.local.dao.NoteDao
import smartnotes.data.local.dao.NoteWithPhotosDao
import smartnotes.data.local.dao.PhotoDao
import smartnotes.data.local.entities.NoteEntity
import smartnotes.data.local.entities.PhotoEntity

/**
 * Источник предоставляющий доступ к БД.
 *
 * Версионирование:
 * - [1]: Добавлена сущность: [NoteEntity], [PhotoEntity] (v0.1.0.0).
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Database(
    entities = [NoteEntity::class, PhotoEntity::class ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    value = [CommonConverter::class]
)
abstract class DatabaseSource : RoomDatabase() {

    /** @see NoteDao */
    abstract fun notesDao(): NoteDao

    /** @see PhotoDao */
    abstract fun photosDao(): PhotoDao

    /** @see NoteWithPhotosDao */
    abstract fun noteWithPhotosDao(): NoteWithPhotosDao

    companion object {

        /**
         * Создает новый экхемпляр [DatabaseSource].
         *
         * @param context Контекст базы данных (применим контекст приложения.)
         *
         * @return Новый экземпляр [DatabaseSource].
         */
        fun create(context: Context): DatabaseSource {
            return Room.databaseBuilder(context, DatabaseSource::class.java, "smart-note.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
