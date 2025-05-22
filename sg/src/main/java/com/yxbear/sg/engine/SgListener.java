package com.yxbear.sg.engine;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationListener;

public interface SgListener<T> extends ApplicationListener<SgContextEvent> {

    static final Map<Class<?>, Class<?>> typeCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public default void onApplicationEvent(SgContextEvent event) {
        Object source = event.getSource();
        if (isSupport(source)) {
            onEvent((T) source);
        }
    }

    public default boolean isSupport(Object source) {
        Class<?> expectedType = getGenericType();
        return expectedType != null && expectedType.isAssignableFrom(source.getClass());
    }

    public default Class<?> getGenericType() {
        return typeCache.computeIfAbsent(this.getClass(), clazz -> {
            Type genericSuperclass = clazz.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>) { return (Class<?>) actualTypeArguments[0]; }
            }
            return null;
        });
    }

    void onEvent(T t);

}
