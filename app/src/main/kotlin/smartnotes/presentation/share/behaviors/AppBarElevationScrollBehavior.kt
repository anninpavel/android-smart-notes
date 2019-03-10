package smartnotes.presentation.share.behaviors

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import ru.github.anninpavel.smartnotes.R
import smartnotes.utils.kotlin.weak

/**
 * Описывает поведение [AppBarLayout], при прокручивание по вертикали.
 * При прокручивание добавляет тень к [AppBarLayout].
 *
 * Пример использования:
 * ```
 * app:layout_behavior="@string/behavior_appbar_elevation_scroll"
 * ```
 *
 * @property totalDy Общее число прокрученных пикселей.
 * @property isElevated Флаг определяющий отображение тени
 * @property appBarLayout Кешированный [AppBarLayout], для применения описанного поведения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@SuppressLint(value = ["PrivateResource", "unused"])
class AppBarElevationScrollBehavior @JvmOverloads constructor(
    context: Context? = null,
    attributes: AttributeSet? = null
) : AppBarLayout.ScrollingViewBehavior(context, attributes), View.OnLayoutChangeListener {

    private var totalDy = 0
    private var isElevated: Boolean = false
    private var appBarLayout by weak<AppBarLayout>()

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        parent.addOnLayoutChangeListener(this)
        appBarLayout = child as? AppBarLayout
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (target.isScrollable(child)) {
            totalDy += dy
            if (totalDy <= 0) {
                if (isElevated) child.elevation(value = 0.0f)
                totalDy = 0
                isElevated = false
            } else {
                if (!isElevated) child.elevation(value = child.resources.getDimension(R.dimen.design_appbar_elevation))
                if (totalDy > target.bottom) totalDy = target.bottom
                isElevated = true
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        totalDy = 0
        isElevated = false
        appBarLayout?.elevation(value = 0.0f)
    }

    /**
     * Устанавнивает высоту представления, с возможностью анимированного изменения.
     *
     * @param value Устанавливаемое значение высоты.
     * @param animated Флаг, определющий анимированное изменение высоты.
     * @param duration Длительность анимированного изменения высоты.
     *
     * @receiver [View]
     */
    private fun View.elevation(
        value: Float,
        animated: Boolean = true,
        duration: Long = resources.getInteger(R.integer.app_bar_elevation_anim_duration).toLong()
    ) {
        if (animated) {
            ObjectAnimator.ofFloat(this, "elevation", value).apply {
                this.duration = duration
            }.start()
        } else {
            elevation = value
        }
    }

    /**
     * Возвращает `true`, если [View] может прокручиваться.
     *
     * @param child Дочернее представление.
     *
     * @receiver [View]
     *
     * @return Возвращает `true`, если [View] может прокручиваться, иначе `false`.
     */
    private fun View.isScrollable(child: View): Boolean {
        return if (this is ScrollingView) {
            computeVerticalScrollRange() > height - child.height
        } else {
            false
        }
    }
}
