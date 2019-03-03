package smartnotes

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import smartnotes.di.ComponentFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Класс приложения.
 *
 * @property component Основной компонент графа зависимостей.
 * @property activityInjector Injector для [android.app.Activity] приложения.
 * @property loggerTree Стратегия обработки логов для [Timber].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class SmartNotesApplication : Application(), HasActivityInjector {

    private val component by lazy(mode = LazyThreadSafetyMode.NONE) { ComponentFactory().build(this) }
    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var loggerTree: Provider<Timber.Tree>

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        configLogging()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    private fun configLogging(tree: Timber.Tree = loggerTree.get()) {
        Timber.uprootAll()
        Timber.plant(tree)
    }
}
