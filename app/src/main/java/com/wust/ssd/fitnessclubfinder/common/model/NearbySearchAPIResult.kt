package com.wust.ssd.fitnessclubfinder.common.model

data class NearbySearchAPIResult(
    val html_attributions:List<String>,
    val results: List<Club>
)