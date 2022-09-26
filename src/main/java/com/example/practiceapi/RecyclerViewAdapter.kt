package com.example.practiceapi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapi.ActivityCollection.MainActivity
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import com.example.practiceapi.Retrofit.Model
import com.example.practiceapi.databinding.ItemViewBinding
import com.example.practiceapi.prefs.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RecyclerViewAdapter(val context: Context, var writingList: MutableList<WritingData>): RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {
    val itembinding = ItemViewBinding.inflate(LayoutInflater.from(context))
    lateinit var adapter: RecyclerViewAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomViewHolder(binding)
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

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        binding.infoSeq.text = writingList[position].seq.toString()
        binding.infoId.text = writingList[position].id
        binding.infoTitle.text = writingList[position].title
        binding.infoContents.text = writingList[position].content
        binding.infoDate.text = writingList[position].date.toString()

        binding.infoUpdate.setOnClickListener {
            binding.updateSeq.text = writingList[position].seq.toString()
            binding.updateId.setText(writingList.get(position).id)
            binding.updateContents.setText(writingList.get(position).content)
            binding.updateoDate.text = writingList[position].date.toString()
            binding.infoLayout.visibility = View.GONE
            binding.updateLayout.visibility = View.VISIBLE

        }
        //Update 버튼눌렀을 때 보내지는 patchData
        binding.updateBtn.setOnClickListener {
            patchData(
                binding.updateSeq.text.toString().toInt(),
                binding.updateTitle.text.toString(),
                binding.updateContents.text.toString(),
                binding.updateId.text.toString()
            )
            binding.infoLayout.visibility = View.VISIBLE
            binding.updateLayout.visibility = View.GONE
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
        //Delete 버튼
        binding.deleteBtn.setOnClickListener {
            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show()
            deleteData(binding.infoSeq.text.toString().toInt())
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }

    }//end of onBindview
    override fun getItemCount(): Int {
        return writingList.size
    }
    class CustomViewHolder(val binding: ItemViewBinding):
        RecyclerView.ViewHolder(binding.root)
    //PATCH
    fun patchData(seq: Int, title: String, text: String, id_lst: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.10.160:8081/testApi/")
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call: Call<ArrayList<Model?>?>
        call = jsonplaceHolderApi.getPatch(seq, title, text, id_lst)!!
        call.enqueue(object : Callback<ArrayList<Model?>?> {
            override fun onResponse(call: Call<ArrayList<Model?>?>, response: Response<ArrayList<Model?>?>) {
                itembinding.updateTitle.setText("")
                itembinding.updateContents.setText("")

            }
            override fun onFailure(call: Call<ArrayList<Model?>?>, t: Throwable) {
            }
        })
    }//end of patchData

    //DELETE
    fun deleteData(seq:Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.10.160:8081/testApi/")
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call: Call<ArrayList<Model?>?> = jsonplaceHolderApi.getDelete(seq)!!
        call.enqueue(object : Callback<ArrayList<Model?>?> {
            override fun onResponse(
                call: Call<ArrayList<Model?>?>,
                response: Response<ArrayList<Model?>?>
            ) {
                itembinding.infoSeq.text.toString()
            }

            override fun onFailure(call: Call<ArrayList<Model?>?>, t: Throwable) {
            }
        })
    }




}//end of

