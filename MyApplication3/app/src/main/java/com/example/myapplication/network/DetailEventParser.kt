package com.example.myapplication.network

import android.util.Log
import android.util.Xml
import com.example.myapplication.data.DetailEvent
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class DetailEventParser {
    private val ns: String? = null

    companion object {
        val FAULT_RESULT = "faultResult"    // OpenAPI 결과에 오류가 있을 때에 생성하는 정보를 위해 지정
        val EVENT_INFO_TAG = "perforInfo"
        val SEQ_TAG = "seq"
        val TITLE_TAG = "title"
        val IMAGEURL_TAG = "imgUrl"
        val GPSX_TAG = "gpsX"
        val GPSY_TAG = "gpsY"
        val STARTDATE_TAG = "startDate"
        val ENDDATE_TAG = "endDate"
        val PLACE_TAG = "place"
        val PRICE_TAG = "price"
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream?) : DetailEvent {

        inputStream.use { inputStream ->
            val parser : XmlPullParser = Xml.newPullParser()

            /*Parser 의 동작 정의, next() 호출 전 반드시 호출 필요*/
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)

            /* Paring 대상이 되는 inputStream 설정 */
            parser.setInput(inputStream, null)

            /*Parsing 대상 태그의 상위 태그까지 이동*/
            while (parser.name != "msgBody") {
                parser.next()
            }

            return readEventInfo(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEventInfo(parser: XmlPullParser) : DetailEvent {
        var detailEvent: DetailEvent? = null
        //dailyBoxOffice가 여러 개 이니까 그걸 담을 하나의 DTO list 선언

        parser.require(XmlPullParser.START_TAG, ns, "msgBody")
        while(parser.next() != XmlPullParser.END_TAG) {
            //XmlPullParser.END_TAG는 DAILY_BOXOFFICE_TAG의 end Tag
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == EVENT_INFO_TAG) {
                detailEvent = readDailyEventInfo(parser)
            } else {
                skip(parser)
            }
        }
        //리스트 반환
        return detailEvent ?: DetailEvent(0, "", "", 0.0, 0.0, 0, 0, "", "")
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readDailyEventInfo(parser: XmlPullParser) : DetailEvent {
        parser.require(XmlPullParser.START_TAG, ns, EVENT_INFO_TAG)
        var seq : Int? = null
        var title : String? = null
        var imgUrl: String? = null
        var gpsX: Double? = null
        var gpsY: Double? = null
        var startDate: Int? = null
        var endDate: Int? = null
        var place: String? = null
        var price: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                SEQ_TAG -> seq = readTextInTag(parser, SEQ_TAG).toInt()
                TITLE_TAG -> title = readTextInTag(parser, TITLE_TAG)
                IMAGEURL_TAG -> imgUrl = readTextInTag(parser, IMAGEURL_TAG)
                GPSX_TAG -> {
                    val gpsXString = readTextInTag(parser, GPSX_TAG)
                    gpsX = if (gpsXString.isNotEmpty()) gpsXString.toDouble() else null
                }
                GPSY_TAG -> {
                    val gpsYString = readTextInTag(parser, GPSY_TAG)
                    gpsY = if (gpsYString.isNotEmpty()) gpsYString.toDouble() else null
                }
                STARTDATE_TAG -> startDate = readTextInTag(parser, STARTDATE_TAG).toInt()
                ENDDATE_TAG -> endDate = readTextInTag(parser, ENDDATE_TAG).toInt()
                PLACE_TAG -> place = readTextInTag(parser, PLACE_TAG)
                PRICE_TAG -> price = readTextInTag(parser, PRICE_TAG)
                else -> skip(parser)
            }
        }
        val TAG = "hi"
        Log.d(TAG, seq.toString())
        Log.d(TAG, title.toString())
        Log.d(TAG, imgUrl.toString())
        Log.d(TAG, gpsX.toString())
        Log.d(TAG, gpsY.toString())
        Log.d(TAG, startDate.toString())
        Log.d(TAG, endDate.toString())
        Log.d(TAG, place.toString())
        Log.d(TAG, price.toString())

        return DetailEvent (seq, title, imgUrl, gpsX, gpsY, startDate, endDate, place, price)
    }


    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTextInTag (parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        //전달하는 태그가 많으니까 매개변수로 tag를 받아야 함
        var text = ""
        //받은 태그가 시작 태그이기 때문에 next()하면 바로 TEXT 나와야 됨
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, ns, tag)
        //nextTag()한 게 end tag가 맞는지 검사
        return text
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        //스킵은 무조건 스타트 태그~앤드 태그
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}