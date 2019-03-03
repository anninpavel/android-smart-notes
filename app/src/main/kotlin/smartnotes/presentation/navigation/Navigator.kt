package smartnotes.presentation.navigation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

/**
 * Наблюдатель жизненого цикла для [Navigator].
 *
 * @property navigatorHolder Держатель [Navigator] для подключения его к [ru.terrakok.cicerone.Cicerone].
 * @property navigator Фактический навигатор (осуществляющий переход между экранами).
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Suppress(names = ["unused"])
private class NavigatorLifecycleObserver(
    private val navigatorHolder: NavigatorHolder,
    private val navigator: Navigator
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun subscribe() {
        navigatorHolder.setNavigator(navigator)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unsubscribe() {
        navigatorHolder.setNavigator(navigator)
    }
}

/**
 * Подписывает [Navigator] к [NavigatorHolder], с привязкой к жизненому циклу [LifecycleOwner].
 *
 * @param owner Владелец жиненого цикла ([androidx.appcompat.app.AppCompatActivity], [androidx.fragment.app.Fragment]).
 * @param navigator Фактический навигатор (осуществляющий переход между экранами).
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun NavigatorHolder.observe(owner: LifecycleOwner, navigator: Navigator) {
    owner.lifecycle.addObserver(NavigatorLifecycleObserver(navigatorHolder = this, navigator = navigator))
}
