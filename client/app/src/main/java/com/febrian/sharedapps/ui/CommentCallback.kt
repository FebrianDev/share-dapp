package com.febrian.sharedapps.ui

import com.febrian.sharedapps.data.response.comment.Comment

interface CommentCallback {
    fun onClickDeleteComment(comment: Comment)
}