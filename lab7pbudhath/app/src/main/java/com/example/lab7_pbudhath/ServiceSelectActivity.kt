package com.example.lab7_pbudhath

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast

class ServiceSelectActivity : AppCompatActivity() {

    companion object{
        val PARAM1: String = "PARAM1"
        val PARAM2: String = "PARAM2"
        val PARAM3: String = "PARAM3"
    }

    private var exc_quality: Float = -1f
    private var avg_quality: Float = -1f
    private var bavg_quality: Float = -1f

    private var o_exc_quality: Float = 0f
    private var o_avg_quality: Float = 0f
    private var o_bavg_quality: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_select)

        o_exc_quality = intent.getFloatExtra(PARAM1, 0.25f)
        o_avg_quality = intent.getFloatExtra(PARAM2, 0.20f)
        o_bavg_quality = intent.getFloatExtra(PARAM3, 0.15f)
        val exc_edit = findViewById<EditText>(R.id.Exc_Edit)
        val avg_edit = findViewById<EditText>(R.id.Avg_Edit)
        val bavg_edit = findViewById<EditText>(R.id.Bavg_Edit)


        exc_edit.hint = ((o_exc_quality*100).toString())
        avg_edit.hint = ((o_avg_quality*100).toString())
        bavg_edit.hint = ((o_bavg_quality*100).toString())

        val tw = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s == exc_edit.text) {
                    if (exc_edit.text.isNotEmpty()) {
                        exc_quality = exc_edit.text.toString().toFloat()/100

                        if(exc_quality < 0){
                            Toast.makeText(applicationContext, "Percentage must be a positive integer", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else if (s == avg_edit.text) {
                    if (avg_edit.text.isNotEmpty()) {
                        avg_quality = avg_edit.text.toString().toFloat()/100

                        if(avg_quality < 0){
                            Toast.makeText(applicationContext, "Percentage must be a positive integer", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else if (s == bavg_edit.text) {
                    if (bavg_edit.text.isNotEmpty()) {
                        bavg_quality = bavg_edit.text.toString().toFloat()/100

                        if(bavg_quality < 0){
                            Toast.makeText(applicationContext, "Percentage must be a positive integer", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun beforeTextChanged (s: CharSequence, start: Int, count: Int, after: Int){
            }
            override fun onTextChanged (s: CharSequence, start: Int, before: Int, count: Int){
            }

        }
        exc_edit.addTextChangedListener(tw)
        avg_edit.addTextChangedListener(tw)
        bavg_edit.addTextChangedListener(tw)
    }

    fun onClick(v: View){
        val intent: Intent = Intent()
        if (v.getId() == R.id.Keep_Button){
            if(exc_quality == -1f){
                intent.putExtra(MainActivity.RESULT1, o_exc_quality);
            }
            else {
                intent.putExtra(MainActivity.RESULT1, exc_quality);
            }
            if(avg_quality == -1f){
                intent.putExtra(MainActivity.RESULT2, o_avg_quality);
            }
            else {
                intent.putExtra(MainActivity.RESULT2, avg_quality);
            }
            if(bavg_quality == -1f){
                intent.putExtra(MainActivity.RESULT3, o_bavg_quality);
            }
            else {
                intent.putExtra(MainActivity.RESULT3, bavg_quality);
            }

        }
        if (v.getId() == R.id.Cancel_Button){
            intent.putExtra(MainActivity.RESULT1, o_exc_quality);
            intent.putExtra(MainActivity.RESULT2, o_avg_quality);
            intent.putExtra(MainActivity.RESULT3, o_bavg_quality);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}