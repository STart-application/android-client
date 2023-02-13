package com.start.STart.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.start.STart.R
import com.start.STart.databinding.FragmentSignInSelectBinding
import com.start.STart.ui.home.HomeActivity

class SignInSelectFragment : Fragment() {
    private var _binding: FragmentSignInSelectBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInSelectFragment_to_signInFragment)
        }
        binding.btnNoSignIn.setOnClickListener {
            startActivity(Intent(context , HomeActivity::class.java))
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}