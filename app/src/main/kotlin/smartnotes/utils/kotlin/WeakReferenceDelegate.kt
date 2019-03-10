@file:JvmMultifileClass

package smartnotes.utils.kotlin

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/** @author Pavel Annin (https://github.com/anninpavel). */
class WeakReferenceDelegate<T>(value: T? = null) : ReadWriteProperty<Any?, T?> {

    private var weak: WeakReference<T>? = value?.let { WeakReference(it) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weak?.get()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weak = value?.let { WeakReference(it) }
    }
}

/** @author П. Аннин. */
fun <T> weak(value: T? = null): WeakReferenceDelegate<T> {
    return WeakReferenceDelegate(value)
}
