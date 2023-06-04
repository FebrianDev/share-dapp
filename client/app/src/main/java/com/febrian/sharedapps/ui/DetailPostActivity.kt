package com.febrian.sharedapps.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.sharedapps.R
import com.febrian.sharedapps.data.api.ApiService
import com.febrian.sharedapps.data.response.Post
import com.febrian.sharedapps.data.response.comment.Comment
import com.febrian.sharedapps.data.response.comment.CommentBody
import com.febrian.sharedapps.data.response.comment.CommentInt
import com.febrian.sharedapps.data.response.comment.CreateCommentResponse
import com.febrian.sharedapps.data.response.comment.UpdateCommentInPostResponse
import com.febrian.sharedapps.data.response.like.AddAddressInLikeResponse
import com.febrian.sharedapps.data.response.like.DEFAULT_VALUE
import com.febrian.sharedapps.data.response.like.GetLikeByIdResponse
import com.febrian.sharedapps.data.response.like.LikeBody
import com.febrian.sharedapps.data.response.like.LikeInt
import com.febrian.sharedapps.data.response.like.UpdateLikeInPostResponse
import com.febrian.sharedapps.databinding.ActivityDetailPostBinding
import com.febrian.sharedapps.ui.comment.CommentAdapter
import com.febrian.sharedapps.utils.Constant.ACCOUNT
import com.febrian.sharedapps.utils.DateUtils
import com.febrian.sharedapps.utils.Helper
import com.febrian.sharedapps.utils.ItemUtils
import com.febrian.sharedapps.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DetailPostActivity : AppCompatActivity(), CommentCallback {

    private lateinit var binding: ActivityDetailPostBinding

    private val postViewModel: PostViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var apiService: ApiService

    private lateinit var commentAdapter: CommentAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val postId = intent.getStringExtra("POST_ID").toString()
        getPostById(postId)
        showLoading()

        if(preferenceManager.getString(ACCOUNT) != intent.getStringExtra("ACCOUNT_ID")){
            binding.btnDelete.visibility = View.GONE
        }

        commentAdapter =
            CommentAdapter(arrayListOf(), this, preferenceManager.getString(ACCOUNT))
        getComment(postId)
        setBackgroundLike(postId, binding.btnLike)
        binding.btnLike.setOnClickListener {
            addLikePost(postId, binding.btnLike, binding.likeCount)
        }

        deleteComment(postId)

        binding.btnDelete.setOnClickListener {
            deletePost(postId)
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun deletePost(postId: String) {
        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Delete").setMessage("Are you sure delete this post?").setPositiveButton(
                "Yes"
            ) { _, _ ->
                postViewModel.deletePost(postId)
            }.setNegativeButton("No", null).show()
    }

    private fun deleteComment(postId: String) {
        postViewModel.deleteComment.observe(this) {
            it.onSuccess {
                helper.showToast("Success Delete Comment")
                postViewModel.getComment(postId)
            }
        }
    }

    private fun setBackgroundLike(postId: String, view: ImageButton) {
        apiService.getLikeById(postId).enqueue(object : Callback<GetLikeByIdResponse> {
            override fun onResponse(
                call: Call<GetLikeByIdResponse>,
                response: Response<GetLikeByIdResponse>
            ) {
                val data = response.body() as GetLikeByIdResponse
                if (data.result?.userId == preferenceManager.getString(ACCOUNT) && data.result?.postId == postId) {
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }

            override fun onFailure(call: Call<GetLikeByIdResponse>, t: Throwable) {
                helper.showToast(t.message.toString())
            }

        })
    }

    private fun addLikePost(postId: String, view: ImageButton, likeCount: TextView) {
        apiService.getLikeById(postId).enqueue(object : Callback<GetLikeByIdResponse> {
            override fun onResponse(
                call: Call<GetLikeByIdResponse>,
                response: Response<GetLikeByIdResponse>
            ) {
                val data = (response.body() as GetLikeByIdResponse)

                if (data.result?.userId == DEFAULT_VALUE) {
                    //if user have not added like

                    apiService.addAddressInLike(
                        LikeBody(
                            postId,
                            "0xe8c3D8944049A3A76e47c45bD006e2c432Fa2Dff"
                        )
                    ).enqueue(object : Callback<AddAddressInLikeResponse> {
                        override fun onResponse(
                            call: Call<AddAddressInLikeResponse>,
                            response: Response<AddAddressInLikeResponse>
                        ) {
                        }

                        override fun onFailure(call: Call<AddAddressInLikeResponse>, t: Throwable) {

                        }

                    })
                    helper.showToast("Success Add Like Post")
                    updateLikeInPost(postId, 1)
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                    likeCount.text = likeCount.text.toString().toInt().plus(1).toString()

                } else {
                    helper.showToast("Success Remove Like Post")
                    postViewModel.deleteLike(postId)
                    updateLikeInPost(postId, -1)
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                    likeCount.text = likeCount.text.toString().toInt().minus(1).toString()
                }
            }

            override fun onFailure(call: Call<GetLikeByIdResponse>, t: Throwable) {
                helper.showToast(t.message.toString())
            }

        })
    }

    private fun updateLikeInPost(postId: String, like: Int) {
        apiService.updateLikeInPost(postId, LikeInt(like))
            .enqueue(object : Callback<UpdateLikeInPostResponse> {
                override fun onResponse(
                    call: Call<UpdateLikeInPostResponse>,
                    response: Response<UpdateLikeInPostResponse>
                ) {

                }

                override fun onFailure(call: Call<UpdateLikeInPostResponse>, t: Throwable) {
                    helper.showToast(t.message.toString())
                }

            })
    }

    private fun sendComment(post: Post?) {
        if (binding.edtMessage.text.toString().isNotEmpty()
        ) {
            apiService.createComment(
                CommentBody(
                    postId = post?.postId,
                    userId = preferenceManager.getString(ACCOUNT),
                    content = binding.edtMessage.text.toString(),
                    timestamp = System.currentTimeMillis() / 1000
                )
            )
                .enqueue(object : Callback<CreateCommentResponse> {
                    override fun onResponse(
                        call: Call<CreateCommentResponse>,
                        response: Response<CreateCommentResponse>
                    ) {
                        postViewModel.getComment(post?.postId.toString())
                    }

                    override fun onFailure(call: Call<CreateCommentResponse>, t: Throwable) {
                        helper.showToast(t.message.toString())
                    }

                })

            apiService.updateCommentInPost(post?.postId, CommentInt(1))
                .enqueue(object : Callback<UpdateCommentInPostResponse> {
                    override fun onResponse(
                        call: Call<UpdateCommentInPostResponse>,
                        response: Response<UpdateCommentInPostResponse>
                    ) {
                        helper.showToast(response.body()?.message.toString())
                    }

                    override fun onFailure(call: Call<UpdateCommentInPostResponse>, t: Throwable) {
                        helper.showToast(t.message.toString())
                    }

                })
            binding.edtMessage.setText("")
        }
    }

    private fun getComment(postId: String) {
        postViewModel.getComment(postId)
        postViewModel.getCommentResult.observe(this) {
            it.onSuccess { data ->
                commentAdapter.setData(data.result)
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(this@DetailPostActivity)
                    setHasFixedSize(true)
                    adapter = commentAdapter
                }
            }

            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPostById(id: String) {
        postViewModel.getPostById(id)
        postViewModel.getPostResult.observe(this) { result ->
            result.onSuccess { data ->
                helper.showToast(data.message.toString())
                helper.showLog(data.message.toString())
                data.result?.let { showData(it) }

                //send comment
                binding.sendComment.setOnClickListener {
                    sendComment(data.result)
                }

                //send comment
                binding.btnComment.setOnClickListener {
                    binding.edtMessage.requestFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.edtMessage, InputMethodManager.SHOW_IMPLICIT)

                    binding.sendComment.setOnClickListener {
                        sendComment(data.result)
                    }
                }
            }

            result.onFailure { t ->
                helper.showToast(t.message.toString())
                helper.showLog(t.message.toString())
            }
        }
    }

    private fun showLoading() {
        postViewModel.loading.observe(this) {
            helper.showLoading(it, binding.shimmerDetail)
            if (it) {
                binding.card.visibility = View.GONE
            } else {
                binding.card.visibility = View.VISIBLE
                binding.rv.visibility = View.VISIBLE
                binding.time.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showData(post: Post) {
        binding.apply {
            binding.likeCount.text = post.likes
            binding.commentCount.text = post.comments
            binding.date.text =
                Date(post.timestamp * 1000).let { SimpleDateFormat("HH:mm : dd-MM-yyyy").format(it) }
            binding.time.text = DateUtils.getTime(Date(post.timestamp * 1000))
            ItemUtils.setTextUrl(post.content.toString(), applicationContext, binding.post)
        }
    }

    override fun onClickDeleteComment(comment: Comment) {
        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Delete").setMessage("Are you sure delete this comment?")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                postViewModel.deleteComment(
                    comment.commentId.toString(),
                )
            }.setNegativeButton("No", null).show()

    }
}