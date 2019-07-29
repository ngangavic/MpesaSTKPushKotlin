package com.example.mpesastkpushkotlin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.example.mpesastkpushkotlin.okhttp.OkHttpRequest
import com.example.mpesastkpushkotlin.settings.SandBox
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editTextPhone = findViewById(R.id.editTextPhone)as EditText
        val editTextAmount = findViewById(R.id.editTextAmount)as EditText
        val button = findViewById(R.id.Send)as Button
        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        button.setOnClickListener {
            stkPush()
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun stkPush(){
        var jsonArray = JSONArray()
        var jsonObject = JSONObject()
        jsonObject.put("BusinessShortCode", SandBox.business_shortcode)
        println(SandBox.business_shortcode)
        jsonObject.put("Password", generatePassword())
        jsonObject.put("Timestamp", generateDate())
        jsonObject.put("TransactionType", "CustomerPayBillOnline")
        jsonObject.put("Amount", editTextAmount.text.toString())
        jsonObject.put("PhoneNumber", editTextPhone.text.toString())
        jsonObject.put("PartyA", editTextPhone.text.toString())
        jsonObject.put("PartyB", SandBox.business_shortcode)
        println("PartyB: "+SandBox.business_shortcode)
        jsonObject.put("CallBackURL", "http://callback/url/address")
        jsonObject.put("AccountReference", "Trial")
        jsonObject.put("TransactionDesc", "Please work")

        jsonArray.put(jsonObject)
        var requestJson = jsonArray.toString().replace("[\\[\\]]","")
        var rq:OkHttpRequest = OkHttpRequest()
        rq.getRequest(requestJson,SandBox.stk_push_url)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generatePassword():String{
        val payBill=SandBox.business_shortcode
        println("Paybill: "+SandBox.business_shortcode)
        val secretKey = SandBox.pass_key
        val time = generateDate()
        val psd=payBill+secretKey+time

        val bytes = psd.toByteArray(charset("ISO-8859-1"))
        val password = Base64.getEncoder().encodeToString(bytes)
        System.out.println("Password: "+password)
        return password
    }

    fun generateDate():String {
        var date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        System.out.println(date)
        return date
    }

}
