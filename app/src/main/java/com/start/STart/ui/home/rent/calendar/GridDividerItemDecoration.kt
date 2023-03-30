package com.start.STart.ui.home.rent.calendar

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridDividerItemDecoration(
    private val dividerSize: Int,
    private val dividerColor: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount

        // View가 RecyclerView의 첫 번째 행인 경우
        if (position < spanCount) {
            outRect.top = dividerSize
        }
        outRect.bottom = dividerSize
        // View가 RecyclerView의 마지막 열인 경우
        if ((position + 1) % spanCount == 0) {
            outRect.right = dividerSize
        } else {
            outRect.right = dividerSize / 2
        }
        // View가 RecyclerView의 첫 번째 열인 경우
        if (position % spanCount == 0) {
            outRect.left = dividerSize
        } else {
            outRect.left = dividerSize / 2
        }
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val spanCount = layoutManager.spanCount
            val column = position % spanCount

            // View가 RecyclerView의 첫 번째 행이 아닌 경우
            if (position >= spanCount) {
                // top divider 그리기
                drawTopDivider(c, view, layoutManager, column)
            }
            // bottom divider 그리기
            drawBottomDivider(c, view, layoutManager, column)

            // View가 RecyclerView의 마지막 열이 아닌 경우
            if ((position + 1) % spanCount != 0) {
                // right divider 그리기
                drawRightDivider(c, view, layoutManager, column)
            }
            // left divider 그리기
            drawLeftDivider(c, view, layoutManager, column)
        }
    }

    private fun drawLeftDivider(
        c: Canvas,
        view: View,
        layoutManager: GridLayoutManager,
        column: Int
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val right = view.left - params.leftMargin
        val left = right - dividerSize
        val top = view.top - params.topMargin - if (column == 0) dividerSize else dividerSize / 2
        val bottom = view.bottom + params.bottomMargin + if (column == 0) dividerSize else dividerSize / 2
        drawDivider(c, left, top, right, bottom, layoutManager, column)
    }

    private fun drawTopDivider(
        c: Canvas,
        view: View,
        layoutManager: GridLayoutManager,
        column: Int
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val left = view.left - params.leftMargin - dividerSize
        val right = view.right + params.rightMargin + dividerSize
        val top = view.top - params.topMargin - dividerSize
        val bottom = top + dividerSize
        drawDivider(c, left, top, right, bottom, layoutManager, column)
    }

    private fun drawBottomDivider(
        c: Canvas,
        view: View,
        layoutManager: GridLayoutManager,
        column: Int
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val left = view.left - params.leftMargin - dividerSize
        val right = view.right + params.rightMargin + dividerSize
        val top = view.bottom + params.bottomMargin
        val bottom = top + dividerSize
        drawDivider(c, left, top, right, bottom, layoutManager, column)
    }

    private fun drawRightDivider(
        c: Canvas,
        view: View,
        layoutManager: GridLayoutManager,
        column: Int
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val left = view.right + params.rightMargin
        val right = left + dividerSize
        val top =
            view.top - params.topMargin - if (column == layoutManager.spanCount - 1) dividerSize else dividerSize / 2
        val bottom =
            view.bottom + params.bottomMargin + if (column == layoutManager.spanCount - 1) dividerSize else dividerSize / 2
        drawDivider(c, left, top, right, bottom, layoutManager, column)
    }

    private fun drawDivider(
        c: Canvas,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        layoutManager: GridLayoutManager,
        column: Int
    ) {
        val paint = Paint().apply { color = dividerColor }

        // 외부 divider일 경우 실선 그리기
        if (column == 0) {
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        } else {
            // 내부 divider일 경우 점선 그리기
            val path = Path()
            path.moveTo(left.toFloat(), top.toFloat())
            path.lineTo(left.toFloat(), bottom.toFloat())
            val effect = DashPathEffect(floatArrayOf(dividerSize.toFloat(), dividerSize.toFloat()), 0f)
            paint.pathEffect = effect
            c.drawPath(path, paint)
        }
    }
}