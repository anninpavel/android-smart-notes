package smartnotes.presentation.share.widgets.progress

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.github.anninpavel.smartnotes.R
import androidx.core.view.isVisible as isViewVisible

/**
 * Представление индикатора активности.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ProgressStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ProgressStateLayout {

    override var isVisible: Boolean
        get() = isViewVisible
        set(value) {
            isViewVisible = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_progress_state, this, true)
    }
}
