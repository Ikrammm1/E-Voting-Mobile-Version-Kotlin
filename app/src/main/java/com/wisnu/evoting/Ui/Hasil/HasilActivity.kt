package com.wisnu.evoting.Ui.Hasil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.wisnu.evoting.API.APIService
import com.wisnu.evoting.API.RetrofitClient
import com.wisnu.evoting.Model.ModelHasil
import com.wisnu.evoting.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HasilActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil)
        barChart = findViewById(R.id.barChart)


            RetrofitClient.instance.Result().enqueue(object : Callback<List<ModelHasil>>{
                override fun onFailure(call: Call<List<ModelHasil>>, t: Throwable) {
                    Toast.makeText(this@HasilActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                    Log.e("Kesalahan API Hasil", t.toString())
                }

                override fun onResponse(
                    call: Call<List<ModelHasil>>,
                    response: Response<List<ModelHasil>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        // Handle data, misalnya, tampilkan dalam grafik batang menggunakan MPAndroidChart
                        displayBarChart(data)
                    } else {
                        // Handle kesalahan response
                    }
                }

            })

    }

    private fun displayBarChart(data: List<ModelHasil>?) {
        val barEntries = mutableListOf<BarEntry>()

        data?.forEachIndexed { index, dataModel ->
            barEntries.add(BarEntry(index.toFloat(), dataModel.jml_vote!!.toFloat()))
        }

        val barDataSet = BarDataSet(barEntries, "Label")
        val barData = BarData(barDataSet)

        val barChart = findViewById<BarChart>(R.id.barChart)
        barChart.data = barData
        barChart.invalidate()
    }



}
