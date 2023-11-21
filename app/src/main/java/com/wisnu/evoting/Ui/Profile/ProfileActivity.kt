package com.wisnu.evoting.Ui.Profile

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.Picasso
import com.wisnu.evoting.API.RetrofitClient
import com.wisnu.evoting.Model.ModelResponse
import com.wisnu.evoting.Model.ResponseLogin
import com.wisnu.evoting.R
import com.wisnu.evoting.Ui.Login.LoginActivity
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    lateinit var BtnSimpan : Button
    lateinit var BtnLogout : Button
    lateinit var EtFirst : EditText
    lateinit var EtLast : EditText
    private lateinit var profil : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profil = getSharedPreferences("Login_Session", MODE_PRIVATE)

        EtFirst = findViewById(R.id.etFirst)
        EtLast = findViewById(R.id.etLast)
        BtnSimpan = findViewById(R.id.btnSimpan)
        BtnLogout = findViewById(R.id.btnLogout)

        EtFirst.setText(profil.getString("firstname", null).toString())
        EtLast.setText(profil.getString("lastname", null).toString())

        BtnLogout.setOnClickListener {
            var alertDialog = AlertDialog.Builder(this)
                .setTitle("Apakah Anda Yakin Ingin Keluar?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->

                    //menghapus session
                    profil.edit().clear().commit()

                    val kelogin = Intent (this@ProfileActivity, LoginActivity::class.java)
                    startActivity(kelogin)

                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->  })
                .show()
        }
        val ImageUser = findViewById<CircleImageView>(R.id.prof)
        val imageUrl = "http://10.4.204.73/e-voting---php-native${profil.getString("photo", null).toString()}"
        Log.d("image", imageUrl)
        Picasso.get()
            .load(imageUrl)
            .into(ImageUser)

        BtnSimpan.setOnClickListener {
            RetrofitClient.instance.EditUser(
                profil.getString("id", null).toString(),
                EtFirst.text.toString(),
                EtLast.text.toString()
            ).enqueue(object : Callback<ModelResponse>{
                override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Maaf Sistem Sedang Gangguan", Toast.LENGTH_SHORT).show()
                    Log.e("Kesalahan API Edit", t.toString())
                }

                override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                    var alertDialog = AlertDialog.Builder(this@ProfileActivity)
                        .setTitle("Simpan Perubahan?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->

                            Toast.makeText(this@ProfileActivity, "Bersilah Edit Data, Silahkan Login Ulang", Toast.LENGTH_SHORT).show()
                            //menghapus session
                            profil.edit().clear().commit()

                            val loginagain = Intent (this@ProfileActivity, LoginActivity::class.java)
                            startActivity(loginagain)

                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->  })
                        .show()

                }

            })
        }

    }
}
