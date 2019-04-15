package smartnotes.presentation.share.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Декоратор, добавляющий отступы для списокв.
 *
 * @property beforeOffset Отступ перед элементом(в пикселях).
 * @property afterOffset Отступ после элемена(в пикселях).
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ItemOffsetDecoration(
    private val beforeOffset: Int = 0,
    private val afterOffset: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        when (val layoutManager = parent.layoutManager) {
            is LinearLayoutManager -> layoutManager.itemOffsets(position, outRect)
            else -> throw IllegalArgumentException("Unknown type layout manager: $layoutManager")
        }
    }

    private fun LinearLayoutManager.itemOffsets(position: Int, outRect: Rect) {
        val (before, after) = when (position) {
            0 -> 0 to afterOffset
            itemCount - 1 -> beforeOffset to 0
            else -> beforeOffset to afterOffset
        }

        with(outRect) {
            when (orientation) {
                LinearLayoutManager.HORIZONTAL -> {
                    left = before
                    right = after
                }
                LinearLayoutManager.VERTICAL -> {
                    top = before
                    bottom = after
                }
            }
        }
    }
}
