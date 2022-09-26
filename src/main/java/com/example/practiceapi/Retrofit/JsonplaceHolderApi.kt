package com.example.practiceapi.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface JsonplaceHolderApi {
    @get:GET("testApi/board/list")
    val data: Call<ArrayList<Model?>?>?

    //데이터를 요청하고 포스트로 주석 처리
    @FormUrlEncoded
    @POST("board")
    fun getPost(
        @Field("title") title: String?,
        @Field("content") text: String?,
        @Field("id_frt") id_frt: String?
    ): Call<ArrayList<Model?>?>?

    @FormUrlEncoded
    @PATCH("board/{seq}")
    fun getPatch(
        @Path("seq") seq: Int,
        @Field("title") title: String?,
        @Field("content") text: String?,
        @Field("id_lst") id_lst: String?
    ): Call<ArrayList<Model?>?>?

    @DELETE("board/{seq}")
    fun getDelete(@Path("seq") seq: Int): Call<ArrayList<Model?>?>?

    //=================================================================================================================
    @FormUrlEncoded
    @POST("user")
    fun getUserPost(
        @Field("username") username: String?, @Field("password") password: String?,
        @Field("email") email: String?, @Field("birth") birth: String?
    ): Call<ArrayList<User?>?>?

    @FormUrlEncoded
    @POST("userApi/loginForm")
    fun getLoginUserPost(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<String?>?


}