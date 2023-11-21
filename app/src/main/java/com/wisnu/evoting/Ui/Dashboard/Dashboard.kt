package com.wisnu.evoting.Ui.Dashboard

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import com.wisnu.evoting.R
import com.wisnu.evoting.Ui.Hasil.HasilActivity
import com.wisnu.evoting.Ui.Profile.ProfileActivity
import com.wisnu.evoting.Ui.Voting.VotingActivity
import de.hdodenhof.circleimageview.CircleImageView

class Dashboard : AppCompatActivity() {

    lateinit var txtName : TextView
    lateinit var BtnVoting : CardView
    lateinit var BtnHasil : CardView
    lateinit var BtnProfil : CardView
    private lateinit var profil : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        profil = getSharedPreferences("Login_Session", MODE_PRIVATE)
        val NamaUser ="${ profil.getString("firstname", null).toString()} ${profil.getString("lastname", null).toString()}"

        BtnVoting = findViewById(R.id.btnCandidate)
        BtnHasil = findViewById(R.id.btnHasil)
        BtnProfil = findViewById(R.id.btnProfile)
        txtName = findViewById(R.id.title)
        val ImageUser = findViewById<CircleImageView>(R.id.prof)
        val imageUrl = "http://10.4.204.69/e-voting---php-native${profil.getString("photo", null).toString()}"
        Log.d("image", imageUrl)
        Picasso.get()
            .load(imageUrl)
            .into(ImageUser)

        txtName.text = "Selamat Datang ${NamaUser}"

        BtnProfil.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        BtnHasil.setOnClickListener {
            startActivity(Intent(this, HasilActivity::class.java))
        }
        BtnVoting.setOnClickListener {
            startActivity(Intent(this, VotingActivity::class.java))
        }
    }
}
