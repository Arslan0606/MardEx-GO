package uz.star.mardex.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.star.mardex.ui.screen.entry.code_verification.SmsVerificationRepository
import uz.star.mardex.ui.screen.entry.code_verification.SmsVerificationRepositoryImpl
import uz.star.mardex.ui.screen.entry.intro.IntroRepository
import uz.star.mardex.ui.screen.entry.intro.IntroRepositoryImpl
import uz.star.mardex.ui.screen.entry.login.LoginRepository
import uz.star.mardex.ui.screen.entry.register.RegisterRepository
import uz.star.mardex.ui.screen.entry.register.RegisterRepositoryImpl
import uz.star.mardex.ui.screen.entry.reset_passpord.RestorePasswordRepository
import uz.star.mardex.ui.screen.entry.reset_passpord.RestorePasswordRepositoryImpl
import uz.star.mardex.ui.screen.login.LoginRepositoryImpl
import uz.star.mardex.ui.screen.working.connected_workers.ConnectedWorkersListRepository
import uz.star.mardex.ui.screen.working.connected_workers.ConnectedWorkersListRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy.FillingVacancyRepository
import uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy.FillingVacancyRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.job_chooser.JobChooserRepository
import uz.star.mardex.ui.screen.working.home_fragment.job_chooser.JobChooserRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.OwnNotificationsRepository
import uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.OwnNotificationsRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.promocode.PromocodeRepository
import uz.star.mardex.ui.screen.working.home_fragment.promocode.PromocodeRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.responded_workers.RespondedWorkersRepository
import uz.star.mardex.ui.screen.working.home_fragment.responded_workers.RespondedWorkersRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.send_request_workers.SendWorkersRequestRepository
import uz.star.mardex.ui.screen.working.home_fragment.send_request_workers.SendWorkersRequestRepositoryImpl
import uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.WorkersRepository
import uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.WorkersRepositoryImpl
import uz.star.mardex.ui.screen.working.news.NewsRepository
import uz.star.mardex.ui.screen.working.news.NewsRepositoryImpl
import uz.star.mardex.ui.screen.working.profile.ProfileRepository
import uz.star.mardex.ui.screen.working.profile.ProfileRepositoryImpl
import uz.star.mardex.ui.screen.working.profile.editpassword_fragment.EditPasswordRepository
import uz.star.mardex.ui.screen.working.profile.editpassword_fragment.EditPasswordRepositoryImpl
import uz.star.mardex.ui.screen.working.profile.editpersonaldata_fragment.EditPersonalDataRepository
import uz.star.mardex.ui.screen.working.profile.editpersonaldata_fragment.EditPersonalDataRepositoryImpl
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 09-Jan-21
 **/

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun getJobsChooserRepository(repo: JobChooserRepositoryImpl): JobChooserRepository

    @Binds
    @Singleton
    fun getWorkersFragmentRepository(repo: WorkersRepositoryImpl): WorkersRepository

    @Binds
    @Singleton
    fun getConnectedFragmentRepository(repo: ConnectedWorkersListRepositoryImpl): ConnectedWorkersListRepository

    @Binds
    @Singleton
    fun getProfileRepository(repo: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun getIntroRepository(repo: IntroRepositoryImpl): IntroRepository

    @Binds
    @Singleton
    fun getLoginRepository(repo: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    fun getRegisterRepository(repo: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @Singleton
    fun getSmsVerificationRepository(repo: SmsVerificationRepositoryImpl): SmsVerificationRepository

    @Binds
    @Singleton
    fun getFillingVacancyRepository(repo: FillingVacancyRepositoryImpl): FillingVacancyRepository

    @Binds
    fun getSendWorkersRequestRepository(repo: SendWorkersRequestRepositoryImpl): SendWorkersRequestRepository

    @Binds
    @Singleton
    fun getRespondedWorkersRepository(repo: RespondedWorkersRepositoryImpl): RespondedWorkersRepository

    @Binds
    @Singleton
    fun getEditPersonalDataRepository(repo: EditPersonalDataRepositoryImpl): EditPersonalDataRepository

    @Binds
    @Singleton
    fun getEditPasswordRepository(repo: EditPasswordRepositoryImpl): EditPasswordRepository

    @Binds
    @Singleton
    fun getRestorePasswordRepository(repo: RestorePasswordRepositoryImpl): RestorePasswordRepository

    @Binds
    @Singleton
    fun getNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    fun getPromocodeRepository(promocodeRepository: PromocodeRepositoryImpl): PromocodeRepository

    @Binds
    @Singleton
    fun geOwnNotificationsRepository(promocodeRepository: OwnNotificationsRepositoryImpl): OwnNotificationsRepository

}