package smartnotes.presentation.common.viewholder

import android.content.res.Resources
import android.view.View
import kotlinx.android.synthetic.main.particle_states_factory.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.presentation.common.viewholder.ViewHolder.State.Content
import smartnotes.presentation.common.viewholder.ViewHolder.State.Empty
import smartnotes.presentation.common.viewholder.ViewHolder.State.Error
import smartnotes.presentation.common.viewholder.ViewHolder.State.Progress
import smartnotes.presentation.share.widgets.empty.EmptyStateLayout
import smartnotes.presentation.share.widgets.error.ErrorStateLayout
import smartnotes.presentation.share.widgets.progress.ProgressStateLayout
import kotlin.properties.Delegates

/**
 * Обобщенное представление экрана.
 *
 * @property resources Ресурсы текущего представления.
 * @property progressLayout Представление индикатора активности.
 * @property emptyLayout Представление пустого состояния экрана.
 * @property errorLayout Представление состояния ошибки.
 * @property state Текущее состояние представления.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
open class ViewHolder(
    private val resources: Resources,
    private val progressLayout: ProgressStateLayout?,
    private val emptyLayout: EmptyStateLayout?,
    private val errorLayout: ErrorStateLayout?
) {

    constructor(view: View) : this(
        resources = view.resources,
        progressLayout = view.statesFactoryProgressStateView,
        emptyLayout = view.statesFactoryEmptyStateView,
        errorLayout = view.statesFactoryErrorStateView
    )

    var state by Delegates.observable<State>(Content) { _, _, newValue: State ->
        applyState(newValue)
    }

    init {
        state = Content
    }

    /**
     * Устанавливает заголовки для представления пустого экрана.
     *
     * @param title Заголовк.
     * @param subTitle Подзаголовок.
     */
    fun setEmptyContent(title: CharSequence?, subTitle: CharSequence? = null) {
        emptyLayout?.also {
            it.title = title
            it.subTitle = subTitle
        }
    }

    /**
     * Устанавливает заголовки для представления состояния ошибки.
     *
     * @param title Заголовк.
     * @param subTitle Подзаголовок.
     */
    private fun setErrorContent(title: CharSequence?, subTitle: CharSequence? = null) {
        errorLayout?.also {
            it.title = title
            it.subTitle = subTitle
        }
    }

    /**
     * Применяет состояние к представлению.
     *
     * @param state Состояние которого должно быть применено.
     */
    private fun applyState(state: State) {
        val (progressVisible, emptyVisible, errorVisible) = when (state) {
            Content -> Triple(first = false, second = false, third = false)
            Progress -> Triple(first = true, second = false, third = false)
            Empty -> Triple(first = false, second = true, third = false)
            is Error -> {
                setErrorContent(
                    title = resources.getString(R.string.label_error_unknown),
                    subTitle = state.error.localizedMessage
                )
                Triple(first = false, second = false, third = true)
            }
        }

        progressLayout?.isVisible = progressVisible
        emptyLayout?.isVisible = emptyVisible
        errorLayout?.isVisible = errorVisible
    }

    /**
     * Состояние представления.
     *
     * - [Content]
     * - [Progress]
     * - [Empty]
     * - [Error]
     */
    sealed class State {

        /** Состояние отображения осного контента. */
        object Content : State()

        /** Состояние индикатора активности. */
        object Progress : State()

        /** Состояние пустого экрана. */
        object Empty : State()

        /**
         * Состояние ошибки.
         *
         * @param error Возникшее исключение.
         */
        data class Error(val error: Throwable) : State()
    }
}
