package com.example.myapplication.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.data.DetailEvent
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.jvm.Throws

class DetailNetworkManager(val context: Context) {
    private val TAG = "DetailNetworkManager"

    //api 주소
    val openApiUrl by lazy {
        //strings.xml에 있는 애
        val baseApiUrl = context.resources.getString(R.string.detail_url)
        "$baseApiUrl&seq="
    }

    @Throws(IOException::class)
    fun downloadXml(seq: Int?): DetailEvent? {
        val url = "$openApiUrl$seq"
        var detailEvent : DetailEvent? = null

        val inputStream = downloadUrl(url)

        Log.d(TAG, "뭐야씨발~~~~~~~~~~~~??????????????")
        Log.d(TAG, url)

        /*Parser 생성 및 parsing 수행*/
        val parser = DetailEventParser()
        detailEvent = parser.parse(inputStream)
        Log.d(TAG, "Network Response: $detailEvent")

        return detailEvent
    }


    @Throws(IOException::class)
    private fun downloadUrl(urlString: String) : InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            //=(readurl.openConnection() as? HttpURLConnection).readTimeout
            readTimeout = 5000
            connectTimeout = 5000
            requestMethod = "GET"
            doInput = true
            //연결
            connect()
            //만들어진 inputStream 반환
            inputStream
        }
    }

    //이 부분 필요 없음
    // InputStream 을 String 으로 변환
    private fun readStreamToString(iStream : InputStream?) : String {
        val resultBuilder = StringBuilder()

        val inputStreamReader = InputStreamReader(iStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        var readLine : String? = bufferedReader.readLine()
        while (readLine != null) {
            resultBuilder.append(readLine + System.lineSeparator())
            readLine = bufferedReader.readLine()
        }

        bufferedReader.close()
        return resultBuilder.toString()
    }


    // InputStream 을 Bitmap 으로 변환
    private fun readStreamToImage(iStream: InputStream?) : Bitmap {
        val bitmap = BitmapFactory.decodeStream(iStream)
        return bitmap
    }

}