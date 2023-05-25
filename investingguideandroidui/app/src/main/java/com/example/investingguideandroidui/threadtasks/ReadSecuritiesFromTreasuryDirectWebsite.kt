package com.example.investingguideandroidui.threadtasks

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

        var tempUrl : String = MainActivity.BASE_URL
        tempUrl += "?"
        tempUrl += "format=" + MainActivity.format
        //tempUrl += "&securityType=" + MainActivity.securityType
        tempUrl += "&startDate=" + MainActivity.startDate
        tempUrl += "&endDate=" + MainActivity.endDate
        tempUrl += "&dateFieldName=" + MainActivity.dateFieldName
        val webUrl : String = tempUrl
        // get data from server
        try {

            val urlObject : URL = URL(webUrl)
            val inputStream: InputStream = urlObject.openStream()
            val scan: Scanner = Scanner(inputStream)
            var webResult : String = ""
            while (scan.hasNext())
                webResult += scan.nextLine()

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