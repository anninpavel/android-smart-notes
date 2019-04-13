@file:JvmMultifileClass

package smartnotes.utils.android

import android.app.Activity
import android.content.Intent
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.containsKey
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer
import smartnotes.utils.kotlin.Supplier
import java.util.concurrent.atomic.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private typealias ActivityStarter = Consumer<Pair<Int, Intent>>
private typealias ActivityRequestCodeGenerator = Supplier<Int>

/**
 * Создает новый экземпляр [ActivityResultManager] для [AppCompatActivity].
 *
 * @receiver [AppCompatActivity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun AppCompatActivity.activityResultManager(): ReadOnlyProperty<AppCompatActivity, ActivityResultManager> {
    return object : ReadOnlyProperty<AppCompatActivity, ActivityResultManager> {

        private val requestCode = AtomicInteger()
        private val manager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityResultManagerImpl(
                activityStarter = { (requestCode, intent) -> startActivityForResult(intent, requestCode) },
                requestCodeGenerator = { requestCode.incrementAndGet() }
            ).also { lifecycle.addObserver(it) }
        }

        override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): ActivityResultManager {
            return manager
        }
    }
}

/**
 * Менеджер обработки результатов дочерних [Activity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface ActivityResultManager {

    /**
     * Обратный вызов для обработки результата полученого от дочерней [Activity].
     * Метод должен вызываться в [Activity.onActivityResult].
     *
     * @param requestCode Код заапроса дочерней [Activity].
     * @param resultCode Код заверешения дочерней [Activity].
     * @param data данные передаваемые дочерней [Activity].
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean

    /**
     * Запускает [intent], обработка результат выполняется по схеме [RequiredResultHandler].
     *
     * @param intent Описание [Activity] для запуска.
     */
    fun launch(intent: Intent, onAccepted: Consumer<Intent?>, onDenied: Action)
}

/**
 * Реализация менеджера обработки результата дочерней [Activity].
 * Менеджер поддерживает [LifecycleObserver].
 *
 * @property activityStarter Запускает [Activity].
 * @property requestedHandlers Коллекция обработчиков результата.
 * @property pendingResults Коллекция ожидающих результатов.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private class ActivityResultManagerImpl(
    private val activityStarter: ActivityStarter,
    private val requestCodeGenerator: ActivityRequestCodeGenerator
) : ActivityResultManager, LifecycleObserver {

    private val requestedHandlers = SparseArray<ActivityResultHandler>()
    private val pendingResults = SparseArray<ActivityResult>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val isContains = requestedHandlers.containsKey(requestCode)
        if (isContains) {
            val handler = requestedHandlers[requestCode]
            requestedHandlers.remove(requestCode)
            pendingResults[requestCode] = ActivityResult(handler, resultCode, data)
        }
        return isContains
    }

    override fun launch(intent: Intent, onAccepted: Consumer<Intent?>, onDenied: Action) {
        starter(intent, RequiredResultHandler(onAccepted, onDenied))
    }

    private fun starter(intent: Intent, handler: ActivityResultHandler) {
        val requestCode = requestCodeGenerator()
        requestedHandlers[requestCode] = handler
        activityStarter(requestCode to intent)
    }

    /** Этот метод вызывается для обработки ожидающих результатов. */
    @Suppress(names = ["unused"])
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onReady() {
        with(pendingResults) {
            forEach { _, value -> value.onActivityResult() }
            clear()
        }
    }

    /**
     * Обрабатывает результат дочерней [Activity].
     *
     * @receiver [ActivityResult].
     */
    private fun ActivityResult.onActivityResult() {
        handler.onActivityResult(resultCode, data)
    }

    /**
     * Результат дочерней [Activity].
     *
     * @property handler Обработчик результат дочерней [Activity].
     * @property resultCode Код заверешения дочерней [Activity].
     * @property data данные передаваемые дочерней [Activity].
     *
     * @author Pavel Annin (https://github.com/anninpavel).
     */
    private data class ActivityResult(
        val handler: ActivityResultHandler,
        val resultCode: Int,
        val data: Intent?
    )
}

/**
 * Интерфейс обработчика результата дочерней [Activity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private interface ActivityResultHandler {

    /**
     * Обратный вызов для обработки результата полученого от дочерней [Activity].
     *
     * @param resultCode Код заверешения дочерней [Activity].
     * @param data данные передаваемые дочерней [Activity].
     */
    fun onActivityResult(resultCode: Int, data: Intent?)
}

/**
 * Обработчик резултата дочерней [Activity].
 *
 * Вызывается [onAccepted] если [Activity.RESULT_OK], иначе [onDenied].
 *
 * @property onAccepted Содержит [Intent] полученный от дочерней [Activity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private class RequiredResultHandler(
    private val onAccepted: Consumer<Intent?>,
    private val onDenied: Action
) : ActivityResultHandler {

    override fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) onAccepted(data) else onDenied()
    }
}
