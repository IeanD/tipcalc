package edu.washington.ieand.tipcalc

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private var currPercent: Double = 0.18

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            bindListeners()
        }
        catch (e: Exception) {
            Log.e(TAG, "Oops! Error binding listeners. Exception: " + e.toString())
        }

    }

    private fun bindListeners() {
        // Bind Button onClick listener
        val txtInput = findViewById<TextInputEditText>(R.id.txt_tip_input)
        val submitBtn: Button = findViewById(R.id.btn_tip_submit)
        submitBtn.setOnClickListener {
            try {
                var currInput = txtInput.text.toString().split("$").last()
                if (!currInput.isNullOrBlank() && currInput !== ".") {
                    val currInputAsDouble: Double = currInput.toDouble() * currPercent
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.HALF_UP
                    var roundedOutput: String = "$" + df.format(currInputAsDouble)
                    if (roundedOutput.contains(".")) {
                        val decimalSplit = roundedOutput.split(".")
                        if (decimalSplit.last().toString().length < 2) {
                            roundedOutput += "0"
                        }
                    } else {
                        roundedOutput += ".00"
                    }

                    val toast = Toast.makeText(applicationContext, "Tip amount: $roundedOutput", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
            catch (e: Exception) {
                Log.e(TAG, "Oops! Error in button OnClick. Exception: " + e.toString())
            }
        }

        // Bind RadioGroup onCheckedChange listener
        val radioGroup = findViewById<RadioGroup>(R.id.rdioGroup_tip_amt)
        radioGroup.setOnCheckedChangeListener({ _, id ->
            try {
                currPercent = when(id) {
                    R.id.radioButton1 -> 0.10
                    R.id.radioButton2 -> 0.15
                    R.id.radioButton3 -> 0.18
                    R.id.radioButton4 -> 0.20
                    else -> {
                        0.18
                    }
                }
            }
            catch (e: Exception) {
                Log.e(TAG, "Oops! Error in RadioGroup OnCheckedChange. Exception: " + e.toString())
            }

        })

        // Bind EditText onFocus listener
        txtInput.setOnFocusChangeListener({ _, _ ->
            try {
                addPrefixToEditText(txtInput, "$")
                txtInput.setSelection(txtInput.text.toString().length)
            }
            catch (e: Exception) {
                Log.e(TAG, "Oops! Error in EditText OnFocusChange. Exception: " + e.toString())
            }

        })

        // Bind EditText onClick listener
        txtInput.setOnClickListener {
            try {
                txtInput.setSelection(txtInput.text.toString().length)
            }
            catch (e: Exception) {
                Log.e(TAG, "Oops! Error in EditText OnClick. Exception: " + e.toString())
            }
        }

        // Bind EditText onChange listener
        txtInput.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                try {
                    txtInput.setSelection(txtInput.text.length)
                }
                catch (e: Exception) {
                    Log.e(TAG, "Oops! Error in EditText beforeTextChanged. Exception: " + e.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    if (!s.toString().startsWith("$")) {
                        if (s.toString().contains("$")) {
                            val currTxt = s.toString().split("$")
                            val newTxt = "$" + currTxt.first() + currTxt.last()
                            txtInput.setText(newTxt)
                        }

                        addPrefixToEditText(txtInput, "$")
                    }
                    if (s.toString().contains(".")) {
                        val currTxt = s.toString().split(".")
                        if (currTxt.last().length > 2) {
                            val newTxt = currTxt.first() + "." + currTxt.last().subSequence(0,2)
                            txtInput.setText(newTxt)
                        }
                    }

                    txtInput.setSelection(txtInput.text.length)

                    val finalOutput = txtInput.text.toString()
                    submitBtn.isEnabled = (finalOutput.startsWith("$") && finalOutput.length > 1 && finalOutput != "$.")
                }
                catch (e: Exception) {
                    Log.e(TAG, "Oops! Error in EditText afterTextChanged. Exception: " + e.toString())
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    txtInput.setSelection(txtInput.text.length)
                }
                catch (e: Exception) {
                    Log.e(TAG, "Oops! Error in EditText onTextChanged. Exception: " + e.toString())
                }
            }
        })
    }

    private fun addPrefixToEditText(txtInput: TextInputEditText, prefix: String) {
        try {
            var currInput = txtInput.text.toString()
            if(currInput.isNullOrBlank() || !currInput.startsWith("$")) {
                val newVal = prefix + currInput
                txtInput.setText(newVal)
            }
        }
        catch (e: Exception) {
            Log.e(TAG, "Oops! Error in addPrefixToEditText. Exception: " + e.toString())
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
