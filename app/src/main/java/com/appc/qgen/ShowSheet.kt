package com.appc.qgen

import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class ShowSheet : AppCompatActivity() {
    private lateinit var gridView: GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_sheet)
        supportActionBar!!.hide()
        gridView = findViewById(R.id.GridView)

        val fN: ArrayList<Int> = intent.getSerializableExtra("firstNumbers") as ArrayList<Int>
        val sN: ArrayList<Int> = intent.getSerializableExtra("secondNumbers") as ArrayList<Int>
        val sign: String = intent.getSerializableExtra("sign") as String
        val orientation: String = intent.getSerializableExtra("orientation") as String
        val colNum: String = intent.getSerializableExtra("colNum") as String
        val gridAdapter = GridAdapter(this, sign, colNum, orientation, fN, sN)
        gridView.adapter = gridAdapter
        gridView.numColumns = Integer.parseInt(colNum)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}