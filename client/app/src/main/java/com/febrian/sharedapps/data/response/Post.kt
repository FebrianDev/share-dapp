package com.febrian.sharedapps.data.response

data class Post(
    var postId: String? = null,
    var userId: String? = null,
    var content: String? = null,
    var likes: String = "0",
    var comments: String = "0",
    var timestamp: Long = 0L
)