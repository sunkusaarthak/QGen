package com.appc.qgen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        //Variables for the actual data
        var firstNumLowerRange = "";
        var firstNumUpperRange = ""
        var secondNumLowerRange = "";
        var secondNumUpperRange = ""
        var operationVal = ""
        var orientationVal = ""
        var maxNumVal: String
        var colNumVal = "";
        var rowNumVal = ""
        val numCollection: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val firstNumbers: ArrayList<Int> = ArrayList()
        val secondNumbers: ArrayList<Int> = ArrayList()

        //objects of the views
        val spinnerFirstNum1: AutoCompleteTextView = findViewById(R.id.fill_item1)
        val spinnerFirstNum2: AutoCompleteTextView = findViewById(R.id.fill_item2)
        val spinnerSecondNum1: AutoCompleteTextView = findViewById(R.id.fill_item_second_1)
        val spinnerSecondNum2: AutoCompleteTextView = findViewById(R.id.fill_item_second_2)
        val operationSpinner: AutoCompleteTextView = findViewById(R.id.operations)
        val orientationSpinner: AutoCompleteTextView = findViewById(R.id.format)
        val colNum: AutoCompleteTextView = findViewById(R.id.colSelect)
        val rowNum: AutoCompleteTextView = findViewById(R.id.rowSelect)
        val maxEditTextLayout: TextInputLayout = findViewById(R.id.maximumEditText)
        val maxEditText: EditText? = maxEditTextLayout.editText
        val generateButton: Button = findViewById(R.id.generateButton)

        //Data for the Spinners
        val listNumbers: ArrayList<String> =
            arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val operationData: ArrayList<String> =
            arrayListOf("Addition +", "Subtraction -", "Multiplication *", "Divide /")
        val orientationData: ArrayList<String> = arrayListOf("Vertical", "Horizontal")
        val colNumData: ArrayList<String> = arrayListOf("1", "2", "3")

        //Adapters for the Spinners
        val spinnerFirstNum1dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, listNumbers)
        val spinnerFirstNum2dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, listNumbers)
        val spinnerSecondNum1dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, listNumbers)
        val spinnerSecondNum2dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, listNumbers)
        val rowNumDataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, listNumbers)
        val operationAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, operationData)
        val orientationAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.drop_down_item, orientationData)
        val colNumAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.drop_down_item, colNumData)

        //setting the adapter for the spinners
        spinnerFirstNum1.setAdapter(spinnerFirstNum1dataAdapter)
        spinnerFirstNum2.setAdapter(spinnerFirstNum2dataAdapter)
        spinnerSecondNum1.setAdapter(spinnerSecondNum1dataAdapter)
        spinnerSecondNum2.setAdapter(spinnerSecondNum2dataAdapter)
        operationSpinner.setAdapter(operationAdapter)
        orientationSpinner.setAdapter(orientationAdapter)
        colNum.setAdapter(colNumAdapter)
        rowNum.setAdapter(rowNumDataAdapter)

        //adding onClickListener for the spinners
        spinnerFirstNum1.setOnItemClickListener { _, _, i, _ ->
            firstNumLowerRange = listNumbers[i]
            spinnerFirstNum1.error = null
        }
        spinnerFirstNum2.setOnItemClickListener { _, _, i, _ ->
            firstNumUpperRange = listNumbers[i]
            spinnerFirstNum2.error = null
        }
        spinnerSecondNum1.setOnItemClickListener { _, _, i, _ ->
            secondNumLowerRange = listNumbers[i]
            spinnerSecondNum1.error = null
        }
        spinnerSecondNum2.setOnItemClickListener { _, _, i, _ ->
            secondNumUpperRange = listNumbers[i]
            spinnerSecondNum2.error = null
        }
        operationSpinner.setOnItemClickListener { _, _, i, _ ->
            operationVal = i.toString()
            operationSpinner.error = null
        }
        orientationSpinner.setOnItemClickListener { _, _, i, _ ->
            orientationVal = orientationData[i]
            orientationSpinner.error = null
        }
        colNum.setOnItemClickListener { _, _, i, _ ->
            colNumVal = colNumData[i]
            colNum.error = null
        }
        rowNum.setOnItemClickListener { _, _, i, _ ->
            rowNumVal = listNumbers[i]
            rowNum.error = null
        }

        maxEditText?.doAfterTextChanged {
            maxEditText.error = null
        }

        //Button onClickListener
        generateButton.setOnClickListener {
            maxNumVal = maxEditText?.text.toString()

            if (firstNumLowerRange.isEmpty()) {
                spinnerFirstNum1.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (firstNumUpperRange.isEmpty()) {
                spinnerFirstNum2.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (secondNumLowerRange.isEmpty()) {
                spinnerSecondNum1.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (secondNumUpperRange.isEmpty()) {
                spinnerSecondNum2.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (operationVal.isEmpty()) {
                operationSpinner.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (orientationVal.isEmpty()) {
                orientationSpinner.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (colNumVal.isEmpty()) {
                colNum.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (rowNumVal.isEmpty()) {
                rowNum.error = "Field cannot be left blank."
                return@setOnClickListener
            }
            if (maxNumVal.isNotEmpty()) {
                if (!checkAllDigits(maxNumVal)) {
                    maxEditText?.error = "please type digits"
                    return@setOnClickListener
                }
            }

            if (firstNumLowerRange > firstNumUpperRange) {
                Toast.makeText(this, "max should be greater in first number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (secondNumLowerRange > secondNumUpperRange) {
                Toast.makeText(this, "max should be greater in second number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val maxNumInInt: Int
            val fMin: Int = Integer.parseInt(firstNumLowerRange)
            val fMax: Int = Integer.parseInt(firstNumUpperRange)
            val sMin: Int = Integer.parseInt(secondNumLowerRange)
            val sMax: Int = Integer.parseInt(secondNumUpperRange)
            val cNum: Int = Integer.parseInt(colNumVal)
            val rNum: Int = Integer.parseInt(rowNumVal)
            val totalCell = cNum * rNum
            var extractedMaxVal: Int = when (operationVal) {
                "0" -> (fMax + sMax)
                "1" -> (fMax.coerceAtLeast(sMax) - fMin.coerceAtMost(sMin))
                "2" -> (fMax * sMax)
                "3" -> (fMax.coerceAtLeast(sMax) / fMin.coerceAtMost(sMin))
                else -> 0
            }
            if (maxNumVal.isNotEmpty()) {
                maxNumInInt = Integer.parseInt(maxNumVal)
                when (operationVal) {
                    "0" -> {
                        if (extractedMaxVal < maxNumInInt) {
                            Toast.makeText(this, "change maximum", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                    "1" -> {
                        if (extractedMaxVal < maxNumInInt) {
                            Toast.makeText(this, "change maximum", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                    "2" -> {
                        extractedMaxVal = (fMax * sMax)
                        if (extractedMaxVal < maxNumInInt) {
                            Toast.makeText(this, "change maximum", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                    "3" -> {
                        if (extractedMaxVal < maxNumInInt) {
                            Toast.makeText(this, "change maximum", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                    else -> {
                        Log.d(
                            "TestData", "$firstNumLowerRange $firstNumUpperRange " +
                                    "$secondNumLowerRange $secondNumUpperRange " +
                                    "$operationVal $orientationVal " +
                                    "$colNumVal $rowNumVal " +
                                    maxNumVal
                        )
                    }
                }
            } else {
                maxNumInInt = extractedMaxVal
            }

            thread {
                when (operationVal) {
                    "0" -> {
                        var count = 0
                        firstNumbers.clear()
                        secondNumbers.clear()
                        numCollection.clear()
                        for (maxNumIntDec in 1..maxNumInInt) {
                            if (count >= totalCell) {
                                break
                            }
                            for (i in fMin..fMax) {
                                val calc = (maxNumIntDec - i)
                                if (count >= totalCell) {
                                    break
                                }
                                if (calc in sMin..sMax) {
                                    if (numCollection.find { it == Pair(i, calc) } == null) {
                                        firstNumbers.add(i)
                                        secondNumbers.add(calc)
                                        numCollection.add(Pair(i, calc))
                                        count++
                                    }
                                }
                            }
                        }
                        Log.d(
                            "TestSet",
                            "$firstNumbers \n $secondNumbers" + "$firstNumLowerRange $firstNumUpperRange " +
                                    "$secondNumLowerRange $secondNumUpperRange " +
                                    "$operationVal $orientationVal " +
                                    "$colNumVal $rowNumVal " +
                                    maxNumVal
                        )
                    }

                    "1" -> {
                        var count = 0
                        firstNumbers.clear()
                        secondNumbers.clear()
                        numCollection.clear()
                        for (maxNumIntDec in 1..maxNumInInt) {
                            if (count >= totalCell) {
                                break
                            }
                            for (i in fMin..fMax) {
                                val calc = (i - maxNumIntDec)
                                if (count >= totalCell) {
                                    break
                                }
                                if (calc in sMin..sMax) {
                                    if (numCollection.find { it == Pair(i, calc) } == null) {
                                        firstNumbers.add(i)
                                        secondNumbers.add(calc)
                                        numCollection.add(Pair(i, calc))
                                        count++
                                    }
                                }
                            }
                        }
                        Log.d(
                            "TestSet",
                            "$firstNumbers \n $secondNumbers" + "$firstNumLowerRange $firstNumUpperRange " +
                                    "$secondNumLowerRange $secondNumUpperRange " +
                                    "$operationVal $orientationVal " +
                                    "$colNumVal $rowNumVal " +
                                    maxNumVal
                        )
                    }

                    "2" -> {
                        var count = 0
                        firstNumbers.clear()
                        secondNumbers.clear()
                        numCollection.clear()
                        for (maxNumIntDec in 1..maxNumInInt) {
                            if (count >= totalCell) {
                                break
                            }
                            for (i in fMin..fMax) {
                                val calc: Int
                                if (maxNumIntDec % i == 0) {
                                    calc = (maxNumIntDec / i)
                                    if (count >= totalCell) {
                                        break
                                    }
                                    if (calc in sMin..sMax) {
                                        if (numCollection.find { it == Pair(i, calc) } == null) {
                                            firstNumbers.add(i)
                                            secondNumbers.add(calc)
                                            numCollection.add(Pair(i, calc))
                                            count++
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("TestSet", "$firstNumbers \n $secondNumbers")
                    }

                    "3" -> {
                        var count = 0
                        firstNumbers.clear()
                        secondNumbers.clear()
                        numCollection.clear()
                        for (i in fMin..fMax) {
                            for (calc in sMin..sMax) {
                                if (i / calc <= maxNumInInt) {
                                    if (numCollection.find { it == Pair(i, calc) } == null) {
                                        firstNumbers.add(i)
                                        secondNumbers.add(calc)
                                        numCollection.add(Pair(i, calc))
                                        count++
                                    }
                                } else if (calc / i <= maxNumInInt) {
                                    if (numCollection.find { it == Pair(calc, i) } == null) {
                                        firstNumbers.add(calc)
                                        secondNumbers.add(i)
                                        numCollection.add(Pair(calc, i))
                                        count++
                                    }
                                }
                            }
                        }
                    }
                }

                val opSign: String = when (operationVal) {
                    "0" -> "+"
                    "1" -> "-"
                    "2" -> "*"
                    "3" -> "/"
                    else -> "."
                }

                val i = Intent(this, ShowSheet::class.java)
                i.putExtra("firstNumbers", firstNumbers)
                i.putExtra("secondNumbers", secondNumbers)
                i.putExtra("sign", opSign)
                i.putExtra("colNum", colNumVal)
                i.putExtra("orientation", orientationVal)
                startActivity(i)
            }
        }
    }

    private fun checkAllDigits(maxNumVal: String): Boolean {
        val ch = maxNumVal.toCharArray()
        for (i in ch) {
            if (i !in '0'..'9') {
                return (false)
            }
        }
        return (true)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}