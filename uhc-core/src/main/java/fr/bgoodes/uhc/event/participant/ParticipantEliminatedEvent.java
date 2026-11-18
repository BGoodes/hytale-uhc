package fr.bgoodes.uhc.event.participant;

import com.hypixel.hytale.event.IEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ParticipantEliminatedEvent implements IEvent<Void> {

    private final UUID uuid;

    public ParticipantEliminatedEvent(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }
}
