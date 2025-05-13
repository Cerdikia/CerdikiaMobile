package com.fhanafi.cerdikia.ui.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.databinding.FragmentSoalBinding
import com.fhanafi.cerdikia.helper.stripHtmlTags
import com.fhanafi.cerdikia.ui.loading.LoadingDialogFragment
import com.fhanafi.cerdikia.ui.shop.ShopViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SoalFragment : Fragment() {

    private val viewModel: SoalViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val shopViewModel: ShopViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentSoalBinding? = null
    private val binding get() = _binding!!
    private var materiId: Int = -1 // Declare at class level
    private var idMapel: Int = -1 // Declare at class level
    private var isCompleted: Boolean = false
    private lateinit var answerOptionAdapter: AnswerOptionAdapter
    private var loadingDialog: LoadingDialogFragment? = null
    private var studyJob: Job? = null
    private val studyInterval = 60_000L // 1 minute

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialogFragment()
            loadingDialog?.show(parentFragmentManager, "loading")
        }
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSoalBinding.inflate(inflater, container, false)
        //Hide bottom navigation
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
        materiId = arguments?.getInt("materiId") ?: -1
        idMapel = arguments?.getInt("idMapel") ?: -1
        isCompleted = arguments?.getBoolean("isCompleted") ?: false
        Log.d("StageFragment", "Received idMapel SoalFragment: $idMapel")
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
        viewModel.fetchQuestions(materiId) // <<--- Call this here
    }

    @Suppress("DEPRECATION")
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentQuestion.collect { question ->
                question?.let {
                    val soalText = question.questionText

                    val hasHtmlTags = soalText.contains("<img") && soalText.contains("<p")

                    if (hasHtmlTags) {
                        binding.webSoal.visibility = View.VISIBLE
                        binding.tvSoal.visibility = View.GONE

                        val htmlContent = """
                    <html>
                        <head>
                            <style>
                                body { font-size: 16px; padding: 0; margin: 0; }
                                img { max-width: 100%; height: auto; display: block; margin: 10px auto; }
                                p { margin: 8px 0; }
                            </style>
                        </head>
                        <body>
                            $soalText
                        </body>
                    </html>
                """.trimIndent()

                        binding.webSoal.settings.javaScriptEnabled = true
                        binding.webSoal.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                        binding.webSoal.settings.setSupportZoom(false)
                        binding.webSoal.settings.builtInZoomControls = false
                        binding.webSoal.isClickable = false
                        binding.webSoal.isLongClickable = false
                        binding.webSoal.isFocusable = false
                    } else {
                        binding.webSoal.visibility = View.GONE
                        binding.tvSoal.visibility = View.VISIBLE
                        binding.tvSoal.text = stripHtmlTags(soalText)
                    }

                    answerOptionAdapter.updateOptions(question.answerOptions)
                    binding.feedbackContainer.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                    answerOptionAdapter.enableClicks()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isCorrectAnswer.collect { isCorrect ->
                isCorrect?.let {
                    binding.feedbackContainer.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                    if (it) {
                        binding.feedbackContainer.setBackgroundColor(resources.getColor(R.color.blueFieldAnswer, null))
                        binding.textViewResult.text = "Benar !!"
                        binding.textViewResult.setTextColor(resources.getColor(R.color.blueMain, null))
                        binding.textViewCorrectAnswer.visibility = View.GONE
                        binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_buttonblue)
                    } else {
                        binding.feedbackContainer.setBackgroundColor(resources.getColor(R.color.redFieldAnswer, null))
                        binding.textViewResult.text = "Salah !!"
                        binding.textViewResult.setTextColor(resources.getColor(R.color.redMain, null))
                        binding.textViewCorrectAnswer.visibility = View.VISIBLE
                        val correctOption = viewModel.currentQuestion.value?.answerOptions
                            ?.find { it.first == viewModel.currentQuestion.value?.correctAnswer }
                        val cleanCorrectAnswer = correctOption?.second?.let { stripHtmlTags(it) }
                        binding.textViewCorrectAnswer.text = "Jawaban yang benar adalah: $cleanCorrectAnswer"
                        binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_buttonred)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            combine(
                viewModel.currentQuestionIndex,
                viewModel.questionList
            ) { index, list ->
                index to list.size
            }.collect { (index, total) ->
                updateProgressBar(index + 1, total)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isQuizFinished.collect { isFinished ->
                if (isFinished) {
                    showLoading()

                    launch {
                        binding.btnClose.visibility = View.GONE
                        binding.progressSoal.visibility = View.GONE
                        binding.rvOpsijawaban.visibility = View.GONE
                        binding.tvSoal.visibility = View.GONE
                        binding.webSoal.visibility = View.GONE
                        binding.feedbackContainer.visibility = View.GONE
                        delay(3000) // Show loading effect for 3 seconds

                        val xp = viewModel.correctAnswers * 2
                        val gems = if (viewModel.correctAnswers > 0) viewModel.correctAnswers * 2 else 0
                        val bundle = Bundle().apply {
                            putInt("XP", xp)
                            putInt("GEMS", gems)
                            putInt("materiId", materiId)
                            putInt("idMapel", idMapel)
                            putBoolean("isCompleted", isCompleted)
                        }

                        hideLoading() // Hide loading before navigating
                        findNavController().navigate(R.id.action_soalFragment_to_completionFragment, bundle)
                        viewModel.resetQuiz()

                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }

                binding.btnClose.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.progressSoal.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.rvOpsijawaban.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.tvSoal.visibility = if (isLoading) View.GONE else View.VISIBLE
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
        if (totalQuestions > 0) {
            val progress = (currentQuestion.toFloat() / totalQuestions * 100).toInt()
            binding.progressSoal.progress = progress
        } else {
            binding.progressSoal.progress = 0 // Set ke 0 kalau belum ada data
        }
    }
    override fun onResume() {
        super.onResume()
        startStudyTracking()
    }

    override fun onPause() {
        super.onPause()
        stopStudyTracking()
    }

    private fun startStudyTracking() {
        studyJob = lifecycleScope.launch {
            while (isActive) {
                delay(studyInterval)
                // Save 1 minute of study time
                shopViewModel.addStudyMinutes(1)
            }
        }
    }

    private fun stopStudyTracking() {
        studyJob?.cancel()
        studyJob = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}