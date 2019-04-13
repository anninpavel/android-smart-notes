@file:JvmMultifileClass

package smartnotes.utils.android

import android.content.pm.PackageManager
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.containsKey
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer
import smartnotes.utils.kotlin.Predicate
import smartnotes.utils.kotlin.Supplier
import java.util.concurrent.atomic.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private typealias RuntimePermissionRequester = Consumer<Pair<Int, Array<String>>>
private typealias RuntimePermissionChecker = Predicate<String>
private typealias RuntimePermissionRequestCodeGenerator = Supplier<Int>


/**
 * Создает новый экземпляр [RuntimePermissionManager] для [AppCompatActivity].
 *
 * @receiver [AppCompatActivity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun AppCompatActivity.permissionManager(): ReadOnlyProperty<AppCompatActivity, RuntimePermissionManager> {
    return object : ReadOnlyProperty<AppCompatActivity, RuntimePermissionManager> {

        private val requestCode = AtomicInteger()
        private val manager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RuntimePermissionManagerImpl(
                permissionRequester = { (id, permissions) ->
                    ActivityCompat.requestPermissions(this@permissionManager, permissions, id)
                },
                permissionChecker = { permission ->
                    ContextCompat.checkSelfPermission(this@permissionManager, permission) ==
                            PackageManager.PERMISSION_GRANTED
                },
                requestCodeGenerator = { requestCode.incrementAndGet() }
            ).also { lifecycle.addObserver(it) }
        }

        override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): RuntimePermissionManager {
            return manager
        }
    }
}

/**
 * Создает новый экземпляр [RuntimePermissionManager] для [Fragment].
 *
 * @receiver [Fragment].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Fragment.permissionManager(): ReadOnlyProperty<Fragment, RuntimePermissionManager> {
    return object : ReadOnlyProperty<Fragment, RuntimePermissionManager> {

        private val requestCode = AtomicInteger()
        private val manager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RuntimePermissionManagerImpl(
                permissionRequester = { (id, permissions) ->
                    this@permissionManager.requestPermissions(permissions, id)
                },
                permissionChecker = { permission ->
                    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
                },
                requestCodeGenerator = { requestCode.incrementAndGet() }
            ).also { lifecycle.addObserver(it) }
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): RuntimePermissionManager {
            return manager
        }
    }
}

/**
 * Менеджер взаимодействия с запросами разрешений.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface RuntimePermissionManager {

    /**
     * Обратный вызов для результата от запроса разрешений.
     * Этот метод вызывается для каждого вызова requestPermissions.
     * Метод должен вызываться в [ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult].
     *
     * @param requestCode Код заапроса разрешения переданный в requestPermissions.
     * @param permissions Список запрошенных разрешений.
     * @param grantResults Список результатов запрошенных разрешений.
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)

    /**
     * Запрашивает разрешения [permissions], обработка разрешений выполняется по схеме [UnnecessaryPermissionsHandler].
     *
     * @param permissions Список запрашиваемых разрешений.
     */
    fun requestAndRun(permissions: List<String>, onAccepted: Action, onDenied: Consumer<List<String>>)

    /**
     * Запрашивает разрешения [permissions], обработка разрешений выполняется по схеме [RequiredPermissionsHandler].
     *
     * @param permissions Список запрашиваемых разрешений.
     */
    fun requestThenRun(permissions: List<String>, onAccepted: Action, onDenied: Consumer<List<String>>)
}

/**
 * Реализация менеджера взаимодействия с запросами разрешений.
 * Менеджер поддерживает [LifecycleObserver].
 *
 * @property permissionRequester Запрашивает указанное разрешение.
 * @property permissionChecker Проверяет указанное разрешение на доступность.
 * @property requestedHandlers Коллекция обработчиков запросов разрешений.
 * @property pendingResults Коллекция ожидающих результатов разрешений.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private class RuntimePermissionManagerImpl(
    private val permissionRequester: RuntimePermissionRequester,
    private val permissionChecker: RuntimePermissionChecker,
    private val requestCodeGenerator: RuntimePermissionRequestCodeGenerator
) : RuntimePermissionManager, LifecycleObserver {

    private val requestedHandlers = SparseArray<RuntimePermissionHandler>()
    private val pendingResults = SparseArray<RuntimePermissionResult>()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestedHandlers.containsKey(requestCode)) {
            val handler = requestedHandlers[requestCode]
            requestedHandlers.remove(requestCode)
            pendingResults[requestCode] = RuntimePermissionResult(handler, permissions, grantResults)
        }
    }

    override fun requestAndRun(permissions: List<String>, onAccepted: Action, onDenied: Consumer<List<String>>) {
        request(permissions, UnnecessaryPermissionsHandler(onAccepted, onDenied), onAccepted)
    }

    override fun requestThenRun(permissions: List<String>, onAccepted: Action, onDenied: Consumer<List<String>>) {
        request(permissions, RequiredPermissionsHandler(onAccepted, onDenied), onAccepted)
    }

    private fun request(permissions: List<String>, handler: RuntimePermissionHandler, onAccepted: Action) {
        val notGrantedPermissions = permissions.filterNot { permissionChecker(it) }

        if (notGrantedPermissions.isEmpty()) {
            onAccepted()
        } else {
            val requestCode = requestCodeGenerator()
            requestedHandlers[requestCode] = handler
            permissionRequester(requestCode to permissions.toTypedArray())
        }
    }

    /** Этот метод вызывается для обработки ожидающих результатов разрешений. */
    @Suppress(names = ["unused"])
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onReady() {
        with(pendingResults) {
            forEach { _, value -> value.onPermissionResult() }
            clear()
        }
    }

    /**
     * Обрабатывает результат запроса разрешения.
     *
     * @receiver [RuntimePermissionResult].
     */
    private fun RuntimePermissionResult.onPermissionResult() {
        permissionHandler.onPermissionResult(resultPermissions, grantResults)
    }

    /**
     * Результат запроса разрешения.
     *
     * @property permissionHandler Обработчик результат разрешения.
     * @property resultPermissions Список запрошенных разрешений.
     * @property grantResults Список результатов запрошенных разрешений.
     *
     * @author Pavel Annin (https://github.com/anninpavel).
     */
    @Suppress(names = ["ArrayInDataClass"])
    private data class RuntimePermissionResult(
        val permissionHandler: RuntimePermissionHandler,
        val resultPermissions: Array<String>,
        val grantResults: IntArray
    )
}

/**
 * Интерфейс обработки разрешений.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private interface RuntimePermissionHandler {

    /**
     * Обратный вызов для результата от запроса разрешений.
     * Этот метод вызывается для каждого вызова requestPermissions.
     *
     * @param permissions Список запрошенных разрешений.
     * @param grantResults Список результатов запрошенных разрешений.
     */
    fun onPermissionResult(permissions: Array<String>, grantResults: IntArray)
}

/**
 * Обработчик разрешений.
 *
 * Вызывается [onAccepted] если все запрашиваемые разрешения предоставлены, иначе [onDenied].
 *
 * @property onDenied Содержит список не предоставленных разрешений.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private class RequiredPermissionsHandler(
    private val onAccepted: Action,
    private val onDenied: Consumer<List<String>>
) : RuntimePermissionHandler {

    override fun onPermissionResult(permissions: Array<String>, grantResults: IntArray) {
        val denied = grantResults.indices.filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
        if (denied.isEmpty()) onAccepted() else onDenied(denied.map { permissions[it] })
    }
}

/**
 * Обработчик разрешений.
 *
 * Вызывается [onDenied] если не все разрешения приняты, [onAccepted] вызывается всегда.
 *
 * @property onDenied Содержит список не предоставленных разрешений.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private class UnnecessaryPermissionsHandler(
    private val onAccepted: Action,
    private val onDenied: Consumer<List<String>>
) : RuntimePermissionHandler {

    override fun onPermissionResult(permissions: Array<String>, grantResults: IntArray) {
        val denied = grantResults.indices.filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
        if (denied.isNotEmpty()) {
            onDenied(denied.map { permissions[it] })
        }
        onAccepted()
    }
}
