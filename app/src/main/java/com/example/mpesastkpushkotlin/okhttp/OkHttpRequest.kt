package com.example.mpesastkpushkotlin.okhttp

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mpesastkpushkotlin.settings.SandBox
import okhttp3.*
import org.json.JSONObject
import java.util.*

class OkHttpRequest() {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAccessToken(): String {
        val key: String = SandBox.counsumer_key + ":" + SandBox.counsumer_secret
        val bytes = key.toByteArray(charset("ISO-8859-1"))
        val encoded: String = Base64.getEncoder().encodeToString(bytes)
        var client = OkHttpClient()
        var request = Request.Builder()
            .url(SandBox.access_token_url)
            .get()
            .addHeader("authorization", "Basic " + encoded)
            .addHeader("cache-control", "no-cache")
            .build()
        var response: Response = client.newCall(request).execute()
        val responseData = response.body()?.string()
        println(responseData)
        var jsonObject =JSONObject(responseData)
        return jsonObject.getString("access_token")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRequest(requestJson: String, url: String): String {
        var client = OkHttpClient()
        var mediaType = MediaType.parse("application/json")
        var body = RequestBody.create(mediaType, requestJson)
        var request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("content-type", "application/json")
            .addHeader("authorization", "Bearer "+getAccessToken())
            .addHeader("cache-control", "no-cache")
            .build()

        var response:Response=client.newCall(request).execute()
        val s  = response.body()?.string()
        System.out.println(s)
        return response.body()!!.toString()
    }


}