package fr.bgoodes.uhc.event;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ParticipantEliminatedEvent implements IEvent<Void> {

    private final UUID uuid;
    private @Nullable Message message;

    public ParticipantEliminatedEvent(@NotNull UUID uuid, @Nullable Message message) {
        this.uuid = uuid;
        this.message = message;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }
}
