package com.ocito.slantedlayout

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.roundToInt


/**
 * slantSize and margin must be in pixels
 */
fun RecyclerView.addSlantItemDecoration(@RecyclerView.Orientation orientation:  Int, slantSize: Float, margin: Float) {
    addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect?, view: View, parent: RecyclerView?, state: RecyclerView.State?) {
                if(parent?.getChildAdapterPosition(view) != 0) {
                    val px = (margin - slantSize).roundToInt()
                    when(orientation)
                    {
                        LinearLayoutManager.VERTICAL -> outRect?.top = px
                        LinearLayoutManager.HORIZONTAL -> outRect?.left = px
                    }
                }
            }
        })
}