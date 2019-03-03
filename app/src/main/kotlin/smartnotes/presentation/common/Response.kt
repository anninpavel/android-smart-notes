package smartnotes.presentation.common

/**
 * Wrapper результата, с поддержкой состояний.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
sealed class Response<out T> {

    /**
     * Состояние успешной обработки операции.
     *
     * @property value Результат выполнения операции.
     */
    data class Success<out T>(val value: T) : Response<T>()

    /**
     * Состояние ошибки.
     *
     * @property error Ошибка полученая при обработке операции.
     */
    data class Failure<out T>(val error: Throwable) : Response<T>()

    /** Состояние обработки операции. */
    class Progress<out T> : Response<T>()

    companion object {

        /** Инициализирует [Success]. */
        fun <T> success(value: T) = Success(value)

        /** Инициализирует [Failure]. */
        fun <T> failure(error: Throwable) = Failure<T>(error)

        /** Инициализирует [Progress]. */
        fun <T> loading() = Progress<T>()
    }
}
