package smartnotes.presentation.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import smartnotes.presentation.navigation.Screens
import smartnotes.presentation.navigation.observe
import smartnotes.utils.extensions.NO_RESOURCE
import javax.inject.Inject

/**
 * Экран загрузки приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var router: Router
    @Inject lateinit var navigatorHolder: NavigatorHolder
    private val navigator = SupportAppNavigator(this, NO_RESOURCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        navigatorHolder.observe(this, navigator)
        router.newRootScreen(Screens.Notes())
    }
}
