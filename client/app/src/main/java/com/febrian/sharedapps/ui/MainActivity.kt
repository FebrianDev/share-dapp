package com.febrian.sharedapps.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.sharedapps.AuthActivity
import com.febrian.sharedapps.R
import com.febrian.sharedapps.data.api.ApiService
import com.febrian.sharedapps.data.response.Post
import com.febrian.sharedapps.data.response.like.AddAddressInLikeResponse
import com.febrian.sharedapps.data.response.like.DEFAULT_VALUE
import com.febrian.sharedapps.data.response.like.GetLikeByIdResponse
import com.febrian.sharedapps.data.response.like.LikeBody
import com.febrian.sharedapps.data.response.like.LikeInt
import com.febrian.sharedapps.data.response.like.UpdateLikeInPostResponse
import com.febrian.sharedapps.databinding.ActivityMainBinding
import com.febrian.sharedapps.utils.Constant
import com.febrian.sharedapps.utils.Constant.ACCOUNT
import com.febrian.sharedapps.utils.Helper
import com.febrian.sharedapps.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PostCallback {

    private lateinit var binding: ActivityMainBinding

    private val postViewModel: PostViewModel by viewModels()

    private lateinit var postAdapter: PostAdapter

    @Inject
    lateinit var helper: Helper

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager.putString(Constant.KEY_LOG, Constant.KEY_LOG)

        postAdapter = PostAdapter(
            arrayListOf(),
            this@MainActivity,
            preferenceManager.getString(Constant.ACCOUNT),
        )

        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        postViewModel.getAllPost()
        getAllPost()

        showLoading()

        postViewModel.deletePost.observe(this) {
            it.onSuccess { data ->
                helper.showToast(data.message.toString())
                postViewModel.getAllPost()
            }

            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }

        binding.addPost.setOnClickListener {
            val intent = Intent(applicationContext, CreatePostActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        AlertDialog.Builder(this).setIcon(R.drawable.baseline_logout_24)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_logout)).setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                preferenceManager.clear(ACCOUNT)
                preferenceManager.clear(Constant.KEY_LOG)
                helper.moveActivityWithFinish(this, AuthActivity())
            }.setNegativeButton(getString(R.string.no), null).show()
    }


    private fun showLog(message: String) {
        helper.showLog(message)
    }

    private fun showToast(message: String) {
        helper.showToast(message)
    }

    private fun showLoading() {
        postViewModel.loading.observe(this) {
            if (binding.rv.visibility == View.GONE) {
                helper.showLoading(it, binding.shimmerItemTweet)
                helper.showLoading(!it, binding.rv)
            }

        }
    }

    private fun getAllPost() {
        postViewModel.getAllPostResult.observe(this) { result ->
            result.onSuccess { data ->
                showLog(data.result.toString())
                helper.showToast(data.message.toString())
                postAdapter.setData(data.result)
                binding.rv.adapter = postAdapter
            }

            result.onFailure { t ->
                showToast(t.message.toString())
                showLog(t.message.toString())
            }

        }
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
                            preferenceManager.getString(Constant.ACCOUNT)
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
                    updateLikeInPost(postId, -1)
                    postViewModel.deleteLike(postId)
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

    override fun onClickLikeListener(postEntity: Post, view: ImageButton, likeCount: TextView) {
        postViewModel.getLikeById(postEntity.postId.toString())
        addLikePost(postEntity.postId.toString(), view, likeCount)
    }

    override fun onCLickDetailPostListener(postEntity: Post, color: Int) {
        val intent = Intent(applicationContext, DetailPostActivity::class.java)
        intent.putExtra(Constant.COLOR_ITEM, color)
        intent.putExtra("POST_ID", postEntity.postId)
        intent.putExtra("ACCOUNT_ID", postEntity.userId)
        startActivity(intent)
    }

    override fun onClickDeleteListener(postEntity: Post) {
        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Delete").setMessage("Are you sure delete this post?").setPositiveButton(
                "Yes"
            ) { _, _ ->
                postViewModel.deletePost(postEntity.postId.toString())
            }.setNegativeButton("No", null).show()
    }

    override fun setBackgroundLike(postEntity: Post, view: ImageButton) {
        apiService.getLikeById(postEntity.postId).enqueue(object : Callback<GetLikeByIdResponse> {
            override fun onResponse(
                call: Call<GetLikeByIdResponse>,
                response: Response<GetLikeByIdResponse>
            ) {
                val data = response.body() as GetLikeByIdResponse
                if (data.result?.userId == preferenceManager.getString(Constant.ACCOUNT) && data.result?.postId == postEntity.postId) {
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

}