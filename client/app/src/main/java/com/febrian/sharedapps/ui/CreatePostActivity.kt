package com.febrian.sharedapps.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.febrian.sharedapps.R
import com.febrian.sharedapps.data.response.PostBody
import com.febrian.sharedapps.databinding.ActivityCreatePostBinding
import com.febrian.sharedapps.utils.Constant
import com.febrian.sharedapps.utils.Helper
import com.febrian.sharedapps.utils.PreferenceManager
import com.febrian.sharedapps.utils.ProfanityFilter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding

    private val postViewModel: PostViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPost.isEnabled = false
        binding.btnPost.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.darker_gray
            )
        )

        tweetWatcher()

        binding.btnPost.setOnClickListener {

            if (ProfanityFilter(this).checkInput(
                    binding.post.text.toString(),
                    helper
                )
            ) {
                return@setOnClickListener
            }

            val postBody = PostBody(
                preferenceManager.getString(Constant.ACCOUNT),
                binding.post.text.toString(),
                getTimestamp()
            )

            postViewModel.createPost(postBody)

            val bottomSheet = BottomSheetDialog(
                this@CreatePostActivity,
                R.style.BottomSheetDialogTheme
            )
            val bottomSheetView: View =
                LayoutInflater.from(this)
                    .inflate(
                        R.layout.bottom_sheet_add_post,
                        findViewById<LinearLayout>(R.id.bottomSheetContainer)
                    )
            bottomSheetView.findViewById<com.google.android.material.button.MaterialButton>(R.id.finish)
                .setOnClickListener {
                    helper.moveActivityWithFinish(CreatePostActivity(), MainActivity())
                }

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

        binding.back.setOnClickListener {
            helper.moveActivityWithFinish(CreatePostActivity(), MainActivity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimestamp(): Long {
      return System.currentTimeMillis() / 1000
    }

    private fun tweetWatcher() {
        binding.post.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.wordCount.text = binding.post.text!!.length.toString() + "/320"
                validateTweet()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun validateTweet() {
        val input = binding.post.text!!.length
        if (input > 320) {
            helper.showToast("Character exceeds the limit")
            binding.btnPost.isEnabled = false
            binding.btnPost.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.darker_gray
                )
            )
        } else if (input < 21) {
            binding.btnPost.isEnabled = false
            binding.btnPost.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.darker_gray
                )
            )
        } else {
            binding.btnPost.isEnabled = true
            binding.btnPost.setBackgroundColor(ContextCompat.getColor(this, R.color.card_1))
        }
    }

}