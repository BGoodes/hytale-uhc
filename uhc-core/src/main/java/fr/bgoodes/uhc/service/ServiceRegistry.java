package fr.bgoodes.uhc.service;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {

    private final Map<Class<?>, Object> services = new HashMap<>();

    public <T> void register(@NotNull Class<T> type, @NotNull T service) {
        if (services.putIfAbsent(type, service) != null) {
            throw new IllegalStateException("Service already registered: %s".formatted(type.getName()));
        }
    }

    public <T> @NotNull T get(@NotNull Class<T> type) {
        Object service = services.get(type);

        if (service == null) {
            throw new IllegalStateException("Service not registered: %s".formatted(type.getName()));
        }

        return type.cast(service);
    }

    public boolean has(@NotNull Class<?> type) {
        return services.containsKey(type);
    }
}
