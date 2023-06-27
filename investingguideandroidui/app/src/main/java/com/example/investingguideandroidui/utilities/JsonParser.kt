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
               //Log.w(MainActivity.LOG_TAG_EXTERIOR,"security object : $securityObject ")
               var security: Security = Security()
               security.setJsonRawObject(jsonArray.optString(index, "{}"))
               var st : String = securityObject.getString("securityType")
               security.setSecurityType(st)
               var pp100: String = securityObject.getString("pricePer100")
               if (pp100 != null && pp100.length > 0 )
                   security.setPricePer100(pp100.toDouble())
               else
                security.setPricePer100(0.0)
               security.setIssueDate(securityObject.getString("issueDate"))
               security.setCusip(securityObject.getString("cusip"))

               securities.add(security)

           } catch (e : JSONException) {

               var securityObject : JSONObject = jsonArray.optJSONObject(index)
               val s : String = securityObject.getString("securityType")
               Log.w(MainActivity.LOG_TAG_EXTERIOR,"Json parsing failed for ${s}")
               continue
           }
        }


        return securities
    }
}