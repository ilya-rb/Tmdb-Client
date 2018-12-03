package com.illiarb.tmdbclient.coreimpl

import com.illiarb.tmdblcient.core.modules.AppInteractor
import com.illiarb.tmdblcient.core.system.WorkManager
import javax.inject.Inject

/**
 * @author ilya-rb on 03.12.18.
 */
class AppInteractorImpl @Inject constructor(
    private val workManager: WorkManager
) : AppInteractor {

    override fun onAppStarted() {
        workManager.initialize()
        workManager.schedulerPeriodicConfigurationFetch()
    }
}