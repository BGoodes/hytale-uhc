package fr.bgoodes.uhc.service.player;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    public int getParticipantCount() {
        return participants.size();
    }

    public @NotNull Optional<PlayerRef> getWinner() {
        return participants.stream()
                .map(uuid -> Universe.get().getPlayer(uuid))
                .filter(Objects::nonNull)
                .findFirst();
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
