@file:JvmMultifileClass

package smartnotes.utils.kotlin

/**
 * Функциональный интерфейс (коллбек), который возвращает значение.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
typealias Supplier<T> = () -> T

/**
 * Функциональный интерфейс (коллбек), возвращающий `true` или `false` для заданного входного значения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
typealias Predicate<T> = (T) -> Boolean

/**
 * Функциональный интерфейс (коллбек), который принимает одно значение.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
typealias Consumer<T> = (T) -> Unit

/**
 * Функциональный интерфейс (коллбек), не имеющий входного и выходного значения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
typealias Action = () -> Unit
