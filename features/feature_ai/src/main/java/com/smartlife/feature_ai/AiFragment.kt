package com.smartlife.feature_ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartlife.feature_ai.databinding.FragmentAiBinding

class AiFragment : Fragment() {
    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPlaceholder.text = "AI中枢 - 待实现"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}