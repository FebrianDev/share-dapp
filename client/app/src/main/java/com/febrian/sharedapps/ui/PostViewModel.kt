package com.febrian.sharedapps.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febrian.sharedapps.data.repository.PostRepository
import com.febrian.sharedapps.data.response.CreatePostResponse
import com.febrian.sharedapps.data.response.DeletePostResponse
import com.febrian.sharedapps.data.response.GetAccountsResponse
import com.febrian.sharedapps.data.response.GetAllPostResponse
import com.febrian.sharedapps.data.response.GetPostByIdResponse
import com.febrian.sharedapps.data.response.PostBody
import com.febrian.sharedapps.data.response.comment.DeleteCommentResponse
import com.febrian.sharedapps.data.response.comment.GetCommentResponse
import com.febrian.sharedapps.data.response.like.AddAddressInLikeResponse
import com.febrian.sharedapps.data.response.like.DeleteLikeResponse
import com.febrian.sharedapps.data.response.like.GetLikeByIdResponse
import com.febrian.sharedapps.data.response.like.UpdateLikeInPostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var _getAccountResult = MutableLiveData<Result<GetAccountsResponse>>()
    val getAccountResult: LiveData<Result<GetAccountsResponse>> get() = _getAccountResult

    private var _createPostResult = MutableLiveData<Result<CreatePostResponse>>()
    val createPostResult: LiveData<Result<CreatePostResponse>> get() = _createPostResult

    private var _getPostResult = MutableLiveData<Result<GetPostByIdResponse>>()
    val getPostResult: LiveData<Result<GetPostByIdResponse>> get() = _getPostResult

    private var _getAllPostResult = MutableLiveData<Result<GetAllPostResponse>>()
    val getAllPostResult: LiveData<Result<GetAllPostResponse>> get() = _getAllPostResult

    private var _getLikeById = MutableLiveData<Result<GetLikeByIdResponse>>()
    val getLikeById: LiveData<Result<GetLikeByIdResponse>> get() = _getLikeById

    private var _addAddressInLike = MutableLiveData<Result<AddAddressInLikeResponse>>()
    val addAddressInLike: LiveData<Result<AddAddressInLikeResponse>> get() = _addAddressInLike

    private var _updateLikeInPost = MutableLiveData<Result<UpdateLikeInPostResponse>>()
    val updateLikeInPost: LiveData<Result<UpdateLikeInPostResponse>> get() = _updateLikeInPost

    private var _deleteLike = MutableLiveData<Result<DeleteLikeResponse>>()
    val deleteLike: LiveData<Result<DeleteLikeResponse>> get() = _deleteLike

    private var _deletePost = MutableLiveData<Result<DeletePostResponse>>()
    val deletePost: LiveData<Result<DeletePostResponse>> get() = _deletePost

    private var _getCommentResult = MutableLiveData<Result<GetCommentResponse>>()
    val getCommentResult: LiveData<Result<GetCommentResponse>> get() = _getCommentResult

    private var _deleteComment = MutableLiveData<Result<DeleteCommentResponse>>()
    val deleteComment: LiveData<Result<DeleteCommentResponse>> get() = _deleteComment

    private var _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> get() = _loading

    fun getAccounts() {
        viewModelScope.launch {
            _loading.value = true
            _getAccountResult.value = repository.getAccounts()
            _loading.value = false
        }
    }


    fun createPost(postBody: PostBody) {
        viewModelScope.launch {
            _loading.value = true
            _createPostResult.value = repository.createPost(postBody)
            _loading.value = false
        }
    }

    fun getAllPost() {
        viewModelScope.launch {
            _loading.value = true
            _getAllPostResult.value = repository.getAllPost()
            _loading.value = false
        }
    }

    fun getPostById(id: String) {
        viewModelScope.launch {
            _loading.value = true
            _getPostResult.value = repository.getPostById(id)
            _loading.value = false
        }
    }

    fun getLikeById(id: String) {
        viewModelScope.launch {
            _loading.value = true
            //   _getLikeById.value = repository.getLikeById(id)
            _loading.value = false
        }
    }

    fun deleteLike(
        id: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            _deleteLike.value = repository.deleteLike(id)
            _loading.value = false
        }
    }

    fun deletePost(
        id: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            _deletePost.value = repository.deletePost(id)
            _loading.value = false
        }
    }

    fun getComment(id: String) {
        viewModelScope.launch {
            _loading.value = true
            _getCommentResult.value = repository.getComment(id)
            _loading.value = false
        }
    }

    fun deleteComment(
        postId: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            _deleteComment.value = repository.deleteComment(postId)
            _loading.value = false
        }
    }
}