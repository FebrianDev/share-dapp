package com.febrian.sharedapps.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

class ProfanityFilter(private val c: Context) {

    private val list = ArrayList<String>()

    fun checkInput(input: String, helper: Helper): Boolean {

        var check = false

        val myOutput: String
        val myInputStream: InputStream

        try {
            myInputStream = c.assets.open("profanity.txt")
            val size: Int = myInputStream.available()
            val buffer = ByteArray(size)
            myInputStream.read(buffer)
            myOutput = String(buffer)

            var i = 0

            list.addAll(myOutput.split(',').toTypedArray())

            list.forEach {

                if (input.trim().contains(it.replace("\n", "").trim())
                ) {
                    check = true
                    helper.showToast("This word contains profanity words, please use kind and polite words!")

                    return@forEach
                }

                i++
            }

        } catch (e: IOException) {
            helper.showToast(e.message.toString())
        }

        return check
    }
}