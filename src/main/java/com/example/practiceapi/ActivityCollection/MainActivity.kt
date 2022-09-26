package com.example.practiceapi.ActivityCollection

import android.R
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practiceapi.RecyclerDecoration
import com.example.practiceapi.RecyclerViewAdapter
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import com.example.practiceapi.Retrofit.Model
import com.example.practiceapi.WritingData
import com.example.practiceapi.databinding.ActivityMainBinding
import com.example.practiceapi.prefs.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isFabOpen = false
    lateinit var adapter: RecyclerViewAdapter
    var writingList: MutableList<WritingData> = mutableListOf<WritingData>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //툴바 설정하기
        setSupportActionBar(binding.boardToolbar)
        supportActionBar?.setTitle("Crizen Notice Board")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //토글이벤트
        binding.fabMain.setOnClickListener {
            toggleFab()
        }
        //플로팅버튼 이벤트
        binding.fabAdd.setOnClickListener {
            var intent = Intent(this, WritingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        //Retrofit
        writingRetrofit()

    } //end of onCreate
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                App.prefs.editor.remove("token").apply()
                Log.d("shin", "token1 : ${App.prefs.getString("token","").toString()}")

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

    fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabAdd, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 45f, 0f).apply { start() }
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            ObjectAnimator.ofFloat(binding.fabAdd, "translationY", -250f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 0f, 45f).apply { start() }
        }
        isFabOpen = !isFabOpen
    }
    //Retrofit Get
     fun writingRetrofit() {
        //Retrofit Builder
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.10.160:8081/")
            .baseUrl("http://192.168.10.160:8081/")
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // instance for interface
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call = jsonplaceHolderApi.data
        call?.enqueue(object : Callback<ArrayList<Model?>?> {
            override fun onResponse(call: Call<ArrayList<Model?>?>, response: Response<ArrayList<Model?>?>) {
                val models = response.body()!!
                for(model in models) {
                    val seq = model?.seq
                    val id = model?.id_frt
                    val title = model?.title
                    val content = model?.text
                    var dateStr = "날짜 : "+model?.dt

                    val writingData = WritingData(seq, id, title, content, dateStr!!)
                    writingList.add(writingData)
                    adapter = RecyclerViewAdapter(this@MainActivity, writingList)
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.addItemDecoration(RecyclerDecoration(this@MainActivity))
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                }

            }
            override fun onFailure(call: Call<ArrayList<Model?>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }//end of retrofit



} //end of MainActivity