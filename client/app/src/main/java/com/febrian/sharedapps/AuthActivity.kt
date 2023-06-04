package com.febrian.sharedapps

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.febrian.sharedapps.databinding.ActivityAuthBinding
import com.febrian.sharedapps.ui.MainActivity
import com.febrian.sharedapps.ui.PostViewModel
import com.febrian.sharedapps.utils.Constant
import com.febrian.sharedapps.utils.Helper
import com.febrian.sharedapps.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val postViewModel: PostViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var helper: Helper

    private var account = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            account = binding.account.text.toString()
            if (account.isEmpty()) return@setOnClickListener
            postViewModel.getAccounts()
        }

        postViewModel.getAccountResult.observe(this) {
            it.onSuccess { data ->
                data.result?.forEach { address ->
                    if (address == account) {
                        helper.showToast("Success Login, Address is valid")
                        preferenceManager.putString(Constant.ACCOUNT, address)
                        helper.moveActivityWithFinish(AuthActivity(), MainActivity())
                        return@forEach
                    }
                }
            }

            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }
    }
}