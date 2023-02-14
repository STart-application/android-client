package com.start.STart.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.start.STart.databinding.FragmentSignInBinding
import com.start.STart.ui.auth.signup.SignUpActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(context, SignUpActivity::class.java))
        }
    }

    private fun signIn() {
        if (isInputValid) {
            startActivity(Intent(context, HomeActivity::class.java).apply {
                putExtra(Constants.SIGN_IN, true)
            })
            activity?.finish()
        } else {
            showSignInFailText()
        }
    }

    private val isInputValid: Boolean
        get() = binding.inputStudentId.text.toString().isNotBlank() and
                binding.inputPassword.text.toString().isNotBlank()

    private fun showSignInFailText() {
        binding.textSignInFail.visibility = View.VISIBLE
    }

    private fun hideSignInFailText() {
        binding.textSignInFail.visibility = View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}