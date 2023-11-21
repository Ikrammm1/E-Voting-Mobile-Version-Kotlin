package com.wisnu.evoting.Ui.Voting

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.wisnu.evoting.API.RetrofitClient
import com.wisnu.evoting.Model.ModelCandidates
import com.wisnu.evoting.Model.ModelResponse
import com.wisnu.evoting.R
import com.wisnu.evoting.Ui.Login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VotingActivity : AppCompatActivity() {
    lateinit var Adapter : AdapterCandidate
    lateinit var listItem : RecyclerView
    private lateinit var profil : SharedPreferences
    lateinit var IdVoter : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)
        profil = getSharedPreferences("Login_Session", MODE_PRIVATE)
        IdVoter = profil.getString("id", null).toString()
        setUpList()
    }
    override fun onStart(){
        super.onStart()
        getData()
    }

    private fun getData() {
        RetrofitClient.instance.Candidates().enqueue(object : Callback<ModelCandidates>{
            override fun onFailure(call: Call<ModelCandidates>, t: Throwable) {
                Toast.makeText(this@VotingActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                Log.e("Kesalahan API Kriteria : ", t.toString())
            }

            override fun onResponse(
                call: Call<ModelCandidates>,
                response: Response<ModelCandidates>
            ) {
                if (response.isSuccessful){
                    val ListData = response.body()!!.candidates
                    ListData.forEach {
                        Adapter.setData(ListData)
                    }
                } else{
                    Toast.makeText(this@VotingActivity, "Maaf Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun setUpList() {
        listItem = findViewById(R.id.listKriteria)
        Adapter = AdapterCandidate(arrayListOf(), object : AdapterCandidate.OnAdapterlistener{
            override fun onClick(vote: ModelCandidates.dataCandidate) {
                var alertDialog = AlertDialog.Builder(this@VotingActivity)
                    .setTitle("Apakah Anda Yakin Ingin Memilih Kandidat Ini?")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->

                        RetrofitClient.instance.Vote(IdVoter, vote.id.toString(), vote.position_id.toString()).enqueue(object :
                            Callback<ModelResponse> {
                            override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                                if (response.isSuccessful){
                                    val status = response.body()!!.status
                                    if (status == "true"){
                                        Toast.makeText(this@VotingActivity, "Berhasil Voting", Toast.LENGTH_SHORT).show()
                                        getData() //masuk ke hasil voting nanti//
                                    }else{
                                        var alertDialog = AlertDialog.Builder(this@VotingActivity)
                                            .setTitle("Anda Sudah Memilih")
                                            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->  })
                                            .show()
                                    }


                                }
                            }

                            override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                                Toast.makeText(this@VotingActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                                Log.e("Kesalahan API Vote", t.toString())
                            }

                        })

                    })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->  })
                    .show()
            }

        })
        listItem.adapter = Adapter
    }
}
