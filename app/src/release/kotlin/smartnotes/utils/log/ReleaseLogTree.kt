package smartnotes.utils.log

import timber.log.Timber

/**
 * Реализация логирования для release сборок.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ReleaseLogTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Empty
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return false
    }
}