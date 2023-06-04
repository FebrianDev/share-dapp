package com.febrian.sharedapps.data.response

data class GetAccountsResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var result:ArrayList<String>? = null
)