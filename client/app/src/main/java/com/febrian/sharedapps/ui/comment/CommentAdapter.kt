package com.febrian.sharedapps.ui.comment

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.febrian.sharedapps.R
import com.febrian.sharedapps.data.response.comment.Comment
import com.febrian.sharedapps.databinding.ItemCommentBinding
import com.febrian.sharedapps.ui.CommentCallback
import com.febrian.sharedapps.utils.DateUtils
import com.febrian.sharedapps.utils.ItemUtils
import java.util.Date

class CommentAdapter(
    private var listComment: ArrayList<Comment>,
    private val callback: CommentCallback,
    private val userId: String,
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    fun setData(newListData: ArrayList<Comment>) {
        val diffResult = DiffUtil.calculateDiff(
            RepoDiffCallback(
                listComment, newListData
            )
        )
        listComment = newListData
        diffResult.dispatchUpdatesTo(this)
    }

    class RepoDiffCallback(
        private val oldItems: ArrayList<Comment>, private val newItems: ArrayList<Comment>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].commentId == newItems[newItemPosition].commentId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems == newItems
    }

    inner class ViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(comment: Comment) {

            if (comment.userId == userId) {
                binding.avatar.background = itemView.context.getDrawable(R.drawable.ic_star)
                binding.ic.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE
            } else {
                val wrap = DrawableCompat.wrap(binding.avatar.background)
                DrawableCompat.setTint(wrap, Color.parseColor("#0299D2"))
                binding.ic.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.GONE
            }

            binding.time.text = DateUtils.getTime(Date(comment.timestamp?.times(1000) ?: 0))
            ItemUtils.setTextUrl(comment.content.toString(), itemView.context, binding.comment)

            binding.btnDelete.setOnClickListener {
                callback.onClickDeleteComment(comment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listComment[position])
    }

    override fun getItemCount(): Int {
        return listComment.size
    }

}