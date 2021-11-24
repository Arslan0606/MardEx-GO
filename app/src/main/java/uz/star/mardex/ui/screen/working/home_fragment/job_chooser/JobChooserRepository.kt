package uz.star.mardex.ui.screen.working.home_fragment.job_chooser

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.jobs.JobCategory

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

interface JobChooserRepository {
    fun getJobs(): LiveData<ResultData<List<JobCategory>>>
}