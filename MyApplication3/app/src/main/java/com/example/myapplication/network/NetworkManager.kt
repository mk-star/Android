package com.example.myapplication.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.data.Event
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.jvm.Throws
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NetworkManager(val context: Context) {
    private val TAG = "NetworkManager"

    //api 주소
    val openApiUrl by lazy {
        //strings.xml에 있는 애
        val currentDate = Date()


        // 1주일 후 날짜 계산
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val oneWeekLaterDate = calendar.time

        // 날짜를 "yyyyMMdd" 형식으로 포맷팅
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val fromDate = dateFormat.format(currentDate)
        val toDate = dateFormat.format(oneWeekLaterDate)

        // 사용할 URL 구성
        val baseApiUrl = context.resources.getString(R.string.kobis_url)
        val apiUrlWithParams = "$baseApiUrl&from=$fromDate&to=$toDate"

        Log.d(TAG, apiUrlWithParams)

        apiUrlWithParams
    }

    @Throws(IOException::class)
    fun downloadXml() : List<Event>? {
        var events : List<Event>? = null

        val inputStream = downloadUrl( openApiUrl)

        /*Parser 생성 및 parsing 수행*/
        val parser = EventInfoParser()
        events = parser.parse(inputStream)

        return events
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