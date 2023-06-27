package com.example.investingguideandroidui.utilities

import com.example.investingguideandroidui.MainActivity

class SecurityType {

    constructor() {

    }


    fun securityTypeMapToInt() : HashMap<String,Int> {

        return hashMapOf<String, Int>(
            "bill" to MainActivity.BILL,
            "frn" to MainActivity.FRN,
            "tips" to MainActivity.TIPS,
            "note" to MainActivity.NOTE,
            "bond" to MainActivity.BOND,
            "cmb" to MainActivity.CMB
        )
    }

}