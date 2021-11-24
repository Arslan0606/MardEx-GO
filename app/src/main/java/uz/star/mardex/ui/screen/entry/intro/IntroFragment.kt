package uz.star.mardex.ui.screen.entry.intro

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.databinding.FragmentIntroBinding
import uz.star.mardex.model.response.server.intro.IntroData
import uz.star.mardex.ui.adapter.recycler_view.IntroAdapter
import uz.star.mardex.utils.extension.changeStatusColorWhite
import uz.star.mardex.utils.extension.hideKeyboard
import uz.star.mardex.utils.helpers.showAlertDialog

@AndroidEntryPoint
class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding: FragmentIntroBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val adapter = IntroAdapter()
    private val viewModel: IntroViewModel by viewModels()

    private var currentIndex = 0
    private var listSize = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusColorWhite()
        _binding = FragmentIntroBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.pager.adapter = adapter

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentIndex = position
//                if (currentIndex >= listSize) findNavController().navigate(R.id.)
            }
        })

    }

    private fun loadPagerButtons() {
        binding.dotsIndicator.setViewPager2(binding.pager)
        binding.btnSave.setOnClickListener {
            currentIndex++
            if (currentIndex < listSize)
                binding.pager.setCurrentItem(binding.pager.currentItem + 1, true)
            else findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, messageObserver)
        viewModel.introData.observe(this, introDataObserver)
    }

    private val messageObserver = Observer<String> { message ->
        showMessage(message)
    }

    private val introDataObserver = Observer<List<IntroData>> { list ->
        adapter.submitList(list)
        listSize = list.size
        loadPagerButtons()
    }

    private fun showMessage(text: String) {
        showAlertDialog(text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}