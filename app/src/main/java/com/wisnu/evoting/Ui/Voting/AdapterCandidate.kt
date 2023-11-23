package com.wisnu.evoting.Ui.Voting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wisnu.evoting.Model.ModelCandidates
import com.wisnu.evoting.R

class AdapterCandidate (
    val candidates : ArrayList<ModelCandidates.dataCandidate>,
    val listener : OnAdapterlistener
): RecyclerView.Adapter<AdapterCandidate.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Nama = view.findViewById<TextView>(R.id.NamaCandidate)
        val Nim = view.findViewById<TextView>(R.id.NimCandidate)
        val Visi = view.findViewById<TextView>(R.id.VisiCandidate)
        val Photo = view.findViewById<ImageView>(R.id.photoCandidate)
        val BtnVote = view.findViewById<Button>(R.id.btnVote)
    }
    interface  OnAdapterlistener {
        fun onClick(vote: ModelCandidates.dataCandidate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_adapter_candidate,parent,false)
        )


    override fun getItemCount()=candidates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = candidates[position]
        holder.Nama.text = data.fullname
        holder.Nim.text = data.nim
        holder.Visi.text = data.platform
//        val imageUrl = "http://10.4.204.73/e-voting---php-native${data.photo}"
        val imageUrl = "http://192.168.1.10/votesystem${data.photo}"
        Log.d("image", imageUrl)
        Picasso.get()
            .load(imageUrl)
            .into(holder.Photo)
        holder.Photo
        if (data.status_vote == "true" ){
            holder.BtnVote.visibility = View.GONE
        }else{
            if (data.mulai_vote == true) {
                holder.BtnVote.visibility = View.VISIBLE
                holder.BtnVote.setOnClickListener {
                    listener.onClick(data)
                }
            }else{
                holder.BtnVote.visibility = View.GONE

            }
        }


    }

    public fun setData(data: List<ModelCandidates.dataCandidate>){
        candidates.clear()
        candidates.addAll(data)
        notifyDataSetChanged()
    }


}
