package uz.star.mardex.ui.screen.entry.code_verification

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.databinding.FragmentSmsVerificationBinding
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showAlertDialog


@AndroidEntryPoint
class SmsVerificationFragment : Fragment() {

    private var _binding: FragmentSmsVerificationBinding? = null
    private val binding: FragmentSmsVerificationBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val args: SmsVerificationFragmentArgs by navArgs()
    private val viewModel: SmsVerificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusColorWhite()
        _binding = FragmentSmsVerificationBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObserver()
//        requestHint()
    }

    private fun loadViews() {
        hideEntryLoader()
        binding.verificationScreenSubtitle.text = getString(R.string.telefon_raqamingiz_1_s_ga_sms_kod_yuborildi, args.userData.phone)
        changeResendText()
        showKeyboard(binding.code)
        binding.code.setAnimationEnable(true)

        binding.code.setOtpCompletionListener {
            showEntryLoader()
            viewModel.verifyPhone(args.userData, it)
        }

        binding.aboveCode.setOnClickListener {
            showKeyboard(binding.code)
        }

        binding.btnSendAgain.setOnClickListener {
            if (binding.btnSendAgain.text.toString() == getString(R.string.resend_sms)) {
                viewModel.resendSms(args.userData.phone)
                changeResendText()
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun changeResendText() {
        binding.btnSendAgain.setTextChangeableAfterTime(getString(R.string.send_after), 119, R.string.resend_sms)
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObserver() {
        viewModel.message.observe(this, messageObserver)
        viewModel.registerResponse.observe(this, userDataObserver)
    }

    private val userDataObserver = Observer<UserData> {
        hideKeyboard(binding.root)
        showAlertDialog(it.name)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private val messageObserver = Observer<MessageData> {
        hideEntryLoader()
        it.onResource {
            showAlertDialog(it)
        }.onMessage {
            showAlertDialog(it)
        }
    }

    /*private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent = Auth.CredentialsApi.getHintPickerIntent(
            GoogleApiClient.Builder(requireActivity()).build(), hintRequest
        )
        startIntentSenderForResult(
            intent.intentSender,
            101010, null, 0, 0, 0, Bundle()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101010) {
            if (resultCode == RESULT_OK) {
                val credential: Credential = data?.getParcelableExtra(Credential.EXTRA_KEY)?: return
                Log.d("T12T", "phone = " + credential.id)
                // credential.getId();  <-- will need to process phone number string
            }
        }
    }
*/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}