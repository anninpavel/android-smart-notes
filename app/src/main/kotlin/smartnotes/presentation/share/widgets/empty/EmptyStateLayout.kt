package smartnotes.presentation.share.widgets.empty

/**
 * Макет состояния пустого представления.
 *
 * @property isVisible Флаг, определяющий отображение макета на представлении.
 * @property title Заголовок отображаемый на представлении.
 * @property subTitle Подзаголовок отображаемый на представлении.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface EmptyStateLayout {

    var isVisible: Boolean
    var title: CharSequence?
    var subTitle: CharSequence?
}
