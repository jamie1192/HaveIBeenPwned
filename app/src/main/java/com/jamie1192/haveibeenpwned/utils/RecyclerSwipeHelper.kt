package com.jamie1192.haveibeenpwned.utils

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jamie1192.haveibeenpwned.storedEmails.UserEmailAdapter

/**
 * Created by jamie1192 on 13/1/19.
 */
class RecyclerSwipeHelper(dragDirs : Int, swipeDirs : Int, private val listener: SwipeListener)
                    : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregoundView = (viewHolder as UserEmailAdapter.EmailViewHolder).foreground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregoundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as UserEmailAdapter.EmailViewHolder).foreground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    interface SwipeListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, dir : Int, pos : Int)
    }
}


