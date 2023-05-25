package com.example.investingguideandroidui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.investingguideandroidui.models.Security


class SecuritiesViewActivity : AppCompatActivity() {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit  var securities_recycler_view: RecyclerView
    private lateinit var securities: ArrayList<Security>
    private lateinit var myOnClickListener: View.OnClickListener
    private lateinit var removedItems: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_securities_view)
        securities_recycler_view = findViewById<RecyclerView>(R.id.securities_recycler_view)
        securities = ArrayList<Security>()

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        securities_recycler_view.layoutManager = layoutManager
    }

    fun setData(data: ArrayList<Security>) {
        securities = data
    }



}