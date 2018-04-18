package com.ocito.slantedlayoutapp

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect?, view: View, parent: RecyclerView?, state: RecyclerView.State?) {
                if(parent?.getChildAdapterPosition(view) != 0)
                    outRect?.top = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,-38.0f, resources.displayMetrics).toInt()
            }
        })
        val items = ArrayList<Item>()
        for (i in 0..49) {
            items.add(Item())
        }
        recyclerView.adapter = MyAdapter(items)
    }
}
