package smartnotes.presentation.share.widgets.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.widget_error_state.view.*
import ru.github.anninpavel.smartnotes.R
import androidx.core.view.isVisible as isViewVisible

/**
 * Представление состояния ошибки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ErrorStateLayout {

    override var isVisible: Boolean
        get() = isViewVisible
        set(value) {
            isViewVisible = value
        }

    override var title: CharSequence?
        get() = errorStateTitleTextView.text
        set(value) = with(errorStateTitleTextView) {
            text = value
            isGone = value.isNullOrBlank()
        }

    override var subTitle: CharSequence?
        get() = errorStateSubTitleTextView.text
        set(value) = with(errorStateSubTitleTextView) {
            text = value
            isGone = value.isNullOrBlank()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_error_state, this, true)
    }
}
