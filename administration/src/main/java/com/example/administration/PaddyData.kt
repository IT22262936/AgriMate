package com.example.administration

data class PaddyData(
    val paddyVariety : String? = null,
    val maturityDuration : Int? = null,
    val wateringDays : List<Int>? = null,
    val fertilizer1Day : Int? = null,
    val fertilizer1Type : String? = null,
    val fertilizer1Amount : Double? = null,
    val fertilizer2Day : Int? = null,
    val fertilizer2Type : String? = null,
    val fertilizer2Amount : Double? = null,
    val fertilizer3Day : Int? = null,
    val fertilizer3Type : String? = null,
    val fertilizer3Amount : Double? = null
)
