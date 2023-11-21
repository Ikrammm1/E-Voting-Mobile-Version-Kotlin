package com.wisnu.evoting.Ui.Login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.wisnu.evoting.API.RetrofitClient
import com.wisnu.evoting.Model.ResponseLogin
import com.wisnu.evoting.R
import com.wisnu.evoting.Ui.Dashboard.Dashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var BtnLogin : Button
    lateinit var EtvoterId : EditText
    lateinit var EtPassword : EditText
    private lateinit var profil : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //cek session
        profil = getSharedPreferences("Login_Session", MODE_PRIVATE)
        if(profil.getString("id",null) !=null) {
            startActivity(Intent(this@LoginActivity, Dashboard::class.java))
            finish()
        }

        EtvoterId = findViewById(R.id.voterId)
        EtPassword = findViewById(R.id.password)
        BtnLogin = findViewById(R.id.btnLogin)

        BtnLogin.setOnClickListener {

            when{
                EtvoterId.text.toString() == "" ->{
                    EtvoterId.error = "Voter Id Tidak Boleh Kosong!"
                }
                EtPassword.text.toString() == "" -> {
                    EtPassword.error = "Password Tidak Boleh Kosong!"
                }
                else -> {
                    getUser()
                }
            }

        }
    }
    private fun getUser(){
        RetrofitClient.instance.login(
            EtvoterId.text.toString(),
            EtPassword.text.toString()
        ).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful){
                    //fungsi session
                    getSharedPreferences("Login_Session", MODE_PRIVATE)
                        .edit()
                        .putString("id", response.body()?.payload?.id)
                        .putString("firstname", response.body()?.payload?.firstname)
                        .putString("lastname", response.body()?.payload?.lastname)
                        .putString("photo", response.body()?.payload?.photo)
                        .apply()

                    if(response.body()?.response == true) {
                        startActivity(Intent(this@LoginActivity, Dashboard::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Voter Id / Password Salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Kesalahan", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Maaf Sedang Gangguan, Silahkan Ulangi", Toast.LENGTH_SHORT).show()
                Log.e("Kesalahan API Login : ", t.toString())
            }

        })
    }
}
