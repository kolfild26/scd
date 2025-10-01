package ru.sportmaster.scd.ui.view.loader;

import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;

public abstract class ViewLoader {
    protected static String BASE_PACKAGE = "ru.sportmaster.scd";

    protected final List<String> processedViews = new ArrayList<>();

    public abstract void load();

    public abstract void registerResolver();

    @NotNull
    @SneakyThrows
    protected Class<?> getClass(BeanDefinition beanDefinition) {
        return Class.forName(beanDefinition.getBeanClassName());
    }
}
