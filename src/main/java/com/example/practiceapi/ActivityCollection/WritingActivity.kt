package com.example.practiceapi.ActivityCollection

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practiceapi.R
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import com.example.practiceapi.Retrofit.Model
import com.example.practiceapi.databinding.ActivityWritingBinding
import com.example.practiceapi.prefs.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WritingActivity : AppCompatActivity() {
    lateinit var binding: ActivityWritingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.writingToolbar))
        supportActionBar!!.title = "작성하기"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.writingBtn.setOnClickListener{
            if (binding.writingID.toString().isEmpty() && binding.writingTitle.toString()
                    .isEmpty() && binding.writingContent.toString().isEmpty()
            ) {
                Toast.makeText(this@WritingActivity, "내용을 넣어주세요", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@WritingActivity, "등록 완료", Toast.LENGTH_SHORT).show()
            }
            postData(
                binding.writingTitle.text.toString(),
                binding.writingContent.text.toString(),
                binding.writingID.text.toString()
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    } //end of onCreate
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                var intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
//                App.prefs.editor.remove("token").apply()
//                Log.d("shin", "token1 : ${App.prefs.getString("token","").toString()}")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun provideOkHttpClient(
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    val interceptor = Interceptor { chain ->
        with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer " + App.prefs.getString("token",""))
                .build()
            proceed(newRequest)
        }
    }//interceptor
    

    private fun postData(title: String, text: String, id_frt: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.10.160:8081/testApi/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call = jsonplaceHolderApi.getPost(title, text, id_frt)
        call?.enqueue(object : Callback<ArrayList<Model?>?> {
            override fun onResponse(
                call: Call<ArrayList<Model?>?>,
                response: Response<ArrayList<Model?>?>
            ) {
                binding.writingID.setText("")
                binding.writingTitle.setText("")
                binding.writingContent.setText("")
            }

            override fun onFailure(call: Call<ArrayList<Model?>?>, t: Throwable) {}
        })
    }
} //end of WritingActivity
