package com.ocito.slantedlayoutapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = ArrayList<Item>()
        for (i in 0..49) {
            items.add(Item())
        }
        recyclerView.adapter = MyAdapter(items)
    }
}
