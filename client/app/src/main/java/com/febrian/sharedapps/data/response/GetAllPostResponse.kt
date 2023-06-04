package com.febrian.sharedapps.data.response

data class GetAllPostResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var result: ArrayList<Post> = ArrayList()
)