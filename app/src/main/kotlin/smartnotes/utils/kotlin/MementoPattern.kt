@file:JvmMultifileClass

package smartnotes.utils.kotlin

import java.util.*

/**
 * Интерфейс снимка состояния объекта.
 *
 * @property value Экземпляр сохраняемого состояния объекта.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface Memento<out T> {

    val value: T
}

/**
 * Реализация интерфейса [Memento].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
data class MementoSnapshot<out T>(override val value: T) : Memento<T>

/**
 * Интерфейс создателя снимков состояния объекта.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface MementoOriginator<T> {

    /** Возвращает снимок состояния объекта. */
    fun save(): Memento<T>

    /** Востанавливает состояние объекта. */
    fun restore(state: Memento<T>)
}

/**
 * "Опекун" хранит историю снимков состояний объекта, с возможностью востановления.
 *
 * @property history История снимков состояния объекта.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class MementoCaretaker<out T>(private val originator: MementoOriginator<T>) {

    private val history = Stack<Memento<T>>()

    /** Сохраняет текущее состояние объекта. */
    fun save() {
        history.push(originator.save())
    }

    /** Востанавливает предыдущее состояние объекта. */
    fun undo() {
        return originator.restore(history.pop())
    }

    /** Возвращает `true` если возможно откатить состояние, иначе `false`. */
    fun hasUndo(): Boolean {
        return history.count() > 0
    }
}
