package com.ocito.slantedlayout

import android.support.v7.widget.RecyclerView

abstract class SlantedAdapter<T:RecyclerView.ViewHolder>: RecyclerView.Adapter<T>() {

    /**
     * Call this method to ignore the first item and last item slant
     */
    fun ignoreFirstAndLastSlant(holder: T, position: Int) {

        val slanted = (holder.itemView as SlantedLayout?)
        slanted?.let {
            if(position == 0)
            {
                slanted.slantIgnoreFlag = slanted.TOP or slanted.LEFT
            }
            else if (position == itemCount - 1)
            {
                slanted.slantIgnoreFlag = slanted.BOTTOM or slanted.RIGHT
            }
            else
            {
                slanted.slantIgnoreFlag = slanted.NONE
            }
        }
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        ignoreFirstAndLastSlant(holder, position)
    }

}