package uz.star.mardex.ui.screen.working.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.star.mardex.databinding.FragmentOrdersBinding
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.OwnNotificationsActivity

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding: FragmentOrdersBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            binding.menu.setOnClickListener { (activity as MainActivity).openDrawer() }

            news.setOnClickListener {
                startActivity(Intent(requireActivity(), OwnNotificationsActivity::class.java))
            }
        }
    }
}