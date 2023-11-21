package com.wisnu.evoting.Model

class ModelCandidates(
    val candidates : List<dataCandidate>
) {
    data class dataCandidate(
        val id: String?,
        val position_id: String?,
        val firstname: String?,
        val lastname: String?,
        val photo: String?,
        val platform: String?,
        val status_vote : String?

    )
}