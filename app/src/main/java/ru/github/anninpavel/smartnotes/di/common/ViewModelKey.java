package ru.github.anninpavel.smartnotes.di.common;

import androidx.lifecycle.ViewModel;
import dagger.MapKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотаций ключа [ViewModel] для графа зависимости.<br>
 * Аннотация перенеса в java, в связи дефектом, вызванный переходом на kotlin 1.3.30
 * <a href="https://github.com/google/dagger/issues/1478">(подробнее)</a>.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
