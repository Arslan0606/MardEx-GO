package uz.star.mardex.model.data_exchange

import uz.star.mardex.model.response.server.worker.WorkerData
import java.io.Serializable

/**
 * Created by Farhod Tohirov on 29-Mar-21
 **/

data class WorkersList (val list: List<WorkerData>) : Serializable