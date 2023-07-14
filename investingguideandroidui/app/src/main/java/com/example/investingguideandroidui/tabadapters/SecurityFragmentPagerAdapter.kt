package com.example.investingguideandroidui.tabadapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.SecuritiesViewActivity
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.utilities.SecurityType

class SecurityFragmentPagerAdapter : FragmentPagerAdapter {

    val hm : HashMap<String, Int> = SecurityType().securityTypeMapToInt()

    lateinit var securitys : ArrayList<Security>
    lateinit var fromActivity : SecuritiesViewActivity

    constructor(fm: FragmentManager, from: SecuritiesViewActivity, secs: ArrayList<Security>) : super(fm) {
        fromActivity = from
        securitys = secs
    }
    override fun getCount(): Int {
        return SecurityType().securityTypeMapToInt().size
    }

    override fun getItem(position: Int): Fragment {
            return DisplaySecurityCardFragment(fromActivity,securitys, position)
    }

    override fun getPageTitle(position: Int): CharSequence? {

        for ( (secType, secId) in hm  )
            if ( position == secId )
                return secType
        return "SecurityTypeNotFound"
    }

    fun setSecurities(secs : ArrayList<Security>) {
        securitys = secs
    }

}