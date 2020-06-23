package com.lidor.calculatorkt.model


import android.content.Context
import com.lidor.calculatorkt.R
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.ceil
import kotlin.math.floor

/**
 * A Basic Calculator operational class.
 * class logic used with @param [expressionString] -> [StringBuilder]
 * and a [ExpressionBuilder] to build and evaluate any [Expression].
 *
 * @param [context] the app context ,
 * helps to apply [Context.getString] method to manipulate the [expressionString]
 * and make sure he valid to avoid [IllegalArgumentException].
 * @property expressionString [StringBuilder] element helps to concatenate the Math expression.
 * @property stateError [Boolean] indicates if the [Calculator] element couldn't processes the request.
 * @property lastDig [Boolean] indicates if the last expression is a [Number].
 * @property lastDot [Boolean] indicates if the [Number] has already [R.string.dot] avoiding [IllegalArgumentException] in addition.
 * @author Lidor Fadida.
 */
class Calculator(private val context: Context) {

    private var expressionString: StringBuilder = StringBuilder()
    private var stateError: Boolean = false
    private var lastDig: Boolean = false
    private var lastDot: Boolean = false

    /**
     * concatenate a [Number] to the [expressionString].
     * @param dig the [Number] to be added.
     * @return [expressionString] after @param [dig] was appended.
     */
    fun appendDigit(dig: String): String {
        this.expressionString.append(dig)
        if (this.stateError)
            this.stateError = false
        this.lastDig = true
        return this.expressionString.toString()
    }

    /**
     * concatenate a [String] representing the Math operator to the [expressionString]
     * @param op the [String] Math operator to be added
     * @return [expressionString] after @param [op] was appended.
     */
    fun appendOperator(op: String): String {
        if (this.lastDig && !this.stateError) {
            this.lastDig = false
            this.lastDot = false
            this.expressionString.append(op)
        }
        return this.expressionString.toString()
    }

    /**
     * concatenate a [R.string.dot] to the [expressionString]
     * @return [expressionString] after @param [R.string.dot] was appended.
     */
    fun appendDot(): String {
        if (this.lastDig && !this.stateError && !this.lastDot) {
            val dot = this.context.getString(R.string.dot)
            this.expressionString.append(dot)
            this.lastDig = false
            this.lastDot = true
        }
        return this.expressionString.toString()
    }

    /**
     * clearing the @param [expressionString] and resetting the @param [lastDig] , @param [stateError] , @param [lastDot].
     * @return [String] value = [R.string.zero] using the @param [context].
     */
    fun refreshExpression(): String {
        this.expressionString.clear()
        this.lastDig = true
        this.stateError = false
        this.lastDot = false
        return this.context.getString(R.string.zero)
    }

    /**
     * refreshes the [expressionString] and
     * stores the last result evaluated in [onEqual] in [expressionString].
     * @param res [String] the last result evaluated in [onEqual]
     * @return [expressionString] after @param [res] appended.
     */
    private fun storeLastResult(res: String): String {
        refreshExpression()
        this.expressionString.append(res)
        return this.expressionString.toString()
    }

    /**
     * Calculating the [expressionString] only if [lastDig] and ![stateError] else @throws [RuntimeException]
     * @return (1) [String] result of evaluated [expressionString] or (2) [R.string.error]
     * @throws RuntimeException if @param ![lastDig] || @param [stateError]
     */
    fun onEqual(): String {
        if (this.lastDig && !this.stateError) {
            val expression: Expression = prepareExpression()
            return try {
                val value: Double = expression.evaluate()
                var temp: Int? = null
                if (ceil(value) == floor(value))
                    temp = value.toInt()
                val result = storeLastResult(temp?.toString() ?: value.toString())
                if (result.contains(context.getString(R.string.dot)))
                    this.lastDot = true
                result
            } catch (ex: ArithmeticException) {
                refreshExpression()
                this.stateError = true
                this.lastDig = false
                this.context.getString(R.string.error)
            }
        }
        throw RuntimeException(context.getString(R.string.inCompleteExpression))
    }

    /**
     * Preparing the [expressionString] before [Expression.evaluate] is initiates.
     * basically replacing the [R.string.multiplyUi] and [R.string.divideUi] when encounters
     * with [R.string.multiply]  and [R.string.divide].
     * @return a valid [Expression] element ready to be evaluated.
     */
    private fun prepareExpression(): Expression {
        return ExpressionBuilder(
            this.expressionString.toString()
                .replace(
                    (this.context.getString(R.string.multiplyUi))[0],
                    this.context.getString(R.string.multiply)[0],
                    true
                )
                .replace(
                    (this.context.getString(R.string.divideUi))[0],
                    this.context.getString(R.string.divide)[0],
                    true
                )
        ).build()
    }
}