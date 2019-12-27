package com.janfranco.kotlinfixercurrency

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getRates(@Suppress("UNUSED_PARAMETER")view : View) {
        val download = Download()
        try {
                val url = "http://data.fixer.io/api/latest?access_key=02bd1b7b5f441668e9463d2dc40d7503&format=1"
            download.execute(url)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    inner class Download : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            var result = ""
            var url : URL
            var httpConnection : HttpURLConnection

            return try {
                url = URL(params[0])
                httpConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                var data = inputStreamReader.read()
                while (data > 0) {
                    val character = data.toChar()
                    result += character
                    data = inputStreamReader.read()
                }
                result
            } catch (e : Exception) {
                e.printStackTrace().toString()
            }
        }

        override fun onPostExecute(result: String?) {
            try {
                val jsonObject = JSONObject(result)
                val rates = jsonObject.getString("rates")
                val jsonObject2 = JSONObject(rates)
                val tl = jsonObject2.getString("TRY")
                val usd = jsonObject2.getString("USD")
                val cad = jsonObject2.getString("CAD")

                val sb = StringBuilder()
                textView2.text = sb.append(usd).append(" USD")
                sb.clear()
                textView3.text = sb.append(cad).append(" CAD")
                sb.clear()
                textView4.text = sb.append(tl).append(" TRY")
            } catch (e : Exception) {
                e.printStackTrace()
            }
            super.onPostExecute(result)
        }

    }
}
