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
        @Field("voter") voter : String,
        @Field("password") password : String
    ) : Call<ResponseLogin>

    @GET("HasilVote.php")
    fun Result():Call<List<ModelHasil>>

    @GET("ListCandidate.php")
    fun Candidates():Call<ModelCandidates>

    @FormUrlEncoded
    @POST("vote.php")
    fun Vote(
        @Field("voters_id") voters_id : String,
        @Field("candidate_id") candidate_id : String,
        @Field("position_id") position_id : String
    ) : Call<ModelResponse>

    @FormUrlEncoded
    @POST("EditUser.php")
    fun EditUser(
        @Field("voters_id") voters_id : String,
        @Field("firstname") firstname : String,
        @Field("lastname") lastname : String
    ) : Call<ModelResponse>
}