package com.febrian.sharedapps.data.response

data class GetPostByIdResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var result:Post? = null
)