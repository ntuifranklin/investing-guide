package com.example.investingguideandroidui.models

class Security {
    private lateinit var jsonRawObject : String
    private var issueDate : String = ""
    private var pricePer100 : Double = 0.0
    private var cusip : String = ""
    private var securityType : String = ""
    constructor() : this("1970-01-01", 99.65, "") {

    }

    constructor(issueDate: String, pricePer100: Double, jsonRawObject: String ) {
        setIssueDate(issueDate)
        setPricePer100(pricePer100)
        setJsonRawObject(jsonRawObject)
    }

    fun setSecurityType(securityType: String){
        this.securityType = securityType
    }


    fun setCusip(c: String) {
        cusip = c
    }


    fun setJsonRawObject(rawJson: String) {
        jsonRawObject = rawJson

    }
    fun setIssueDate(issueDate: String ) {
        this.issueDate = issueDate
    }

    fun setPricePer100(pp100: Double ) {
        this.pricePer100 = pp100
    }


    fun getSecurityType() : String {
        return securityType
    }

    fun getCusip() : String {
        return cusip
    }

    fun getIssueDate() : String {
        return issueDate
    }

    fun getPricePer100() : Double  {
       return pricePer100
    }

    fun getJsonRawObject() : String {
        return jsonRawObject
    }

}