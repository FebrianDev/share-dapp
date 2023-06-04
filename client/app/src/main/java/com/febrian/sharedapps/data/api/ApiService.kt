package com.febrian.sharedapps.data.api

import com.febrian.sharedapps.data.response.CreatePostResponse
import com.febrian.sharedapps.data.response.DeletePostResponse
import com.febrian.sharedapps.data.response.GetAccountsResponse
import com.febrian.sharedapps.data.response.GetAllPostResponse
import com.febrian.sharedapps.data.response.GetPostByIdResponse
import com.febrian.sharedapps.data.response.PostBody
import com.febrian.sharedapps.data.response.comment.CommentBody
import com.febrian.sharedapps.data.response.comment.CommentInt
import com.febrian.sharedapps.data.response.comment.CreateCommentResponse
import com.febrian.sharedapps.data.response.comment.DeleteCommentResponse
import com.febrian.sharedapps.data.response.comment.GetCommentResponse
import com.febrian.sharedapps.data.response.comment.UpdateCommentInPostResponse
import com.febrian.sharedapps.data.response.like.AddAddressInLikeResponse
import com.febrian.sharedapps.data.response.like.DeleteLikeResponse
import com.febrian.sharedapps.data.response.like.GetLikeByIdResponse
import com.febrian.sharedapps.data.response.like.LikeBody
import com.febrian.sharedapps.data.response.like.LikeInt
import com.febrian.sharedapps.data.response.like.UpdateLikeInPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("/account")
    suspend fun getAccounts(): GetAccountsResponse

    @GET("/post")
    suspend fun getAllPost(): GetAllPostResponse

    @POST("/post")
    suspend fun createPost(
        @Body post: PostBody
    ): CreatePostResponse

    @GET("/post/{id}")
    suspend fun getPostById(
        @Path("id") id: String?
    ): GetPostByIdResponse

    @GET("/like/{id}")
    fun getLikeById(
        @Path("id") id: String?
    ): Call<GetLikeByIdResponse>

    @POST("/like")
    fun addAddressInLike(
        @Body likeBody: LikeBody
    ): Call<AddAddressInLikeResponse>

    @PUT("/post-like/{id}")
    fun updateLikeInPost(
        @Path("id") id: String?,
        @Body like: LikeInt?
    ): Call<UpdateLikeInPostResponse>

    @DELETE("/like/{id}")
    suspend fun deleteLike(
        @Path("id") id: String
    ): DeleteLikeResponse

    @DELETE("/post/{id}")
    suspend fun deletePost(
        @Path("id") id: String?,
    ): DeletePostResponse

    @POST("/comment")
    fun createComment(
        @Body commentBody: CommentBody
    ): Call<CreateCommentResponse>

    @GET("/comment/{postId}")
    suspend fun getComment(
        @Path("postId") postId: String
    ): GetCommentResponse

    @PUT("/post-comment/{id}")
    fun updateCommentInPost(
        @Path("id") id: String?,
        @Body comment: CommentInt?
    ): Call<UpdateCommentInPostResponse>

    @DELETE("/comment/{id}")
    suspend fun deleteComment(
        @Path("id") id: String?,
    ): DeleteCommentResponse
}