package com.febrian.sharedapps.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.febrian.sharedapps.AuthActivity
import com.febrian.sharedapps.R
import com.google.android.material.button.MaterialButton

class ViewPagerAdapter(private val c: Context, private val activity: Activity) : PagerAdapter() {

    var position: Int? = 0

    private var layoutInflater: LayoutInflater? = null

    private val listImage = listOf(
        R.drawable.onboard_1,
        R.drawable.onboard_2,
        R.drawable.onboard_3,
        R.drawable.onboard_4
    )

    private val listTitle = listOf(
        "Share and tell your story \n" +
                "to fellow users anonymously",
        "Give your feedback without worrying \n" +
                "about your identity being known",
        "Use good and polite language \n" +
                "to avoid unwanted things",
        "Users who violate the rules will \nbe penalized" +
                "according to the \napplicable provisions"
    )

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = c.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater?
        val view: View = layoutInflater!!.inflate(R.layout.item_onboarding, null)
        view.findViewById<ImageView>(R.id.iv1).setImageResource(listImage[position])
        view.findViewById<TextView>(R.id.textView).text = listTitle[position]

        if (position != 3)
            view.findViewById<MaterialButton>(R.id.btn_onboard).visibility = View.GONE
        else
            view.findViewById<MaterialButton>(R.id.btn_onboard).visibility = View.VISIBLE

        view.findViewById<MaterialButton>(R.id.btn_onboard).setOnClickListener {
            val intent = Intent(c.applicationContext, AuthActivity::class.java)
            c.startActivity(intent)
            activity.finish()
        }

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    override fun getCount(): Int {
        return listImage.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}