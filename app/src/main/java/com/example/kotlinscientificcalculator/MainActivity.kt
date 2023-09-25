package com.example.kotlinscientificcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.kotlinscientificcalculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true
    private var prevResult = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sinButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = Math.sin(Math.toRadians(operand.toDouble()))
            binding.workingsTV.text = result.toString()
        }

        binding.cosButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = Math.cos(Math.toRadians(operand.toDouble()))
            binding.workingsTV.text = result.toString()
        }

        binding.tanButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = Math.tan(Math.toRadians(operand.toDouble()))
            binding.workingsTV.text = result.toString()
        }

        binding.logButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = Math.log10(operand.toDouble())
            binding.workingsTV.text = result.toString()
        }

        binding.inButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = Math.log(operand.toDouble())
            binding.workingsTV.text = result.toString()
        }

        binding.factorialButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            var result = 1.0
            for (i in 2..operand.toInt()) {
                result *= i.toDouble()
            }
            binding.workingsTV.text = result.toString()
        }

        binding.squareButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = operand.toDouble() * operand.toDouble()
            binding.workingsTV.text = result.toString()
        }

        binding.rootButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = Math.sqrt(operand.toDouble())
            binding.workingsTV.text = result.toString()
        }

        binding.reciprocalButton.setOnClickListener {
            val operand = binding.workingsTV.text.toString()
            val result = 1.0 / operand.toDouble()
            binding.workingsTV.text = result.toString()
        }
    }
    fun numberAction(view: View){
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    binding.workingsTV.append(view.text)

                canAddDecimal = false
            }
            else
                binding.workingsTV.append(view.text)

            canAddOperation = true
        }
    }
    fun operationAction(view: View){
        if(view is Button && canAddOperation)
        {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun equalsAction(view: View) {
        binding.resultTV.text = calculateResults()
    }
    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '−')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('×') || list.contains('÷'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    '×' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
    fun allClearAction(view: View) {

        binding.workingsTV.text=""
        binding.resultTV.text = ""

    }
    fun backSpaceAction(view: View) {
        val length = binding.workingsTV.length()
        if(length>0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0,length-1)
    }

}