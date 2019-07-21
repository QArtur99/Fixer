package com.artf.fixer.utility

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.artf.fixer.network.FixerApi
import com.artf.fixer.network.RetrofitModule
import com.artf.fixer.repository.Repository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface ServiceLocator {
    companion object {
        @Volatile
        private var instance: ServiceLocator? = null
        private val LOCK = Any()
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application
                    )
                }
                return instance!!
            }
        }

        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(): Repository

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getFixerApi(): FixerApi
}

open class DefaultServiceLocator(val app: Application) : ServiceLocator {

    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    private val api by lazy {
        RetrofitModule.fixer
    }

    override fun getRepository(): Repository {
        return Repository(
            api = getFixerApi(),
            diskExecutor = getDiskIOExecutor(),
            networkExecutor = getNetworkExecutor()
        )
    }

    override fun getNetworkExecutor(): Executor = NETWORK_IO

    override fun getDiskIOExecutor(): Executor = DISK_IO

    override fun getFixerApi(): FixerApi = api
}