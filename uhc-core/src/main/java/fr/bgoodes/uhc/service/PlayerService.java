package fr.bgoodes.uhc.service;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerService {

    private final Set<UUID> participants = new HashSet<>();

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
        return participants.remove(uuid);
    }
}
