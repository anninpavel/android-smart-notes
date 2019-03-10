package smartnotes.presentation.share.widgets.error

/**
 * Макет состояния ошибки на представлении.
 *
 * @property isVisible Флаг, определяющий отображение макета на представлении.
 * @property title Заголовок отображаемый на представлении.
 * @property subTitle Подзаголовок отображаемый на представлении.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface ErrorStateLayout {

    var isVisible: Boolean
    var title: CharSequence?
    var subTitle: CharSequence?
}
