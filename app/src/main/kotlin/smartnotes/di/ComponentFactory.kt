package smartnotes.di

import smartnotes.SmartNotesApplication
import smartnotes.di.components.ApplicationComponent
import smartnotes.di.components.DaggerApplicationComponent

/**
 * Фабрика предоставляющая основной компонент графа зависимостей приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class ComponentFactory {

    /**
     * Создает основной компонент графа зависимостей приложения.
     *
     * @param application Экземпляр приложения.
     */
    fun build(application: SmartNotesApplication): ApplicationComponent {
        return componentBuilder(application).build()
    }

    private fun componentBuilder(application: SmartNotesApplication): ApplicationComponent.Builder {
        return DaggerApplicationComponent.builder()
            .application(application)
    }
}
