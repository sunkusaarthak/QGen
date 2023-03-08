package com.appc.qgen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GridAdapter(
    private val context: Context,
    private val sign: String,
    private val colNum: String,
    private val orientation: String,
    private val firstNumbers: ArrayList<Int>,
    private val secondNumbers: ArrayList<Int>
) : BaseAdapter() {

    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return (firstNumbers.size)
    }

    override fun getItem(p0: Int): Any {
        return (p0)
    }

    override fun getItemId(p0: Int): Long {
        return (0)
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var p2 = layoutInflater.inflate(R.layout.grid_item_vertical, null)
        p2 = if (orientation == "Vertical") {
            layoutInflater.inflate(R.layout.grid_item_vertical, null)
        } else {
            layoutInflater.inflate(R.layout.grid_item_horizontal, null)
        }
        val firstText: TextView = p2!!.findViewById(R.id.firstText)
        val secondText: TextView = p2.findViewById(R.id.secondText)
        val signText: TextView = p2.findViewById(R.id.signText)
        if (colNum == "3" && orientation == "Vertical") {
            firstText.textSize = 32F
            secondText.textSize = 32F
            signText.textSize = 32F
        }
        signText.text = sign
        firstText.text = firstNumbers[p0].toString()
        secondText.text = secondNumbers[p0].toString()
        return (p2)
    }
}