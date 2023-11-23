package com.wisnu.evoting.API

import com.wisnu.evoting.Model.ModelCandidates
import com.wisnu.evoting.Model.ModelHasil
import com.wisnu.evoting.Model.ModelResponse
import com.wisnu.evoting.Model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("nim") nim : String,
        @Field("password") password : String
    ) : Call<ResponseLogin>

    @GET("HasilVote.php")
    fun Result():Call<List<ModelHasil>>

    @FormUrlEncoded
    @POST("ListCandidate.php")
    fun Candidates(
        @Field("voters_id") voters_id : String
    ):Call<ModelCandidates>

    @FormUrlEncoded
    @POST("pilihanSaya.php")
    fun PilihanSaya(
        @Field("voters_id") voters_id : String
    ):Call<ModelHasil>

    @FormUrlEncoded
    @POST("vote.php")
    fun Vote(
        @Field("voters_id") voters_id : String,
        @Field("candidate_id") candidate_id : String,
    ) : Call<ModelResponse>

    @FormUrlEncoded
    @POST("EditUser.php")
    fun EditUser(
        @Field("voters_id") voters_id : String,
        @Field("fullname") fullname : String,
        @Field("password") password : String
    ) : Call<ModelResponse>
}