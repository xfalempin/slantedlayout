package com.ocito.slantedlayoutapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocito.slantedlayout.SlantedLayout

class MyAdapter(val items: List<Item>, val listener: (Item) -> Unit = {}) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(items[position], listener)
    }

}

class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Item, listener: (Item) -> Unit) = with(itemView) {
        setOnClickListener { listener(item) }
    }

}