package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var lastNumeric = false
    var lastDot = false
    var stateError = false

    private  lateinit var  expression : Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onAllClear(view: View) {
        binding.tvExpression.text = ""
        binding.tvResult.text = ""
        binding.tvResult.visibility = View.GONE
        lastNumeric = false
        lastDot = false
        stateError = false
    }

    fun onBack(view: View) {
        binding.tvExpression.text = binding.tvExpression.text.toString().dropLast(1)
        try {
            val lastChar = binding.tvExpression.text.toString().last()
            if(lastChar.isDigit()){
                Equal()
            }
        }
        catch (e: Exception){
            Log.e("evaluate error",e.toString())
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
        }

    }

    fun onOperator(view: View) {
        if(!stateError && lastNumeric){
            binding.tvExpression.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            Equal()
        }
    }

    fun onEqual(view: View) {
        Equal()
        binding.tvExpression.text = binding.tvResult.text.toString().drop(1)
        binding.tvResult.text=""
        binding.tvResult.visibility = View.GONE
    }

    fun onDigit(view: View) {
        if(stateError){
            binding.tvExpression.text = (view as Button).text
            stateError = false
        }
        else{
            binding.tvExpression.append((view as Button).text)
        }
        lastNumeric = true
        Equal()
    }

    fun onClear(view: View) {
        binding.tvExpression.text = ""
        lastNumeric = false
    }

    fun Equal(){
        if(lastNumeric && !stateError){
            val text = binding.tvExpression.text.toString()
            expression = ExpressionBuilder(text).build()

            try {
                val answer = expression.evaluate()
                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = "=$answer"
            }
            catch (ex : ArithmeticException){
                Log.e("evaluate error",ex.toString())
                binding.tvResult.text="Error"
                stateError=true
                lastNumeric = false
            }
        }
    }


}