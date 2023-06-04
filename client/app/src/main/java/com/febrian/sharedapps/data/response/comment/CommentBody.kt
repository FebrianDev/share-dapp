package com.febrian.sharedapps.data.response.comment

data class CommentBody(
    var postId: String? = null,
    var userId: String? = null,
    var content: String? = null,
    var timestamp: Long? = null
)
