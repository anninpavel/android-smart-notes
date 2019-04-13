package smartnotes.presentation.usecase

import androidx.documentfile.provider.DocumentFile
import smartnotes.domain.values.ViewType

/**
 * Интерфейс UseCase взаимодействия с пользовательскими данными.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface UserUseCase {

    /** Запрашивает тип отображения списков. */
    fun fetchViewType(): ViewType

    /**
     * Сохраняет выбранный тип отображения списков.
     *
     * @param value Сохраняемый тип отоюражения списков.
     */
    fun saveViewType(value: ViewType)

    /**
     * Возвращает директорию для экспорта данных.
     *
     * @param desiredDirectory Желаемый каталог для экспорта (опционально).
     */
    fun exportDirectory(desiredDirectory: DocumentFile? = null): DocumentFile
}
