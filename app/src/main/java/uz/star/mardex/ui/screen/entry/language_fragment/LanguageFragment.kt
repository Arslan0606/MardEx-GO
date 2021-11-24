package uz.star.mardex.ui.screen.entry.language_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentLanguageBinding
import uz.star.mardex.ui.screen.entry.EntryActivity
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.utils.extension.changeStatusColorWhite
import uz.star.mardex.utils.extension.hideKeyboard
import uz.star.mardex.utils.helpers.*
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding: FragmentLanguageBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    @Inject
    lateinit var storage: LocalStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusColorWhite()
        _binding = FragmentLanguageBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViews()
    }

    private fun loadViews() {
        changeLang(LANG_UZ)
        binding.apply {
            loadLangOptions()

            backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }

            btnSave.setOnClickListener {
                if (LocalStorage.instance.registrated)
                    findNavController().popBackStack()
                else
                    findNavController().navigate(R.id.action_languageFragment_to_introFragment)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun loadLangOptions() {
        binding.apply {
            val checkedBg = when (storage.langLocal) {
                LANG_UZ -> checkBackgroundUzb
                LANG_RUS -> checkBackgroundRus
                LANG_KRILL -> checkBackgroundUzbKirill
                else -> checkBackgroundUzb
            }
            checkedBg.setCardBackgroundColor(resources.getColor(R.color.new_green))

            btnUzb.setOnClickListener {
                checkBackgroundUzb.setCardBackgroundColor(resources.getColor(R.color.new_green))
                checkBackgroundRus.setCardBackgroundColor(resources.getColor(R.color.light_white))
                checkBackgroundUzbKirill.setCardBackgroundColor(resources.getColor(R.color.light_white))
                changeLang("default")
            }

            btnRus.setOnClickListener {
                checkBackgroundRus.setCardBackgroundColor(resources.getColor(R.color.new_green))
                checkBackgroundUzb.setCardBackgroundColor(resources.getColor(R.color.light_white))
                checkBackgroundUzbKirill.setCardBackgroundColor(resources.getColor(R.color.light_white))
                changeLang("ru")
            }

            btnUzbKirill.setOnClickListener {
                checkBackgroundUzbKirill.setCardBackgroundColor(resources.getColor(R.color.new_green))
                checkBackgroundUzb.setCardBackgroundColor(resources.getColor(R.color.light_white))
                checkBackgroundRus.setCardBackgroundColor(resources.getColor(R.color.light_white))
                changeLang("cy")
            }
        }
    }

    private fun changeLang(language: String) {
        storage.langLocal = language
        storage.lang = if (language == "default") LANG_UZ else if (language == "ru") LANG_RUS else LANG_KRILL
        if (storage.registrated)
            (activity as MainActivity).changeLang()
        else
            (activity as EntryActivity).changeLang()
        binding.apply {
            textTitle.text = getString(R.string.welcome_mardex)
            textChooseLanguage.text = getString(R.string.choose_lang)
            btnSave.text = getString(R.string.next)
        }
    }

}