package com.febrian.sharedapps.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.febrian.sharedapps.R
import com.febrian.sharedapps.data.response.Post
import com.febrian.sharedapps.databinding.ItemPostBinding
import com.febrian.sharedapps.utils.DateUtils
import com.febrian.sharedapps.utils.ItemUtils
import java.util.Date

class PostAdapter(
    private var listPost: ArrayList<Post>,
    private var postCallback: PostCallback,
    private var userId: String
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    fun setData(newListData: ArrayList<Post>) {
        val diffResult = DiffUtil.calculateDiff(RepoDiffCallback(listPost, newListData))
        listPost = newListData
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(post: Post, position: Int) {
            val color: Int
            if (position % 2 == 1) {
                //1
                color = ContextCompat.getColor(itemView.context, R.color.card_2)
                binding.card1.setCardBackgroundColor(color)

            } else {
                //0
                color = ContextCompat.getColor(itemView.context, R.color.card_1)
                binding.card1.setCardBackgroundColor(color)
            }

            if(post.userId != userId){
                binding.btnDelete.visibility = View.GONE
            }

            postCallback.setBackgroundLike(post, binding.btnLike)

            binding.apply {
                likeCount.text = post.likes
                commentCount.text = post.comments
                time.text = DateUtils.getTime(Date(post.timestamp * 1000))
                ItemUtils.setTextUrl(post.content.toString(), itemView.context, binding.textPost)
            }

            binding.btnLike.setOnClickListener {
                postCallback.onClickLikeListener(post, binding.btnLike, binding.likeCount)
            }

            binding.btnComment.setOnClickListener {
                postCallback.onCLickDetailPostListener(post, color)
            }

            itemView.setOnClickListener {
                postCallback.onCLickDetailPostListener(post, color)
            }

            binding.btnDelete.setOnClickListener {
                postCallback.onClickDeleteListener(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPost[position], position)
    }

    class RepoDiffCallback(
        private val oldItems: ArrayList<Post>,
        private val newItems: ArrayList<Post>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].postId == newItems[newItemPosition].postId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems == newItems
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.postId == newItem.postId
            }
        }
    }
}