package com.febrian.sharedapps.data.response.comment

data class Comment(
    var commentId: String? = null,
    var postId: String? = null,
    var userId: String? = null,
    var content: String? = null,
    var timestamp: Long? = null
)