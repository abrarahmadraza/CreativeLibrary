package com.example.creativelibraryactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AndroidInterface.SelectImageButtonClickListener {
    companion object {
        private const val TAG = "MainActivity"
        private const val MAIN_URL = "https://marketingmobile.surge.sh/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web_view_home.apply {
            addJavascriptInterface(AndroidInterface(this@MainActivity), "Android")
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    loadJS(web_view_home)
                    Log.d(TAG, "shouldOverrideUrlLoading: ${request?.url}")
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    loadJS(web_view_home)
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(
                    message: String?,
                    lineNumber: Int,
                    sourceID: String?
                ) {
                    super.onConsoleMessage(message, lineNumber, sourceID)

                    if (message != null && message.contains("I am the selected Image [object Object]")) {
                        loadJS(web_view_home)
                       // Log.d(TAG, "$CURRENT_IMG_URL")
                    } else if (message != null && message.contains("[object Object] selectedImage")) {
                        loadJS(web_view_home)
                    }
                }

            }
        }


        //WebSettings for WebView
        var webSettings = web_view_home.settings

        webSettings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            useWideViewPort = true
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            setAppCacheEnabled(false)
//            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        }


        web_view_home.loadUrl(MAIN_URL)
    }

    private fun loadJS(webView: WebView) {
        webView.loadUrl(
            """javascript:(function f() {
        var divs = document.getElementsByClassName('ImageCard_CardItem__7WuK1');
        for (var i = 0, n = divs.length; i < n; i++) {
             
            divs[i].addEventListener('click',function(){
               var images =this.getElementsByTagName('img');
               Android.onButtonsClicked(images[0].src);
            })
          
        }
        
         var divs2 = document.getElementsByClassName('ImagePreviewCard_previewModalClose__mdK28');
        for (var i = 0, n = divs2.length; i < n; i++) {
             
            divs2[i].addEventListener('click',function(){
               Android.onButtonsClicked("closeButton");
            })
          
        }
        
         var divs3 = document.getElementsByClassName('List_headerBackIcon__2dW_b');
        for (var i = 0, n = divs3.length; i < n; i++) {
             
            divs3[i].addEventListener('click',function(){
               Android.onButtonsClicked("goBack");
            })
          
        }
        
         var divs4 = document.getElementsByClassName('ImagePreviewCard_SelectButton__1WnvC');
        for (var i = 0, n = divs4.length; i < n; i++) {
             
            divs4[i].addEventListener('click',function(){
            var container=document.getElementsByClassName('ImagePreviewCard_previewImageContainer__3TVJ5');
            var images = container[0].getElementsByTagName("img");
               Android.onButtonsClicked(images[1].src);
            })
          
        }
        
      })()"""
        )
    }

    override fun onSelectImageButtonClicked(url: String) {
        Log.d(TAG, "onSelectImageButtonClicked: $url")
        if (url == "goBack") {
            GlobalScope.launch(Dispatchers.Main) {
                onBackPressed()

            }
        }
        if (url == "closeButton") {
            web_view_home.reload()
        }
        else
        {
            /** Return This Url
            val intent=Intent()
            intent.putExtra("resultUrl",url)
            setResult(Activity.RESULT_OK,intent)
            finish()

             **/

        }

    }

    override fun onBackPressed() {
        if (web_view_home.canGoBack())
            web_view_home.goBack()
        else
            super.onBackPressed()


    }
}
