package smartnotes.utils.log

import timber.log.Timber

/**
 * Реализация логирования для debug сборок.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class DebugLogTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return "${super.createStackElementTag(element)}, line: ${element.lineNumber}"
    }
}