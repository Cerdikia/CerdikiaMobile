package com.fhanafi.cerdikia.ui.question

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentSoalBinding

class SoalFragment : Fragment() {

    companion object {
        fun newInstance() = SoalFragment()
    }
    private val viewModel: SoalViewModel by viewModels()
    private var _binding: FragmentSoalBinding? = null
    private val binding get() = _binding!!
    private var materiId: Int = -1 // Declare at class level

    private lateinit var answerOptionAdapter: AnswerOptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSoalBinding.inflate(inflater, container, false)
        //Hide bottom navigation
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
        materiId = arguments?.getInt("materiId") ?: -1
        closeButton()
        return binding.root
    }

    private fun closeButton(){
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack(R.id.stageFragment, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupJawaban()
        setupNextButton()
    }

    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            binding.tvSoal.text = question.questionText
            answerOptionAdapter.updateOptions(question.answerOptions)
            // Reset UI for the new question
            binding.feedbackContainer.visibility = View.GONE
            binding.btnNext.visibility = View.GONE
            answerOptionAdapter.enableClicks()
        }

        viewModel.isCorrectAnswer.observe(viewLifecycleOwner) { isCorrect ->
            if (isCorrect != null) {
                binding.feedbackContainer.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
                if (isCorrect) {
                    binding.feedbackContainer.setBackgroundColor(resources.getColor(R.color.blueFieldAnswer, null))
                    binding.textViewResult.text = "Benar !!"
                    binding.textViewResult.setTextColor(resources.getColor(R.color.blueMain, null))
                    binding.textViewCorrectAnswer.visibility = View.GONE
                    binding.btnNext.setBackgroundColor(resources.getColor(R.color.blueMain, null))
                    binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_buttonblue)
                } else {
                    binding.feedbackContainer.setBackgroundColor(resources.getColor(R.color.redFieldAnswer, null))
                    binding.textViewResult.text = "Salah !!"
                    binding.textViewResult.setTextColor(resources.getColor(R.color.redMain, null))
                    binding.textViewCorrectAnswer.visibility = View.VISIBLE
                    binding.textViewCorrectAnswer.text = "Jawaban yang benar adalah: ${viewModel.currentQuestion.value?.correctAnswer}"
                    binding.btnNext.setBackgroundColor(resources.getColor(R.color.redMain, null))
                    binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_buttonred)
                }
            }
        }

        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            updateProgressBar(index + 1, viewModel.questionList.value?.size ?: 1)
        }

        viewModel.isQuizFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished == true) {
                val xp = viewModel.correctAnswers * 2
                val gems = if (viewModel.correctAnswers > 0) viewModel.correctAnswers * 2 else 0

                val bundle = Bundle().apply {
                    putInt("XP", xp)
                    putInt("GEMS", gems)
                    putInt("materiId", materiId)
                }

                findNavController().navigate(R.id.action_soalFragment_to_completionFragment, bundle)
                viewModel.resetQuizFinished()
            }
        }
    }

    private fun setupJawaban() {
        binding.rvOpsijawaban.layoutManager = LinearLayoutManager(requireContext())
        answerOptionAdapter = AnswerOptionAdapter(emptyList()) { selectedAnswer ->
            viewModel.selectAnswer(selectedAnswer)
            answerOptionAdapter.disableClicks()
        }
        binding.rvOpsijawaban.adapter = answerOptionAdapter
        // Initial load of options
        viewModel.currentQuestion.value?.let { answerOptionAdapter.updateOptions(it.answerOptions) }
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            viewModel.nextQuestion()
        }
    }

    private fun updateProgressBar(currentQuestion: Int, totalQuestions: Int) {
        val progress = (currentQuestion.toFloat() / totalQuestions * 100).toInt()
        binding.progressSoal.progress = progress
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}
