package fr.bgoodes.uhc.service.player;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerService {

    @NotNull
    private final Set<UUID> participants = new HashSet<>();

    @NotNull
    private final Map<UUID, Integer> disconnectCountdowns = new HashMap<>();

    public void addParticipant(@NotNull UUID uuid) {
        this.participants.add(uuid);
    }

    public void removeParticipant(@NotNull UUID uuid) {
        this.participants.remove(uuid);
        this.disconnectCountdowns.remove(uuid);
    }

    public boolean isParticipant(@NotNull UUID uuid) {
        return this.participants.contains(uuid);
    }

    public @NotNull Set<UUID> getParticipants() {
        return new HashSet<>(participants);
    }

    // disconnect countdown
    public void setDisconnectCountdown(@NotNull UUID uuid, int seconds) {
        if (!participants.contains(uuid)) return;
        disconnectCountdowns.put(uuid, seconds);
    }

    public void removeDisconnectCountdown(@NotNull UUID uuid) {
        disconnectCountdowns.remove(uuid);
    }

    public @NotNull Map<UUID, Integer> getDisconnectCountdowns() {
        return disconnectCountdowns;
    }
}
