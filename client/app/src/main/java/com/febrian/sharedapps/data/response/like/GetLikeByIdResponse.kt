package com.febrian.sharedapps.data.response.like

data class GetLikeByIdResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var result: Like? = null
)