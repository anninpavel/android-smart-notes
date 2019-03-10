@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.components

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import smartnotes.SmartNotesApplication
import smartnotes.di.modules.ActivityBindingModule
import smartnotes.di.modules.ApplicationModule
import smartnotes.di.modules.NavigationModule
import smartnotes.di.modules.ViewModelBindingModule
import javax.inject.Singleton

/**
 * Основной компонент приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityBindingModule::class,
        ViewModelBindingModule::class,
        NavigationModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<SmartNotesApplication> {

    /** Адаптер, позволя.obq [dagger.Subcomponent.Builder] реализовать [AndroidInjector.Factory].*/
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: SmartNotesApplication): Builder

        fun build(): ApplicationComponent
    }
}
