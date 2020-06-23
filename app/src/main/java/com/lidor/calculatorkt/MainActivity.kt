package com.lidor.calculatorkt

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lidor.calculatorkt.model.Calculator
import net.objecthunter.exp4j.Expression


/**
 * [MainActivity] Controller element integrating
 * the @property [calculator] as the model element.
 * basically requests data to refresh the [TextView]'s.
 * @property calculator the model element responsible the Math operations.
 * @property result a [TextView] filed in the UI containing the last calculated result.
 * @property expression a [TextView] filed in the UI containing the concatenated [Expression].
 * @author Lidor Fadida
 */
class MainActivity : AppCompatActivity() {
    private val calculator: Calculator = Calculator(this)
    private lateinit var result: TextView
    private lateinit var expression:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    /**
     * initialize the [TextView]'s to be handled.
     */
    private fun init() {
        this.result = findViewById(R.id.result)
        this.expression = findViewById(R.id.expression)
    }

    ///From here to below all functions updating the views according to the controller element///

    fun onDigitClicked(view: View) {
        this.expression.text = calculator.appendDigit(view.tag.toString())
    }

    fun onOperatorClicked(view: View) {
        this.expression.text = calculator.appendOperator((view as Button).text.toString())
    }

    fun onDotClicked(view: View) {
        this.expression.text = calculator.appendDot()
    }

    fun onClearClicked(view: View) {
        this.expression.text = calculator.refreshExpression()
        this.result.text = null
    }

    fun onEqualClicked(view: View) {
        try {
            this.result.text = calculator.onEqual()
        } catch (e: RuntimeException) { // for certain use cases. scientific calculator etc..
            Log.e("TAG", "onEqualClicked: ${e.message}")
        }
    }
}