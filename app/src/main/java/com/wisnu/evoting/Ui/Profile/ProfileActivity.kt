package com.wisnu.evoting.Ui.Profile

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
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
    lateinit var BtnBack : ImageView
    lateinit var EtNim : TextView
    lateinit var EtName : EditText
    lateinit var EtPass : EditText
    private lateinit var profil : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profil = getSharedPreferences("Login_Session", MODE_PRIVATE)

        EtNim = findViewById(R.id.etNim)
        EtName = findViewById(R.id.etName)
        EtPass = findViewById(R.id.etPass)
        BtnSimpan = findViewById(R.id.btnSimpan)
        BtnLogout = findViewById(R.id.btnLogout)
        BtnBack = findViewById(R.id.btnBack)

        EtNim.setText(profil.getString("nim", null).toString())
        EtName.setText(profil.getString("fullname", null).toString())

        BtnBack.setOnClickListener {
            this.finish()
        }

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
         var imageUrl = "https://condign-shells.000webhostapp.com/E-Voting/images/foto.png"
        if (profil.getString("photo", null).toString() != ""){
            imageUrl = "https://condign-shells.000webhostapp.com/E-Voting/${profil.getString("photo", null).toString()}"


        }
        Log.d("image", imageUrl)
        Picasso.get()
            .load(imageUrl)
            .into(ImageUser)

        BtnSimpan.setOnClickListener {
            RetrofitClient.instance.EditUser(
                profil.getString("id", null).toString(),
                EtName.text.toString(),
                EtPass.text.toString()
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
