package com.example.investingguideandroidui.threadtasks

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.models.Security
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class ReadSecuritiesFromTreasuryDirectWebsite : Thread {
    private lateinit var securities : ArrayList<Security>
    private lateinit var fromActivity : MainActivity


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
            //editor.clear()
            var oldWebResult : String?
            oldWebResult = pref.getString(MainActivity.SAVED_WEB_RESULT_KEY,"")
            var webResult : String = ""
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



            // Now we read the objects in the result and load them as Securities
            var jsonArray : JSONArray  = JSONArray(webResult)
            if ( securities == null)
                securities = ArrayList<Security>()
            for (index in 0 until jsonArray.length()) {
                var securityObject : JSONObject = jsonArray.optJSONObject(index)
                var security: Security = Security()
                security.setJsonRawObject(jsonArray.optString(index, "{}"))
                security.setPricePer100(securityObject.getString("pricePer100").toDouble())
                security.setIssueDate(securityObject.getString("issueDate"))
                security.setCusip(securityObject.getString("cusip"))
                security.setSecurityType(securityObject.getString("securityType"))
                securities.add(security)
            }


        } catch ( e: JSONException) {


        }
        fromActivity.runOnUiThread(displayTask)

    }


    inner class DisplayListOfSecurities : Runnable {

        override fun run() {
            fromActivity.displaySecuritiesList(securities)
        }
    }
}