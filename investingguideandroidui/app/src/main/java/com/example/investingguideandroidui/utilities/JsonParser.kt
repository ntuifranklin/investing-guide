package com.example.investingguideandroidui.utilities

import android.util.Log
import com.example.investingguideandroidui.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import com.example.investingguideandroidui.models.Security
import org.json.JSONException

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
           try {
               var securityObject : JSONObject = jsonArray.optJSONObject(index)
               var security: Security = Security()
               security.setJsonRawObject(jsonArray.optString(index, "{}"))
               var st : String = securityObject.getString("securityType")
               security.setSecurityType(st)
               if (st.lowercase() != "note")
                securityObject.getString("pricePer100").toDouble()
               else
                security.setPricePer100(0.0)
               security.setIssueDate(securityObject.getString("issueDate"))
               security.setCusip(securityObject.getString("cusip"))

               securities.add(security)

           } catch (e : JSONException) {

               var securityObject : JSONObject = jsonArray.optJSONObject(index)
               val s : String = securityObject.getString("securityType")
               Log.w(MainActivity.LOG_TAG_EXTERIOR,"Json parsing failed for ${s}")
           }
        }


        return securities
    }
}