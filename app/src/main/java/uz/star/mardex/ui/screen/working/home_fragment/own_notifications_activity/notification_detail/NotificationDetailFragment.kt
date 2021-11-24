package uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.notification_detail

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
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentNotificationDetailBinding
import uz.star.mardex.utils.helpers.*
import uz.star.mardexworker.model.response.notification_for_user.OwnNotificationResponse
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDetailFragment : Fragment() {

    private var _binding: FragmentNotificationDetailBinding? = null
    private val binding: FragmentNotificationDetailBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: OwnNotificationDetailViewModel by viewModels()

    private val args: NotificationDetailFragmentArgs by navArgs()

    @Inject
    lateinit var storage: LocalStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            txtTitle.text = args.notificationData.title!!.uz
            when (storage.lang) {
                LANG_UZ -> {
                    txtTitle.text = args.notificationData.title!!.uz
                    txtDesc.text = args.notificationData.body!!.uz
                }

                LANG_RUS -> {
                    txtTitle.text = args.notificationData.title!!.ru
                    txtDesc.text = args.notificationData.body!!.ru
                }
                LANG_KRILL -> {
                    txtTitle.text = args.notificationData.title!!.uzKr
                    txtDesc.text = args.notificationData.body!!.uzKr
                }
                else -> {
                    txtTitle.text = args.notificationData.title!!.en
                    txtDesc.text = args.notificationData.body!!.en
                }
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            loadObservers()
        }
    }

    private fun loadObservers() {
        viewModel.changeStatusToRead(args.notificationData.id!!)
        viewModel.notificationLiveData.observe(viewLifecycleOwner, notificationObserver)
    }


    private val notificationObserver = Observer<OwnNotificationResponse> {

    }
}