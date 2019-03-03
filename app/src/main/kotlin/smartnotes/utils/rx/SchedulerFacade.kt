package smartnotes.utils.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Унифицированный интерфейс для получения эземпляров планировщика.
 *
 * @property io Экземпляр планировщика предназначеного для работы связанной с вводом/выводом.
 * @property computation Экземпляр планировщика предназначеного для вычислительной работы.
 * @property ui Экземпляр планировщика предназначеного для работы в главном потоке.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class SchedulerFacade {

    val io: Scheduler
        get() = Schedulers.io()

    val computation: Scheduler
        get() = Schedulers.computation()

    val ui: Scheduler
        get() = AndroidSchedulers.mainThread()
}
