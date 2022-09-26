package com.example.practiceapi.ActivityCollection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import com.example.practiceapi.databinding.ActivityWebviewBinding


class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebviewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        jsProperty()

        binding.webview.loadUrl("http://192.168.10.160:3000/")

    }//end of onCreate

    fun jsProperty() {
        binding.webview.settings.useWideViewPort = true // wide Viewport 설정 아래 OverViewMode와 같이 써야합니다.
        binding.webview.settings.loadWithOverviewMode = false //WebView 화면 크기에 맞춰 설정
        binding.webview.settings.setSupportZoom(true) //줌 설정 여부를 물어보는것인데 저는 true를
        binding.webview.settings.builtInZoomControls = true //줌 확대
//        binding.webview.apply {
//            webViewClient = WebViewClient() //새 창을 안띄운다.
//            webChromeClient = WebChromeClient() //크롬 이용하기
//        }
        binding.webview.settings.javaScriptEnabled = true //javascript 사용여부
        binding.webview.settings.javaScriptCanOpenWindowsAutomatically = true //javascript가 window.open()을 사용할 수 있게 설정
        binding.webview.settings.domStorageEnabled = true

        binding.webview.addJavascriptInterface(WebAppInterface( this),  "shin")
    }

    class WebAppInterface( private val activity: WebViewActivity) {
        //자바스크립트 코드와, 안드로이드 코드간의 새 인터페이스를 결합하기 위해서 호출
        @JavascriptInterface
        fun showToast(toast:String) {
            val intent = Intent()
            intent.putExtra("webViewPW", toast)
            activity.setResult(RESULT_OK, intent)
            activity.finish()
            Log.d("shin","${toast}")

        }
    }
}//end of class

