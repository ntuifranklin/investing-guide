package com.example.investingguideandroidui.threadtasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.models.Security
import org.json.JSONException
import android.util.Log
import com.example.investingguideandroidui.SecuritiesViewActivity
import java.io.InputStream
import java.net.URL
import java.util.*

class ReadSecuritiesFromTreasuryDirectWebsite : Thread {

    private lateinit var fromActivity : MainActivity
    private var webResult : String = ""
    private var searchRoute : String = "search"

    constructor(activity: MainActivity, search_route : String = "search") {
        this.fromActivity = activity

        searchRoute = search_route

    }

    override fun run () {
        super.run()
        var displayTask : ProcesAndViewWebResult = ProcesAndViewWebResult()

        var tempUrl : String = fromActivity.BASE_URL

        tempUrl += "/${searchRoute}?"
        tempUrl += "format=" + fromActivity.format
        //tempUrl += "&securityType=" + MainActivity.securityType
        tempUrl += "&startDate=" + fromActivity.startDate
        tempUrl += "&endDate=" + fromActivity.endDate
        if (fromActivity.securityType != null && fromActivity.securityType.length > 0)
            tempUrl += "&securityType=${fromActivity.securityType}"
        if (searchRoute == MainActivity.AUCTIONED_ROUTE)
            tempUrl += "&dateFieldName=" + fromActivity.auctionDateFieldName
        else
            tempUrl += "&dateFieldName=" + fromActivity.issueDateFieldName
        val webUrl : String = tempUrl
        // get data from server
        try {

            Log.w(fromActivity.LOG_TAG,webUrl)
            val urlObject : URL = URL(webUrl)
            val inputStream: InputStream = urlObject.openStream()
            val scan: Scanner = Scanner(inputStream)

            while (scan.hasNext())
                webResult += scan.nextLine()
            inputStream.close()


        } catch ( e: Exception) {
            Log.w(fromActivity.LOG_TAG, "Error in Class : ${this.name}, Thread class pulling data from remote server")

        }
        fromActivity.runOnUiThread(displayTask)

    }


    inner class ProcesAndViewWebResult : Runnable {

        override fun run() {
            fromActivity.saveWebResult(webResult)
        }
    }
}