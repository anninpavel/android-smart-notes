package smartnotes.data.mapper

/**
 * Базовый интерфейс конвертора.
 *
 * @param IN Входной тип данных.
 * @param OUT Выходной тип данных.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface BaseMapper<IN, OUT> {

    /** Конвертирует из [IN] в [OUT]. */
    fun from(value: IN): OUT

    /** Конвертирует из [OUT] в [IN]. */
    fun to(value: OUT): IN
}
