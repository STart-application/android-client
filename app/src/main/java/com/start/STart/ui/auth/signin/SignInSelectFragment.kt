package com.start.STart.ui.auth.signin

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.start.STart.R
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.databinding.FragmentSignInSelectBinding

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
            showNoSignInDialog()
            //startActivity(Intent(context , HomeActivity::class.java))
            //activity?.finish()
        }
    }

    private fun showNoSignInDialog() {
        val dialogFragment = ConfirmNoSignInDialog()
        dialogFragment.show(parentFragmentManager, "ConfirmNoSignInDialog")
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