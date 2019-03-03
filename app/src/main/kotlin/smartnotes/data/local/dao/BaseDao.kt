package smartnotes.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Базовый DAO, содержащий базовые функции.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface BaseDao<in T> {

    /**
     * Добавляет [elements] в базу данных.
     * Если один или несколько экземпляров уже созданы (проверка по первичным ключам),
     * записи в базе данных будут заменены.
     *
     * @param elements Коллекция экземляров, подготовленных для добавления.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elements: T)

    /**
     * Добавляет [elements] в базу данных.
     * Если один или несколько экземпляров уже созданы (проверка по первичным ключам),
     * записи в базе данных будут заменены.
     *
     * @param elements Коллекция экземляров, подготовленных для добавления.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(elements: List<T>)

    /**
     * Добавляет [element] в базу данных.
     * Если экземпляр уже создан (проверка по первичным ключам), запись в базе данных будет заменена.
     *
     * @param element Экземляров, подготовленный для добавления.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: T): Long

    /**
     * Обновляет [element] в базе данных, если экземпляр уже существуют (проверка по первичным ключам).
     * Если экземпляр еще не существует в базе данных, операция будет проигнорирована.
     *
     * @param element Экземляров, подготовленный для обновления.
     */
    @Update
    fun update(element: T): Int

    /**
     * Удаляет [element] в базе данных.
     *
     * @param element Экземляров, представленных к удалению.
     */
    @Delete
    fun delete(element: T): Int

    /**
     * Удаляет все [elements] в базе данных.
     *
     * @param elements Коллекция экземляров, представленных к удалению.
     */
    @Delete
    fun deleteAll(vararg elements: T)

    /**
     * Удаляет все [elements] в базе данных.
     *
     * @param elements Коллекция экземляров, представленных к удалению.
     */
    @Delete
    fun deleteAll(elements: List<T>)
}
