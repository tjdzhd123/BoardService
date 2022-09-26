package com.example.practiceapi.ActivityCollection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.practiceapi.R
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import com.example.practiceapi.databinding.ActivityLoginmainBinding
import com.example.practiceapi.prefs.App
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    val WEB_REQUEST_CODE = 10

    lateinit var binding: ActivityLoginmainBinding
    val gson : Gson = GsonBuilder()
        .setLenient()
        .create()
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityLoginmainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //로그인 버튼
        binding.mainLoginBtn.setOnClickListener {
            getLoginUserPost(
                binding.editLoginID.text.toString(),
                binding.editLoginPW.text.toString(),
            )
        }
        //회원가입 버튼
        binding.mainSignBtn.setOnClickListener {
            var intent = Intent (this, SignUpActivity::class.java)
            startActivity(intent)
        }
//        비밀번호 버튼 클릭시 화면전환(startActivity)
//        binding.editLoginPW.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View?) {
//                showWebViewDialog("http://192.168.10.160:3000/")
////                var intent = Intent(this@LoginActivity, WebViewActivity::class.java)
////                startActivity(intent)
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//        })
        //get, put형식으로 js에서 받은 데이터 확인
        val webViewPW = intent.getStringExtra("webViewPW")
        Log.d("shin", "잘받아졌는지? :${webViewPW}")
        binding.editLoginPW.setText(webViewPW) //password칸에 넣기
    }//end of onCreate

    //로그인시 게시판 리스트 가져오기
    private fun getLoginUserPost(username: String, password:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.10.160:8081/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call = jsonplaceHolderApi.getLoginUserPost(username, password)
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                Log.d("shin", "${response.body()}")
                binding.editLoginID.setText("")
                binding.editLoginPW.setText("")
                App.prefs.setString("token", "${response.body()}")
                Log.d("shin", "${App.prefs.getString("token", "").toString()}")
                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }//end of getLoginUserPost

    //다이얼로그창으로 웹뷰 올리기 2번째 방법
//==================================================================================================
//private fun showWebViewDialog(url: String) {
//    val webView = WebView(this).apply {
//        loadUrl("http://192.168.10.160:3000/")
//        webViewClient = object : WebViewClient() {
//            @SuppressLint("JavascriptInterface")
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view!!.loadUrl("http://192.168.10.160:3000/")
//                view.settings.javaScriptEnabled = true
//                view.settings.javaScriptCanOpenWindowsAutomatically = true
//                view.settings.domStorageEnabled = true
//                view.addJavascriptInterface(webAppInterface(this@LoginActivity), "shin")
//                return true
//            }
//        }
//    }
//    val wrapper = LinearLayout(this)
//    val keyboardHack = EditText(this)
//    wrapper.orientation = LinearLayout.VERTICAL;
//    wrapper.addView(webView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//    wrapper.addView(keyboardHack, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//    AlertDialog.Builder(this@LoginActivity)
//        .setTitle("WebView")
//        .setView(wrapper)
//        //안드로이드 컴포넌트라고 인식을 못한다. 웹뷰 자체를 넣어버리면 로드를 하는데
//        .setNegativeButton("취소") { dialog, _ ->
//            dialog.dismiss()
//        }
//        .show()
//    }
//    class webAppInterface(private val activity:LoginActivity) {
//        @JavascriptInterface
//        fun showToast(toast: String) {
//            Toast.makeText(activity,"${toast}", Toast.LENGTH_SHORT).show()
//            Log.d("shin", "${toast}")
//            Log.e("shin", "에러입니다.")
//        }
//    }
//==================================================================================================
    fun viewOnclick(view: View) {
        when(view.id) {
            R.id.editLoginPW -> {
                //새 액티비티를 열어줌으로 써 동시에 결과 값을 전달하기(양방향)
                val intent = Intent(this, WebViewActivity::class.java)
                //어디로 데이터를 주고받는지 알기 위해
                startActivityForResult(intent, WEB_REQUEST_CODE)
            }
        }
    }

    //인텐트로 요청되어진 다른액티비티에서 데이터를 가지고 왔을 때 불러주는 콜백 함수 입니다.
    //requestCode: 어느 액티비티에서  넘어왔는지 확인
    //resultCode: 요청한 데이터 성공유무를 확인( RESULT_Ok, RESULT_CANCEL)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when(requestCode) {
            WEB_REQUEST_CODE -> {
                if(resultCode == RESULT_OK) {
                    val webViewPW = intent!!.getStringExtra("webViewPW")
                    Log.d("shin", "잘받아졌는지? :${webViewPW}")
                    binding.editLoginPW.setText(webViewPW) //password칸에 넣기
                    //어떻게 요청하고 어떻게 받는지는 프로토콜이라고 한다. 프로토콜로 서로 약속을 정합니다.
                }
            }
        }
    }


}//end of class