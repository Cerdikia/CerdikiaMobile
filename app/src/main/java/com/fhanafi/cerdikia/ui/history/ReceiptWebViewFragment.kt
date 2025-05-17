package com.fhanafi.cerdikia.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fhanafi.cerdikia.R

class ReceiptWebViewFragment : Fragment() {

    private val args by navArgs<ReceiptWebViewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val webView = WebView(requireContext())
        webView.loadDataWithBaseURL(null, args.htmlContent, "text/html", "utf-8", null)
        onBackButtonPressed()
        return webView
    }

    private fun onBackButtonPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_receiptWebViewFragment_to_historyFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
