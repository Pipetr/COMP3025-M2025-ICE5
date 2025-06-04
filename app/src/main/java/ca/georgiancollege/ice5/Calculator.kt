package ca.georgiancollege.ice5

import android.widget.Button
import ca.georgiancollege.ice5.databinding.ActivityMainBinding

class Calculator(private var binding: ActivityMainBinding) {

    // Lists of buttons for easy access
    private lateinit var numberButtons: List<Button>
    private lateinit var operatorButtons: List<Button>
    private lateinit var modifierButtons: List<Button>
    // Operational properties
    private var currentOperands: List<Float> = emptyList()
    private var currentOperators: List<String> = emptyList()
    init {
        initializeButtonLists()
        configureNumberInput()
        configureModifierButtons()
        configureOperatorButtons()
    }

    /**
     * Initializes the lists of buttons for numbers, operators, and modifiers.
     * This allows for easy access to all buttons in the calculator layout.
     *
     * @param binding [ActivityMainBinding] The binding object for the main activity layout.
     */
    private fun initializeButtonLists()
    {
        // Initialize number buttons
        numberButtons = listOf(
            binding.zeroButton, binding.oneButton, binding.twoButton,
            binding.threeButton, binding.fourButton, binding.fiveButton,
            binding.sixButton, binding.sevenButton, binding.eightButton,
            binding.nineButton, binding.decimalButton
        )

        // Initialize operator buttons
        operatorButtons = listOf(
            binding.plusButton, binding.minusButton,
            binding.multiplyButton, binding.divideButton, binding.percentButton
        )

        modifierButtons = listOf(
            binding.plusMinusButton,
            binding.clearButton, binding.deleteButton
        )
    }

    /**
     * Configures the number input buttons to handle clicks and update the result EditText.
     * It prevents multiple decimal points in the current number and handles leading zeros.
     */
    fun configureNumberInput()
    {
        numberButtons.forEach { button ->
            button.setOnClickListener {
                val input = button.text.toString()
                val currentResultText = binding.resultEditText.text.toString()

                // Prevent multiple decimal points in the current number
                if (input == "." && currentResultText.contains("."))
                {
                    return@setOnClickListener // Do nothing if a decimal already exists
                }

                // If the current result is "0" and input is not ".", replace it
                if (currentResultText == "0" && input != ".")
                {
                    binding.resultEditText.setText(input)
                }
                else
                {
                    binding.resultEditText.append(input)
                }
            }
        }
    }


    fun configureModifierButtons()
    {
        modifierButtons.forEach { button ->
            button.setOnClickListener {
                when (button)
                {
                    binding.clearButton -> binding.resultEditText.setText("0")
                    binding.deleteButton ->
                    {
                        val currentText = binding.resultEditText.text.toString()
                        if (currentText.isNotEmpty())
                        {
                            val newText = currentText.dropLast(1)

                            // If only "-" remains after deleting, reset to "0"
                            if (newText == "-" || newText.isEmpty())
                            {
                                binding.resultEditText.setText("0")
                            }
                            else
                            {
                                binding.resultEditText.setText(newText)
                            }
                        }
                    }
                    binding.plusMinusButton ->
                    {
                        val currentText = binding.resultEditText.text.toString()
                        if (currentText.isNotEmpty())
                        {
                            // don't allow changing sign if the current text is "0" or empty
                            if (currentText == "0" || currentText.isEmpty())
                            {
                                return@setOnClickListener // Do nothing
                            }
                            // if the current text is already negative, remove the negative sign
                            if (currentText.startsWith("-"))
                            {
                                binding.resultEditText.setText(currentText.removePrefix("-"))
                            }
                            else
                            {
                                val prefixedCurrentText = "-$currentText"
                                binding.resultEditText.setText(prefixedCurrentText)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs the calculation based on the current operator and operands.
     */
    private fun calculate(operation: String, values: List<Float>): Float{
        when(operation){
            "+" -> return (values.sum())
            "-" -> {
                var result = values[0]
                for (value in values.drop(1)) {
                    result -= value
                }
                return result
            }
            "*" -> {
                var result = 1f
                for (value in values) {
                    result *= value
                }
                return result
            }
            "/" -> {
                var result = values[0]
                for (value in values.drop(1)) {
                    if (value != 0f) {
                        result /= value
                    } else {
                        throw ArithmeticException("Division by zero")
                    }
                }
                return result
            }
        }
        throw IllegalArgumentException("Invalid operation: $operation")
    }

    /**
     * Sets the current operator for the calculator.
     * Sets the Operands
     * This method is used to set the operator when a button is clicked.
     */
    private fun splitResultText(){
        val currentText = binding.resultEditText.text.toString()
        if ((currentText.isNotEmpty() || currentText != "0") && currentText.length > 1) {
            // If the current text is a valid number, set it as the current operand
            // use regex to split the current text into numbers and operators
            val regex = Regex("([+\\-*/%])")
            val parts = currentText.split(regex).filter { it.isNotBlank() }

            // group the numbers into a list of floats
            currentOperands = parts.filter { it.toFloatOrNull() != null }.map { it.toFloat() }
            // group the operators into a list of strings
            currentOperators = parts.filter { it.toFloatOrNull() == null && it.isNotBlank() }
        } else {
            // If the current text is empty, do nothing
            return
        }
    }

    /**
     * Adds an event listener to the operator buttons to handle calculations.
     * This method is used to set the operator when a button is clicked.
     */
    fun configureOperatorButtons() {
        operatorButtons.forEach { button ->
            button.setOnClickListener {
                val operator = button.text.toString()
                binding.resultEditText.append(operator)
                splitResultText() // Split the current result text into operands and operators

                if (currentOperands.isNotEmpty()) {
                    try {
                        val result = calculate(operator, currentOperands)
                        binding.resultEditText.setText(result.toString())
                    } catch (e: Exception) {
                        binding.resultEditText.setText("Error")
                    }
                } else {
                    binding.resultEditText.setText("0")
                }
            }
        }
    }

}