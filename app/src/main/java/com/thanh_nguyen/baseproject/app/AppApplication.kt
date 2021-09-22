package com.thanh_nguyen.baseproject.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.thanh_nguyen.baseproject.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class AppApplication: Application(), LifecycleObserver, KodeinAware {
    override fun onCreate() {
        super.onCreate()
        context = this
        //AppFirebaseMessageService.init(this)
        //FirebaseManager.init()
    }

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@AppApplication))
        import(appModule)
    }

    companion object{
        private var context: Context? = null
        fun getContext(): Context{
            return context!!
        }
    }
}