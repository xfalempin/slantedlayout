package com.ocito.slantedlayout

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View


fun RecyclerView.addSlantItemDecoration(@RecyclerView.Orientation orientation:  Int, slantSize: Float, margin: Float) {
    addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect?, view: View, parent: RecyclerView?, state: RecyclerView.State?) {
                if(parent?.getChildAdapterPosition(view) != 0) {
                    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin - slantSize, resources.displayMetrics).toInt()
                    when(orientation)
                    {
                        LinearLayoutManager.VERTICAL -> outRect?.top = px
                        LinearLayoutManager.HORIZONTAL -> outRect?.left = px
                    }
                }
            }
        })
}