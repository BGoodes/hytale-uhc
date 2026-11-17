package fr.bgoodes.uhc.service.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerService {

    private final Set<UUID> participants = new HashSet<>();

    public synchronized Set<UUID> getParticipants() {
        return Set.copyOf(participants);
    }

    public synchronized boolean isParticipant(UUID uuid) {
        return participants.contains(uuid);
    }

    public synchronized boolean addParticipant(UUID uuid) {
        return participants.add(uuid);
    }

    public synchronized boolean removeParticipant(UUID uuid) {
        return participants.remove(uuid);
    }
}
