package com.ocito.slantedlayoutapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ocito.slantedlayout.addSlantItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val orientation = (recyclerView.layoutManager as LinearLayoutManager).orientation
        recyclerView.addSlantItemDecoration(orientation, 40.0f ,2.0f)

        val items = ArrayList<Item>()
        for (i in 0..49) {
            items.add(Item())
        }
        recyclerView.adapter = MyAdapter(items)
    }
}

