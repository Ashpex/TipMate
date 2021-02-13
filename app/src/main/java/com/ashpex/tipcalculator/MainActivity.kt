package com.ashpex.tipcalculator

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ashpex.tipcalculator.databinding.ActivityMainBinding
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sharedBillSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.iconNumberOfPayer.visibility = View.VISIBLE
                binding.numberOfPayerEditText.visibility = View.VISIBLE
                binding.numberOfPayer.visibility = View.VISIBLE
            }
            else{
                binding.iconNumberOfPayer.visibility = View.GONE
                binding.numberOfPayerEditText.visibility = View.GONE
                binding.numberOfPayer.visibility = View.GONE
                binding.totalPerPayer.text = ""
                binding.tipPerPayer.text = ""
            }
        }
        binding.calculateButton.setOnClickListener { calculateTip() }
    }

    private fun calculateTip() {

        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null) {
            binding.tipResult.text = ""
            binding.totalResult.text = ""
            return
        }
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_fifteen_percent -> 0.15
            else -> 0.10
        }

        var tip = tipPercentage * cost
        var total = tip + cost

        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
            total = kotlin.math.ceil(total)
        }

        if(binding.sharedBillSwitch.isChecked){
            var numberOfPayer = binding.numberOfPayerEditText.text.toString().toIntOrNull()
            if(numberOfPayer == null){
                binding.iconNumberOfPayer.visibility = View.GONE
                binding.numberOfPayerEditText.visibility = View.GONE
                binding.numberOfPayer.visibility = View.GONE
                binding.totalPerPayer.text = ""
                binding.tipPerPayer.text = ""
                return
            }
            var tipPerPayer = tip/numberOfPayer
            var totalPerPayer = cost/numberOfPayer + tipPerPayer
            if(binding.roundUpSwitch.isChecked){
                tipPerPayer = kotlin.math.ceil(tipPerPayer)
                totalPerPayer = kotlin.math.ceil(totalPerPayer)
            }
            val formattedTipPerPayer = NumberFormat.getCurrencyInstance().format(tipPerPayer)
            val formattedTotalPerPayer = NumberFormat.getCurrencyInstance().format(totalPerPayer)
            binding.tipPerPayer.text =  getString(R.string.tip_per_payer, formattedTipPerPayer)
            binding.totalPerPayer.text = getString(R.string.total_per_payer, formattedTotalPerPayer)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        val formattedTotal = NumberFormat.getCurrencyInstance().format(total)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        binding.totalResult.text = getString(R.string.total, formattedTotal)
    }
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
    fun toggleView(view: View) {
        view.isVisible = !view.isVisible
    }
}