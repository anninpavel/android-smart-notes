@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import smartnotes.presentation.screens.notes.NotesActivity
import smartnotes.presentation.screens.splash.SplashActivity

/** @author Pavel Annin (https://github.com/anninpavel). */
@Module
interface ActivityBindingModule {

    @ContributesAndroidInjector
    fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    fun bindNotesActivity(): NotesActivity
}
