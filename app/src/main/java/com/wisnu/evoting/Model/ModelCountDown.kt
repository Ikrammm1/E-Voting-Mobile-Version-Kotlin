package com.wisnu.evoting.Model

data class ModelCountDown (
    val position_id: String,
    val description: String,
    val start_vote: String,
    val end_vote: String,
    val countdown: String,
    val sisa_waktu: Int,
    val awal : Int
)