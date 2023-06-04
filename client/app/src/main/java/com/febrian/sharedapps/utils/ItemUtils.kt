package com.febrian.sharedapps.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.TextView
import android.widget.Toast

object ItemUtils {
    fun setTextUrl(result: String, context: Context, binding: TextView) {
        if (result.contains("https://") || result.contains("http://")) {
            binding.text = result
            binding.setTextColor(Color.parseColor("#223775"))
            binding.setOnLongClickListener {
                copy(context, result)
                true
            }
            binding.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
                context.startActivity(intent)
            }
        } else {
            binding.text = result
        }
    }

    fun copy(context: Context, message: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", message)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(
            context,
            "The text has been copied successfully",
            Toast.LENGTH_SHORT
        ).show()
    }
}