package com.artf.fixer.utility

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.artf.fixer.network.FixerApi
import com.artf.fixer.network.RetrofitModule
import com.artf.fixer.repository.Repository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
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

        /**
         * Allows tests to replace the default implementations.
         */
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

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application) : ServiceLocator {
    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    // thread pool used for network requests
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