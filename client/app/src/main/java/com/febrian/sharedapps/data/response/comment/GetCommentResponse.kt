package com.febrian.sharedapps.data.response.comment

data class GetCommentResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var result:ArrayList<Comment> = ArrayList()
    )