package fr.bgoodes.uhc.service.hud.model;

import com.hypixel.hytale.server.core.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Title(@NotNull Message primary, @Nullable Message secondary, int duration) {

    public static final int DEFAULT_DURATION = 3;

    public Title(@NotNull Message primary) {
        this(primary, null, DEFAULT_DURATION);
    }
}
