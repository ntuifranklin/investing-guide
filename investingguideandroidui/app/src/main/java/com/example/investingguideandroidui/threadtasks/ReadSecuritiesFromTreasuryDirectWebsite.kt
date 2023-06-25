package com.example.investingguideandroidui.threadtasks

import android.content.Context
import android.content.SharedPreferences

import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.models.Security
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.util.Log
import com.example.investingguideandroidui.JsonParser
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class ReadSecuritiesFromTreasuryDirectWebsite : Thread {
    private lateinit var securities : ArrayList<Security>
    private lateinit var fromActivity : MainActivity
    private var webResult : String = ""

    constructor(activity: MainActivity) {
        this.fromActivity = activity
        securities = ArrayList<Security>()

    }

    override fun run () {
        super.run()
        var displayTask : DisplayListOfSecurities = DisplayListOfSecurities()

        var tempUrl : String = fromActivity.BASE_URL
        tempUrl += "?"
        tempUrl += "format=" + fromActivity.format
        //tempUrl += "&securityType=" + MainActivity.securityType
        tempUrl += "&startDate=" + fromActivity.startDate
        tempUrl += "&endDate=" + fromActivity.endDate
        tempUrl += "&dateFieldName=" + fromActivity.dateFieldName
        val webUrl : String = tempUrl
        // get data from server
        try {

            Log.w(fromActivity.LOG_TAG,webUrl)

            var pref : SharedPreferences = fromActivity.getSharedPreferences(fromActivity.APP_UNIQUE_ID, Context.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.clear()
            var oldWebResult : String?
            oldWebResult = pref.getString(MainActivity.SAVED_WEB_RESULT_KEY,"")

            if ( oldWebResult == null || oldWebResult.length == 0 ) {
                Log.w(fromActivity.LOG_TAG,"No previous result saved. Getting fresh data")
                val urlObject : URL = URL(webUrl)
                val inputStream: InputStream = urlObject.openStream()
                val scan: Scanner = Scanner(inputStream)

                while (scan.hasNext())
                    webResult += scan.nextLine()

                editor.putString(MainActivity.SAVED_WEB_RESULT_KEY, webResult)
                editor.commit()

                inputStream.close()
            } else {
                webResult = oldWebResult
                Log.w(fromActivity.LOG_TAG,"Reusing prevuious data : " + webResult.substring(0,20))
            }





        } catch ( e: JSONException) {


        }
        fromActivity.runOnUiThread(displayTask)

    }


    inner class DisplayListOfSecurities : Runnable {

        override fun run() {
            fromActivity.parseWebResult(webResult)
            fromActivity.setPagerAndTabs()
            fromActivity.displaySecuritiesList()
        }
    }
}