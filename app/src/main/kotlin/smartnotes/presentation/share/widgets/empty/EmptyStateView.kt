package smartnotes.presentation.share.widgets.empty

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.widget_empty_state.view.*
import ru.github.anninpavel.smartnotes.R
import androidx.core.view.isVisible as isViewVisible

/**
 * Представление пустого состояния.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class EmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), EmptyStateLayout {

    override var isVisible: Boolean
        get() = isViewVisible
        set(value) {
            isViewVisible = value
        }

    override var title: CharSequence?
        get() = emptyStateTitleTextView.text
        set(value) = with(emptyStateTitleTextView) {
            text = value
            isGone = value.isNullOrBlank()
        }

    override var subTitle: CharSequence?
        get() = emptyStateSubTitleTextView.text
        set(value) = with(emptyStateSubTitleTextView) {
            text = value
            isGone = value.isNullOrBlank()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_empty_state, this, true)
    }
}
