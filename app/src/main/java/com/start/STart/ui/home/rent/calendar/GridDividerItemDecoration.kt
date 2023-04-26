package com.start.STart.ui.home.rent.calendar

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
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
        val itemCount = parent.adapter?.itemCount?:0

        val isTopRow = position < spanCount
        val isBottomRow = position >= itemCount - spanCount
        val isLeftColumn = position % spanCount == 0
        val isRightColumn = (position + 1) % spanCount == 0

        outRect.top = if (isTopRow) dividerSize else dividerSize / 2
        outRect.bottom = if (isBottomRow) 0 else dividerSize / 2
        outRect.left = if (isLeftColumn) 0 else dividerSize / 2
        outRect.right = if (isRightColumn) 0 else dividerSize / 2

    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager
        val itemCount = parent.adapter?.itemCount?:0

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val spanCount = layoutManager.spanCount

            val column = position % spanCount

            drawTopDivider(c, view, layoutManager, column)
            drawBottomDivider(c, view, layoutManager, column)
            drawLeftDivider(c, view, layoutManager, column)
            drawRightDivider(c, view, layoutManager, column)
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
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
    }
}