package uz.star.mardex.ui.screen.entry.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.utils.extension.changeStatusColorMainColor
import uz.star.mardex.utils.extension.changeStatusColorWhite
import java.util.concurrent.Executors

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeStatusColorWhite()
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(1000)
            requireActivity().runOnUiThread {
                if (LocalStorage.instance.registrated) {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
                }
            }
        }
    }

}