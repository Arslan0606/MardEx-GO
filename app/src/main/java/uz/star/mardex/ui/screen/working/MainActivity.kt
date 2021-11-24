package uz.star.mardex.ui.screen.working

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.ActivityMainBinding
import uz.star.mardex.ui.screen.entry.EntryActivity
import uz.star.mardex.ui.screen.entry.change_language.ChangeLanguageFragment
import uz.star.mardex.ui.screen.working.news.NewsActivity
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.playStoreUrl
import uz.star.mardex.utils.extension.shareText
import uz.star.mardex.utils.extension.show
import uz.star.mardex.utils.helpers.LANG_CHANGE
import uz.star.mardex.utils.helpers.LIGHT_MODE
import uz.star.mardex.utils.helpers.MyContextWrapper
import uz.star.mardex.utils.helpers.NIGHT_MODE
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var localStorage: LocalStorage

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchNightMode.isChecked = localStorage.nightMode == NIGHT_MODE
        MapKitFactory.initialize(this)
        binding.navigationView.itemIconTintList = null
        binding.mainContent.bottomMenu.setupWithNavController(findNavController(R.id.fragment))
        changeDataInMenu()
        binding.headerLayout.layoutHeader.setOnClickListener { closeDrawer() }

        binding.btnLogOut.setOnClickListener {
            LocalStorage.instance.registrated = false
            startActivity(Intent(this, EntryActivity::class.java))
            finish()
        }

        binding.layoutNightMode.setOnClickListener {
            binding.switchNightMode.performClick()
        }

        binding.switchNightMode.setOnCheckedChangeListener { _, isNightMode ->
            if (isNightMode) {
                localStorage.nightMode = NIGHT_MODE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                localStorage.nightMode = LIGHT_MODE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.layoutSettings.setOnClickListener {
            closeDrawer()
            findNavController(R.id.fragment).navigate(R.id.profileFragment)
        }

        binding.headerLayout.imageViewHeaderExit.setOnClickListener {
            closeDrawer()
        }

        binding.layoutLanguage.setOnClickListener {
            closeDrawer()
            val intent = Intent(this, ChangeLanguageFragment::class.java)
            startActivityForResult(intent, 1)
//            hideBottomMenu()
//            findNavController(R.id.fragment).navigate(R.id.changeLanguageFragment)
        }

        binding.layoutNews.setOnClickListener {
//            showAlertDialog(R.string.alert, R.string.currently_no_news)
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        binding.layoutShare.setOnClickListener {
            shareText(playStoreUrl())
        }

        binding.layoutCall.setOnClickListener {
            call()
        }

        binding.layoutMardex.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mardex.uz"))
            startActivity(browserIntent)
        }

        binding.layoutPromocode.setOnClickListener {
            findNavController(R.id.fragment).navigate(R.id.promocodeFragment)
            closeDrawer()
        }
    }

    private fun call() {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.fromParts("tel", getString(R.string._998_93_000_88_55_call), null)
        startActivity(callIntent)
    }

    fun changeDataInMenu() {
        try {
            binding.headerLayout.textViewHeaderName.text = LocalStorage.instance.name
            binding.headerLayout.textPhone.text = LocalStorage.instance.phone
        } catch (e: Exception) {

        }
    }

    private fun changeMenuTexts() {
        binding.settingsText.text = getString(R.string.settings)
        binding.newsText.text = getString(R.string.news)
        binding.textOurSite.text = getString(R.string.t_site)
        binding.languageText.text = getString(R.string.soft_lang)
        binding.shareText.text = getString(R.string.share)
        binding.switchText.text = getString(R.string.night_mode)
        binding.promocodeText.text = getString(R.string.promocode_text)

        binding.mainContent.bottomMenu.menu.findItem(R.id.homeFragment).title = getString(R.string.home)
        binding.mainContent.bottomMenu.menu.findItem(R.id.ordersFragment).title = getString(R.string.orders)
        binding.mainContent.bottomMenu.menu.findItem(R.id.connectedWorkersFragment).title = getString(R.string.connected)
        binding.mainContent.bottomMenu.menu.findItem(R.id.profileFragment).title = getString(R.string.profile)

        binding.btnLogOut.text = getString(R.string.menu_logout)

    }

    fun hideBottomMenu() {
        try {
            binding.mainContent.bottomMenuArea.hide()
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } catch (e: Exception) {

        }
    }

    fun showBottomMenu() {
        try {
            binding.mainContent.bottomMenuArea.show()
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } catch (e: Exception) {

        }
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        binding.drawerLayout.close()
    }

    fun hideLoader() {
        try {
            binding.mainContent.loader.root.hide()
        } catch (e: Exception) {

        }
    }

    fun showLoader() {
        try {
            binding.mainContent.loader.root.show()
        } catch (e: Exception) {

        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LocalStorage.instance.langLocal))
    }

    @Suppress("DEPRECATION")
    fun changeLang() {
        val context: Context = MyContextWrapper.wrap(this, LocalStorage.instance.langLocal)
        resources.updateConfiguration(
            context.resources.configuration,
            context.resources.displayMetrics
        )
        changeMenuTexts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == LANG_CHANGE) {
            if (findNavController(R.id.fragment).currentDestination?.id != null)
                findNavController(R.id.fragment).navigate(findNavController(R.id.fragment).currentDestination?.id!!)
            changeLang()
        }
    }

    override fun onRestart() {
        super.onRestart()
        try {
            when (findNavController(R.id.fragment).currentDestination?.id) {
                R.id.homeFragment -> {
                    showBottomMenu()
                }
                R.id.connectedWorkersFragment -> {
                    showBottomMenu()
                }

                R.id.ordersFragment -> {
                    showBottomMenu()
                }

                R.id.profileFragment -> {
                    showBottomMenu()
                }
                else -> {
                    hideBottomMenu()
                }
            }
        } catch (e: Exception) {

        }
    }
}