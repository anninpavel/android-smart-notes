@file:JvmMultifileClass

package smartnotes.utils.extensions

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.UnicastSubject
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer
import java.util.concurrent.*

/**
 * Ограничивает вызов [Action]. Вызывает [Action] при первом, последующие вызовы [Action] будут проигнорированы
 * в течении [duration] миллисекунд.
 *
 * @param duration Время ожидания до вызова [Action] после последнего вызова.
 * @param disposables Контейнер [CompositeDisposable].
 *
 * @receiver [Action].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Action.throttleFirst(duration: Long = 500L, disposables: CompositeDisposable = CompositeDisposable()): Action {
    val subject = UnicastSubject.create<Unit>().apply {
        disposables += throttleFirst(duration, TimeUnit.MILLISECONDS)
            .subscribe { this@throttleFirst.invoke() }
    }
    return { subject.onNext(Unit) }
}

/**
 * Ограничивает вызов [Consumer]. Игнорирует вызовы [Consumer] за которыми следуют новые вызовы
 * до истечения [duration] миллисекунд.
 *
 * @param disposables Контейнер [CompositeDisposable].
 * @param duration Время ожидания нового вызова [Consumer].
 * @param scheduler Планировщик который обрабатывает результат ожидания для каждого элемента.
 *
 * @receiver [Consumer].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T> Consumer<T>.debounce(
    disposables: CompositeDisposable = CompositeDisposable(),
    duration: Long = 500L,
    scheduler: Scheduler = AndroidSchedulers.mainThread()
): Consumer<T> {
    val subject = UnicastSubject.create<T>().apply {
        disposables += debounce(duration, TimeUnit.MILLISECONDS)
            .observeOn(scheduler)
            .subscribe { value -> this@debounce.invoke(value) }
    }
    return { value -> subject.onNext(value) }
}
