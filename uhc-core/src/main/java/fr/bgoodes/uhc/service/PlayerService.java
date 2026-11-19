package fr.bgoodes.uhc.service;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerService {

    private final Set<UUID> participants = new HashSet<>();
    private final Map<UUID, Instant> disconnections = new HashMap<>();

    public synchronized @NotNull Set<UUID> getParticipants() {
        return Set.copyOf(participants);
    }

    public synchronized boolean isParticipant(@NotNull UUID uuid) {
        return participants.contains(uuid);
    }

    public synchronized boolean addParticipant(@NotNull UUID uuid) {
        return participants.add(uuid);
    }

    public synchronized boolean removeParticipant(@NotNull UUID uuid) {
        disconnections.remove(uuid);
        return participants.remove(uuid);
    }

    public synchronized @NotNull Map<UUID, Instant> getDisconnections() {
        return Map.copyOf(disconnections);
    }

    public synchronized void setDisconnected(@NotNull UUID uuid) {
        disconnections.put(uuid, Instant.now());
    }

    public synchronized void removeDisconnected(@NotNull UUID uuid) {
        disconnections.remove(uuid);
    }
}
