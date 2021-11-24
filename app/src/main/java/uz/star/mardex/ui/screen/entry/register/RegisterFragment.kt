package uz.star.mardex.ui.screen.entry.register

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.databinding.FragmentRegisterBinding
import uz.star.mardex.model.requests.registration.RegistrationData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.regions.CityResponse
import uz.star.mardex.model.response.regions.RegionResponse
import uz.star.mardex.model.response.regions.SpinnerRegion
import uz.star.mardex.utils.custom.NothingSelectedSpinnerAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showAlertDialog

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: RegisterViewModel by viewModels()
    private var isMan = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.textMan.setOnClickListener {
            binding.textMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.textMan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_man_white, 0, 0, 0)
            binding.textMan.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.new_green))

            binding.textWoman.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_number_color))
            binding.textWoman.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_woman_black, 0, 0, 0)
            binding.textWoman.background = null
            isMan = true
        }

        binding.textWoman.setOnClickListener {
            binding.textWoman.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.textWoman.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_woman_white, 0, 0, 0)
            binding.textWoman.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.new_green))

            binding.textMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_number_color))
            binding.textMan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_man_black, 0, 0, 0)
            binding.textMan.background = null
            isMan = false
        }

        binding.btnCity.setOnClickListener {
            binding.spinnerCity.performClick()
        }
        binding.btnRegion.setOnClickListener {
            binding.spinnerRegion.performClick()
        }
        binding.txtPolicy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mardex.uz/uz/public-offer"))
            startActivity(browserIntent)
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnGoLogin.setOnClickListener { findNavController().popBackStack() }
        binding.btnNext.setOnClickListener {
            var reg = ""
            try {
                reg = (binding.spinnerRegion.selectedItem as SpinnerRegion).getId()
            } catch (e: Exception) {
            }

            var city = ""
            try {
                city = (binding.spinnerCity.selectedItem as SpinnerRegion).getId()
            } catch (e: Exception) {
            }
            if (binding.checkboxConfirmPolicy.isChecked) {
                if (binding.etPhone.rawText.length != 9) return@setOnClickListener
                val phoneNumber = "+998${binding.etPhone.rawText}"
                val fullName = binding.etFullName.text.toString()
                val password = binding.etPassword.text.toString()
                val confirmPassword = binding.etConfirmPassword.text.toString()
                if (password == confirmPassword) {
                    if (password.length >= 4) {
                        if (fullName.isEmpty()) {
                            binding.etFullName.error = getString(R.string.enter_name_surname)
                            binding.etFullName.requestFocus()
                        } else {
                            if (reg.isEmpty()) {
                                showAlertDialog(getString(R.string.choose_region_))
                            } else {
                                if (city.isEmpty()) {
                                    showAlertDialog(getString(R.string.choose_city_))
                                } else {
                                    showEntryLoader()
                                    viewModel.sendSms(phoneNumber)
                                }
                            }
                        }
                    } else {
                        showAlertDialog(getString(R.string.password_lenth_no))
                        binding.etPassword.error = getString(R.string.password_lenth_no)
                    }
                } else {
                    showAlertDialog(R.string.password_not_same)
                }
            } else {
                showAlertDialog(R.string.check_policy)
            }

        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, messageObserver)
        viewModel.sendDone.observe(this, sendDoneObserver)
        viewModel.loader.observe(viewLifecycleOwner, loaderObserver)
        viewModel.responseCities.observe(this, responseCitiesObserver)
        viewModel.responseRegions.observe(this, responseRegionsObserver)
    }

    private var regions: List<RegionResponse>? = null
    private val responseRegionsObserver = Observer<List<RegionResponse>> { data ->
        regions = data
        initRegions(data)
    }

    private fun initRegions(data: List<RegionResponse>) {
        binding.apply {
            val subjectMs = ArrayList<SpinnerRegion>()
            for (subject in data) {
                subjectMs.add(SpinnerRegion(title = subject.title?.toLocalString() ?: "", id = subject.id ?: ""))
            }

            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subjectMs)

            binding.spinnerRegion.adapter = NothingSelectedSpinnerAdapter(
                arrayAdapter,
                R.layout.contact_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                binding.root.context
            )
            binding.spinnerRegion.prompt = getString(R.string.select_an_option)
        }
    }

    private var cities: List<CityResponse>? = null
    private val responseCitiesObserver = Observer<List<CityResponse>> { data ->
        cities = data
        initCities(data)
    }

    private fun initCities(data: List<CityResponse>) {
        binding.spinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val d = binding.spinnerRegion.adapter.getItem(position) as SpinnerRegion
                d.let {
                    val subjectMs = ArrayList<SpinnerRegion>()
                    val filtered = data.filter { it.cityId?.id == d.getId() }

                    for (subject in filtered) {
                        subjectMs.add(SpinnerRegion(title = subject.title?.toLocalString() ?: "", id = subject.id ?: ""))
                    }

                    val arrayAdapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subjectMs)
                    binding.spinnerCity.adapter = arrayAdapter
                    binding.spinnerCity.prompt = getString(R.string.select_an_option)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private val loaderObserver = Observer<Boolean> { b ->
        if (b) showEntryLoader() else hideEntryLoader()
    }

    private val sendDoneObserver = Observer<Boolean> { success ->
        if (success) {
            val fullName = binding.etFullName.text.toString()
            val phoneNumber = "+998${binding.etPhone.rawText}"
            val promocode = if (binding.etPromoCode.rawText.toString() == "")
                0
            else {
                binding.etPromoCode.rawText.toString().toInt()
            }
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if (password == confirmPassword) {
                if (password.length >= 4) {
                    val action = RegisterFragmentDirections.actionRegisterFragmentToSmsVerificationFragment(
                        RegistrationData(null, fullName, password, phoneNumber, promocode)
                    )
                    findNavController().navigate(action)
                } else {
                    showAlertDialog(getString(R.string.password_lenth_no))
                    binding.etPassword.error = getString(R.string.password_lenth_no)
                }
            } else {
                showAlertDialog(R.string.password_not_same)
            }
        }
    }

    private val messageObserver = Observer<MessageData> {
        hideEntryLoader()
        it.onResource {
            showAlertDialog(it)
        }.onMessage {
            showAlertDialog(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}