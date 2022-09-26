package com.example.practiceapi.ActivityCollection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import com.example.practiceapi.Retrofit.User
import com.example.practiceapi.databinding.ActivitySignUpBinding
import com.example.practiceapi.prefs.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val year = arrayOf("1951","1952","1953","1954","1955","1956","1957","1958","1959","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969"
        ,"1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989"
        ,"1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010")
    private val month = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12")
    private val day = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24",
        "25","26","27","28","29","30")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signUpBtn.setOnClickListener {
            if(binding.editID.toString().isEmpty() && binding.editPW.toString().isEmpty() &&
                binding.editName.toString().isEmpty()) {
                Toast.makeText(this, "내용을 넣어주세요", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
            }
            postUserData(
                binding.editID.text.toString(),
                binding.editPW.text.toString(),
                binding.editName.text.toString(),
                binding.birthText.text.toString()
            )
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var yearAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, year)
        binding.yearSpinner.adapter = yearAdapter
        binding.yearSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                binding.birthText.setText(year.get(position))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var monthAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, month)
        binding.monthSpinner.adapter = monthAdapter
        binding.monthSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
               binding.birthText.append(month.get(position))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var dayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day)
        binding.daySpinner.adapter = dayAdapter
        binding.daySpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                binding.birthText.append(day.get(position))
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

     }//end of onCreate


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

    private fun postUserData(username: String, password :String, email: String, birth: String) {
        Log.d("shin","1번")
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.10.160:8080/")
            .baseUrl("http://192.168.10.160:8081/userApi/")
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        Log.d("shin","2번")
        val call = jsonplaceHolderApi.getUserPost(username, password, email, birth)
        call?.enqueue(object : Callback<ArrayList<User?>?>{
            override fun onResponse(
                call: Call<ArrayList<User?>?>,
                response: Response<ArrayList<User?>?>
            ) {
               binding.editID.setText("")
               binding.editPW.setText("")
               binding.editName.setText("")
            }

            override fun onFailure(call: Call<ArrayList<User?>?>, t: Throwable) {

            }
        })
    }




















}//end of class