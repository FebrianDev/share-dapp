package com.febrian.sharedapps.data.repository

import com.febrian.sharedapps.data.api.ApiService
import com.febrian.sharedapps.data.response.CreatePostResponse
import com.febrian.sharedapps.data.response.DeletePostResponse
import com.febrian.sharedapps.data.response.GetAccountsResponse
import com.febrian.sharedapps.data.response.GetAllPostResponse
import com.febrian.sharedapps.data.response.GetPostByIdResponse
import com.febrian.sharedapps.data.response.PostBody
import com.febrian.sharedapps.data.response.comment.DeleteCommentResponse
import com.febrian.sharedapps.data.response.comment.GetCommentResponse
import com.febrian.sharedapps.data.response.like.DeleteLikeResponse

class PostRepository(private val apiService: ApiService) {

    suspend fun getAccounts(): Result<GetAccountsResponse> {
        return try {
            Result.success(apiService.getAccounts())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(postBody: PostBody): Result<CreatePostResponse> {
        return try {
            val createPost = apiService.createPost(postBody)
            Result.success(createPost)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllPost(): Result<GetAllPostResponse> {
        return try {
            Result.success(apiService.getAllPost())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPostById(id: String): Result<GetPostByIdResponse> {
        return try {
            Result.success(apiService.getPostById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteLike(
        id: String
    ): Result<DeleteLikeResponse> {
        return try {
            Result.success(apiService.deleteLike(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(
        id: String
    ): Result<DeletePostResponse> {
        return try {
            Result.success(apiService.deletePost(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComment(postId: String): Result<GetCommentResponse> {
        return try {
            Result.success(apiService.getComment(postId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(
        id: String
    ): Result<DeleteCommentResponse> {
        return try {
            Result.success(apiService.deleteComment(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}