package uz.star.mardex.ui.screen.entry.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.GetDataApi
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.intro.IntroData
import uz.star.mardex.model.response.server.title.Title
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 11-Jan-21
 **/

class IntroRepositoryImpl @Inject constructor(
    private val api: GetDataApi,
    private val localStorage: LocalStorage
) : IntroRepository, SafeApiRequest() {

    override fun getIntroData(): LiveData<ResultData<List<IntroData>>> {
        val resultLiveData = MutableLiveData<ResultData<List<IntroData>>>()

        Coroutines.ioThenMain(
            { apiRequest { api.getIntro(localStorage.lang) } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(if (it.data.isNotEmpty()) it.data else getIntroFromLocal())
                    else
                        resultLiveData.value = ResultData.data(getIntroFromLocal())
                }
                data?.onMessage {
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.data(getIntroFromLocal())
                    }
                }
            }
        )
        return resultLiveData
    }

    private fun getIntroFromLocal(): List<IntroData> {
        return listOf(
            IntroData(
                "",
                "",
                Title(
                    "Ishchi topish biz bilan juda oson!",
                    "С нами найти работника очень просто!",
                    "Ишчи топиш биз билан жуда осон!"
                ),
                Title(
                    "Tezkor topish \uD83D\uDD56",
                    "Быстрый поиск \uD83D\uDD56",
                    "Тезкор топиш \uD83D\uDD56"
                ),
                R.drawable.ic_intro_1
            ),
            IntroData(
                "",
                "",
                Title(
                    "Kerakli kasb turini tanlang!",
                    "Выберите подходящую профессию!",
                    "Керакли касб турини танланг !"
                ),
                Title(
                    "Barcha kasblar mavjud \uD83D\uDE0E",
                    "Доступны все профессии \uD83D\uDE0E",
                    "Барча касблар мавжуд \uD83D\uDE0E"
                ),
                R.drawable.ic_intro_2
            ),
            IntroData(
                "",
                "",
                Title(
                    "Tez va oson ishchi toping va turli bonuslarga ega bo`ling.",
                    "Найдите быстрого и легкого работника и получайте различные бонусы.",
                    "Тез ва осон ишчи топинг ва турли бонусларга эга бўлинг."
                ),
                Title(
                    "Izlashingiz mumkin \uD83D\uDE09",
                    "Вы можете начать поиск \uD83D\uDE09",
                    "Излашингиз мумкин \uD83D\uDE09"
                ),
                R.drawable.ic_intro_3
            )
        )
    }

}