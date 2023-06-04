package com.febrian.sharedapps.ui

import android.widget.ImageButton
import android.widget.TextView
import com.febrian.sharedapps.data.response.Post

interface PostCallback {
    fun onClickLikeListener(postEntity: Post, view: ImageButton, likeCount: TextView)
    fun onCLickDetailPostListener(postEntity: Post, color: Int)
    fun onClickDeleteListener(postEntity: Post)
    fun setBackgroundLike(postEntity: Post, view: ImageButton)
}