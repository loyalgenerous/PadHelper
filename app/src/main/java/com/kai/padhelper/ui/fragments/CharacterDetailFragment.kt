package com.kai.padhelper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kai.padhelper.R
import com.kai.padhelper.databinding.FragmentCharacterDetailBinding
import com.kai.padhelper.ui.MainActivity
import com.kai.padhelper.ui.viewmodels.ViewModel

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private lateinit var viewModel: ViewModel

    private var _viewBinding: FragmentCharacterDetailBinding? = null
    private val binding get() = _viewBinding!!
    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentCharacterDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val padCharacter = args.padCharacter
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl("https://pad.chesterip.cc/" + padCharacter.characterId)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


}