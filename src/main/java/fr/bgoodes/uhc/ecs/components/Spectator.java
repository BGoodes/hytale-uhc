package fr.bgoodes.uhc.ecs.components;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.Nullable;

public class Spectator implements Component<EntityStore> {

    public static final Spectator INSTANCE = new Spectator();
    private static ComponentType<EntityStore, Spectator> type;

    public static void setComponentType(ComponentType<EntityStore, Spectator> type) {
        Spectator.type = type;
    }

    public static ComponentType<EntityStore, Spectator> getComponentType() {
        return Spectator.type;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return INSTANCE;
    }
}
