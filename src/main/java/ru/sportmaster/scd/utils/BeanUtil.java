package ru.sportmaster.scd.utils;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public synchronized void setApplicationContext(@NotNull ApplicationContext applicationContext)
        throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return Optional.ofNullable(context)
            .map(applicationContext -> applicationContext.getBean(beanClass))
            .orElse(null);
    }
}
