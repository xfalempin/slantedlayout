package com.ocito.slantedlayoutapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import com.ocito.slantedlayout.SlantedLayout
import com.ocito.slantedlayout.addSlantItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val orientation = (recyclerView.layoutManager as LinearLayoutManager).orientation
        recyclerView.post({
            recyclerView.addSlantItemDecoration(orientation, (recyclerView.getChildAt(0) as SlantedLayout).slantedSize,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,2.0f,resources.displayMetrics))
        })

        val items = ArrayList<Item>()
        for (i in 0..49) {
            items.add(Item())
        }
        recyclerView.adapter = MyAdapter(items)
    }
}

