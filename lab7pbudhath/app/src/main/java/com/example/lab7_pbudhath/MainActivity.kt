package com.example.lab7_pbudhath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var bill: Float = 0.0f
    private var serviceQuality: Float = 0.0f
    private var excQuality: Float = 0.25f
    private var avgQuality: Float = 0.20f
    private var bavgQuality: Float = 0.15f
    private var numPeople: Int = 1
    private var radioChecked: Boolean = false

    companion object {
        const val STATE_EXC_QUAL: String = "EXC_QUALITY"
        const val STATE_AVG_QUAL: String = "AVG_QUALITY"
        const val STATE_BAVG_QUAL: String = "BAVG_QUALITY"
        const val STATE_RADIO_CHECK: String = "RADIO_CHECKED"
        const val STATE_NUM_PEOPLE: String = "NUM_PEOPLE"
        const val STATE_SERVICE_QUAL: String = "SERVICE_QUALITY"
        const val STATE_BILL: String = "BILL"

        const val RESULT1: String = "RESULT1"
        const val RESULT2: String = "RESULT2"
        const val RESULT3: String = "RESULT3"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState != null){
            excQuality = savedInstanceState.getFloat(STATE_EXC_QUAL)
            avgQuality = savedInstanceState.getFloat(STATE_AVG_QUAL)
            bavgQuality = savedInstanceState.getFloat(STATE_BAVG_QUAL)

            serviceQuality = savedInstanceState.getFloat(STATE_SERVICE_QUAL)
            bill = savedInstanceState.getFloat(STATE_SERVICE_QUAL)
            numPeople = savedInstanceState.getInt(STATE_SERVICE_QUAL)
            radioChecked = savedInstanceState.getBoolean(STATE_SERVICE_QUAL)

        }

        val billEdit = findViewById<EditText>(R.id.Bill_Edit)
        val peopleEdit = findViewById<EditText>(R.id.People_Edit)
        val twb = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (billEdit.text.isNotEmpty()){
                    bill = billEdit.text.toString().toFloat()
                    if(bill > 10000){
                        Toast.makeText(applicationContext, "Max is $10,000",
                            Toast.LENGTH_SHORT).show()
                        bill = 0f
                        return
                    }

                    if(bill < 0){
                        Toast.makeText(applicationContext, "Invalid Entry",
                            Toast.LENGTH_SHORT).show()
                        bill = 0f
                        return
                    }
                    if(radioChecked){
                        updateText()
                    }
                    else{
                        Toast.makeText(applicationContext, "Select a service quality",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    bill = 0f
                    findViewById<TextView>(R.id.Tip_Total_Val).text = ""
                    findViewById<TextView>(R.id.Total_Val).text = ""
                    findViewById<TextView>(R.id.Total_Per_Val).text = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        billEdit.addTextChangedListener(twb)
        billEdit.setText("")

        val twp = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (peopleEdit.text.isNotEmpty()){
                    numPeople = peopleEdit.text.toString().toInt()
                    if(numPeople > 10){
                        Toast.makeText(applicationContext, "Max is $10,000",
                            Toast.LENGTH_SHORT).show()
                        numPeople = 1
                        return
                    }

                    if(numPeople < 1){
                        Toast.makeText(applicationContext, "Invalid Entry",
                            Toast.LENGTH_SHORT).show()
                        numPeople = 1
                        return
                    }
                    if(radioChecked){
                        updateText()
                    }
                    else{
                        Toast.makeText(applicationContext, "Select a service quality",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    numPeople = 1
                    findViewById<TextView>(R.id.Tip_Total_Val).text = ""
                    findViewById<TextView>(R.id.Total_Val).text = ""
                    findViewById<TextView>(R.id.Total_Per_Val).text = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        peopleEdit.addTextChangedListener(twp)
        peopleEdit.setText("")

    }

    fun myOnClick(v: View){
        if(v.id == R.id.Clear_Button){
            findViewById<EditText>(R.id.Bill_Edit).setText("")
            findViewById<EditText>(R.id.People_Edit).setText("")
            findViewById<RadioButton>(R.id.Radio_Exc).isChecked = false
            findViewById<RadioButton>(R.id.Radio_Avg).isChecked = false
            findViewById<RadioButton>(R.id.Radio_Bavg).isChecked = false
            findViewById<TextView>(R.id.Tip_Total_Val).text = ""
            findViewById<TextView>(R.id.Total_Val).text = ""
            findViewById<TextView>(R.id.Total_Per_Val).text = ""
            radioChecked = false
            bill = 0.0f
        }

        if(v.id == R.id.Update_Button){
            val intent = Intent(applicationContext, ServiceSelectActivity::class.java)
            intent.putExtra(STATE_EXC_QUAL, excQuality)
            intent.putExtra(STATE_AVG_QUAL, avgQuality)
            intent.putExtra(STATE_BAVG_QUAL, bavgQuality)
            startActivityForResult(intent, 0)
        }
    }

    fun onServiceRadioClick(v: View){
        radioChecked = (v as RadioButton).isChecked
        if(!radioChecked){
            serviceQuality = 0f
            return;
        }

        if(v.id == R.id.Radio_Exc){
            serviceQuality = excQuality
        }
        if(v.id == R.id.Radio_Avg){
            serviceQuality = avgQuality
        }
        if(v.id == R.id.Radio_Bavg){
            serviceQuality = bavgQuality
        }

        if(bill == 0f){
            Toast.makeText(applicationContext, "Enter bill amount", Toast.LENGTH_SHORT).show();
        }
        else{
            updateText()
        }
    }

    fun updateText(){

        if(!radioChecked){
            return;
        }

        if(bill == 0f){
            return;
        }

        val tip_total: Float =  bill * serviceQuality;
        val total: Float = bill + tip_total;
        val total_per: Float = total/numPeople;

        (findViewById<TextView>(R.id.Tip_Total_Val)).text = (String.format("%.2f%n", tip_total))
        (findViewById<TextView>(R.id.Total_Val)).text = (String.format("%.2f%n", total))
        (findViewById<TextView>(R.id.Total_Per_Val)).text = (String.format("%.2f%n", total_per))
    }

    private fun updateServiceQuality(){
        (findViewById<TextView>(R.id.Radio_Exc))
            .text = (String.format("Excellent (%.2f%%)", excQuality*100))
        (findViewById<TextView>(R.id.Radio_Avg))
            .text = (String.format("Average (%.2f%%)", avgQuality*100))
        (findViewById<TextView>(R.id.Radio_Bavg))
            .text =(String.format("Below Average (%.2f%%)", bavgQuality*100))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null) {
            excQuality = data.getFloatExtra(RESULT1, 0.25f)
            avgQuality = data.getFloatExtra(RESULT2, 0.20f)
            bavgQuality = data.getFloatExtra(RESULT3, 0.15f)
            updateServiceQuality()
        }

    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putFloat(STATE_EXC_QUAL, excQuality);
        savedInstanceState.putFloat(STATE_AVG_QUAL, avgQuality);
        savedInstanceState.putFloat(STATE_BAVG_QUAL, bavgQuality);
        savedInstanceState.putFloat(STATE_BILL, bill);
        savedInstanceState.putFloat(STATE_SERVICE_QUAL, serviceQuality);

        savedInstanceState.putInt(STATE_NUM_PEOPLE, numPeople);
        savedInstanceState.putBoolean(STATE_RADIO_CHECK, radioChecked);

        super.onSaveInstanceState(savedInstanceState);
    }
}