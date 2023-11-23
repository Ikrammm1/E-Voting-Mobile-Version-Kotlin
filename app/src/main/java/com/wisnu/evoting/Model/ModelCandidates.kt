package com.wisnu.evoting.Model

class ModelCandidates(
    val candidates : List<dataCandidate>
) {
    data class dataCandidate(
        val id: String?,
        val position_id: String?,
        val nim: String?,
        val fullname: String?,
        val photo: String?,
        val platform: String?,
        val status_vote : String?,
        val mulai_vote : Boolean

    )
}