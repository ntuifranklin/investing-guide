package com.example.investingguideandroidui

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.util.Log
import com.example.investingguideandroidui.models.Security

class JsonParser {
    private var webResult : String = ""
    private lateinit var securities : ArrayList<Security>
    constructor() {
        webResult = ""
        securities = ArrayList<Security>()

    }

    fun parseString(s : String ) : ArrayList<Security>{
        webResult = s
        securities = ArrayList<Security>()
        var jsonArray : JSONArray  = JSONArray(webResult)

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


        return securities
    }
}