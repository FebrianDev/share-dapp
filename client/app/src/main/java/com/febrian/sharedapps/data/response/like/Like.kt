package com.febrian.sharedapps.data.response.like

data class Like(
    var userId:String = DEFAULT_VALUE,
    var postId:String? = null
)

const val DEFAULT_VALUE = "0x0000000000000000000000000000000000000000"