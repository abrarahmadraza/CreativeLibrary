package com.example.creativelibraryactivity

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

class AndroidInterface(context: Context) {
    private  val TAG = "AndroidInterface"
    private var mListener:SelectImageButtonClickListener = context as MainActivity




    @JavascriptInterface
    fun onButtonsClicked(link:String)
    {
        Log.d(TAG, "onButtonsClicked: $link")
        mListener.onSelectImageButtonClicked(url = link)
    }

    interface SelectImageButtonClickListener{

        fun onSelectImageButtonClicked(url:String)

    }
}