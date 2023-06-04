package com.febrian.sharedapps.data.response

data class PostBody(
    var userId: String? = null,
    var content: String? = null,
    var timestamp: Long? = null
)