package com.wisnu.evoting.Ui.Hasil

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.wisnu.evoting.API.APIService
import com.wisnu.evoting.API.RetrofitClient
import com.wisnu.evoting.Model.ModelCountDown
import com.wisnu.evoting.Model.ModelHasil
import com.wisnu.evoting.Model.ModelResponse
import com.wisnu.evoting.R
import com.wisnu.evoting.Ui.Dashboard.Dashboard
import com.wisnu.evoting.Ui.Login.LoginActivity
import com.wisnu.evoting.Ui.Voting.VotingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HasilActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart
    var highestVoteName: String? = null
    lateinit var NamaCandidate : TextView
    private lateinit var BtnLihat : TextView
    private lateinit var pilihan : String
    private lateinit var nimpilihan : String
    private lateinit var idpilihan : String
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var profil : SharedPreferences
    lateinit var BtnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil)
        barChart = findViewById(R.id.barChart)
        NamaCandidate = findViewById(R.id.NamaCandidate)
        BtnLihat = findViewById(R.id.btnLihat)
        profil = getSharedPreferences("Login_Session", MODE_PRIVATE)
        BtnBack = findViewById(R.id.btnBack)

        BtnBack.setOnClickListener {
            this.finish()
        }

        BtnLihat.setOnClickListener {
            RetrofitClient.instance.PilihanSaya(profil.getString("id", null).toString()).enqueue(object : Callback<ModelHasil>{
                override fun onFailure(call: Call<ModelHasil>, t: Throwable) {
                    Toast.makeText(this@HasilActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                    Log.e("Kesalahan API Pilihan Saya : ", t.toString())
                }

                override fun onResponse(
                    call: Call<ModelHasil>,
                    response: Response<ModelHasil>
                ) {
                    idpilihan = response.body()!!.votes_id

                    if (response.body()!!.nim == null){
                        pilihan = "Anda Belum Memilih"
                        nimpilihan = ""
                    }else{
                        nimpilihan = response.body()!!.nim
                        pilihan = "${response.body()!!.nim} ${response.body()!!.fullname}"
                    }
                    var alertDialog = AlertDialog.Builder(this@HasilActivity)
                        .setTitle(pilihan)
                    if (nimpilihan != ""){
                        alertDialog.setPositiveButton("Ubah Pilihan", DialogInterface.OnClickListener { dialog, which ->
                            RetrofitClient.instance.CountDown().enqueue(object : Callback<ModelCountDown>{
                                override fun onFailure(call: Call<ModelCountDown>, t: Throwable) {
                                    Toast.makeText(this@HasilActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(
                                    call: Call<ModelCountDown>,
                                    response: Response<ModelCountDown>
                                ) {
                                    if (response.isSuccessful){
                                        if (response.body()!!.sisa_waktu <=0){
                                            Toast.makeText(this@HasilActivity, "Waktu Pemilihan Telah Berakhir", Toast.LENGTH_SHORT).show()
                                        }else{
                                            var Dialog = AlertDialog.Builder(this@HasilActivity)
                                                .setTitle("Apakah Anda Yakin Ingin Merubah Pilihan?")
                                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                                                    RetrofitClient.instance.DeleteVote(idpilihan).enqueue(object : Callback<ModelResponse>{
                                                        override fun onFailure(
                                                            call: Call<ModelResponse>,
                                                            t: Throwable
                                                        ) {
                                                            Toast.makeText(this@HasilActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                                                            Log.e("Kesalahan API Delete : ", t.toString())
                                                        }

                                                        override fun onResponse(
                                                            call: Call<ModelResponse>,
                                                            response: Response<ModelResponse>
                                                        ) {

                                                            val update = Intent (this@HasilActivity, VotingActivity::class.java)
                                                            startActivity(update)
                                                        }

                                                    })

                                                })
                                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->  })
                                                .show()
                                        }
                                    }
                                }

                            })

                        })

                    }else{
                    }

                    alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->  })
                    alertDialog.show()
                }

            })

        }
        SetUpResult()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            SetUpResult()
            swipeRefreshLayout.isRefreshing = false
        }





    }
    private fun SetUpResult(){
        RetrofitClient.instance.Result().enqueue(object : Callback<List<ModelHasil>> {
            override fun onResponse(call: Call<List<ModelHasil>>, response: Response<List<ModelHasil>>) {
                if (response.isSuccessful) {
                    val chartDataList = response.body()
                    // Handle data dan tampilkan di bar chart
                    displayBarChart(chartDataList)
                } else {
                    Toast.makeText(this@HasilActivity, "Data Not Found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelHasil>>, t: Throwable) {
                Toast.makeText(this@HasilActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                Log.e("Kesalahan API Hasil : ", t.toString())
            }
        })
    }
    private fun displayBarChart(chartDataList: List<ModelHasil>?) {
        val barChart = findViewById<BarChart>(R.id.barChart)

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        var highestVoteCount = 0

        for ((index, data) in chartDataList!!.withIndex()) {
            entries.add(BarEntry(index.toFloat(), data.jml_vote, data.fullname))
            labels.add(data.fullname)

            val voteCount = data.jml_vote.toInt()

            // Check if the current vote count is higher than the current highest vote count
            if (voteCount > highestVoteCount) {
                highestVoteCount = voteCount
                highestVoteName = data.fullname
            }
            NamaCandidate.text = highestVoteName

        }


        val dataSet = BarDataSet(entries, "")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        dataSet.valueTextColor= Color.BLACK
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.animateY(2000)
        barChart.invalidate()

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.isGranularityEnabled = true




    }

}
