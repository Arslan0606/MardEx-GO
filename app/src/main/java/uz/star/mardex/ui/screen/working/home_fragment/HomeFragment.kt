package uz.star.mardex.ui.screen.working.home_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentHomeBinding
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy.FillingVacancyViewModel
import uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.OwnNotificationsActivity
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showAlertDialog

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: FillingVacancyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showBottomMenu()
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        changeStatusColorMainColor()
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideLoader()
        viewModel.sendLanguage()
        binding.findWorkerButton.setOnClickListener {
            hideBottomMenu()
            findNavController().navigate(R.id.action_homeFragment_to_jobChooserFragment)
        }
        binding.postWorkButton.setOnClickListener {
            showAlertDialog(R.string.alert, R.string.now_working_on_it)
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }


        binding.news.setOnClickListener {
            startActivity(Intent(requireActivity(), OwnNotificationsActivity::class.java))
        }

        showBottomMenu()
        binding.menu.setOnClickListener { (activity as MainActivity).openDrawer() }
        binding.name.text = LocalStorage.instance.name

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}