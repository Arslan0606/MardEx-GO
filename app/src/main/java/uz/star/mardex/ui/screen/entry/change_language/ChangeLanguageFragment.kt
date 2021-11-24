package uz.star.mardex.ui.screen.entry.change_language

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentChangeLanguageBinding
import uz.star.mardex.utils.helpers.*
import javax.inject.Inject

@AndroidEntryPoint
class ChangeLanguageFragment : AppCompatActivity() {

    private var _binding: FragmentChangeLanguageBinding? = null
    private val binding: FragmentChangeLanguageBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    @Inject
    lateinit var storage: LocalStorage
    private lateinit var lan: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentChangeLanguageBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_white)
        setContentView(binding.root)
        loadViews()
    }

    private fun loadViews() {
        binding.apply {
            firstLan = storage.langLocal
            loadLangOptions()

            imageViewHeaderExit.setOnClickListener {
                finish()
            }
        }
    }

    private lateinit var firstLan: String

    @Suppress("DEPRECATION")
    private fun loadLangOptions() {
        binding.apply {
            lan = storage.langLocal

            btnUzb.setOnClickListener {
                bgUz()
                changeLang("default")
            }

            btnRus.setOnClickListener {
                bgRus()
                changeLang("ru")
            }

            btnUzbKirill.setOnClickListener {
                bgKirill()
                changeLang("cy")
            }

            val checkedBg = when (lan) {
                "cy" -> {
                    bgKirill()
                    checkBackgroundUzbKirill
                }
                LANG_RUS -> {
                    bgRus()
                    checkBackgroundRus
                }
                else -> {
                    bgUz()
                    checkBackgroundUzb
                }
            }
            checkedBg.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.language_selected))
        }
    }

    private fun bgUz() {
        binding.apply {
            checkBackgroundUzb.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.language_selected))
            checkBackgroundRus.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.light_white))
            checkBackgroundUzbKirill.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.light_white))
        }
    }

    private fun bgRus() {
        binding.apply {
            checkBackgroundRus.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.language_selected))
            checkBackgroundUzb.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.light_white))
            checkBackgroundUzbKirill.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.light_white))
        }
    }

    private fun bgKirill() {
        binding.apply {
            checkBackgroundUzbKirill.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.language_selected))
            checkBackgroundUzb.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.light_white))
            checkBackgroundRus.setCardBackgroundColor(ContextCompat.getColor(this@ChangeLanguageFragment, R.color.light_white))
        }
    }

    private fun changeLang(language: String) {
        storage.langLocal = language
        storage.lang = if (language == "default") LANG_UZ else if (language == "ru") LANG_RUS else LANG_KRILL
        changeLang()
        binding.apply {
            textViewHeaderName.text = getString(R.string.change_language)
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
        setResult(LANG_CHANGE)
    }
}