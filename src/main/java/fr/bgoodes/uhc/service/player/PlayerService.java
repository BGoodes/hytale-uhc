package fr.bgoodes.uhc.service.player;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PlayerService {

    @NotNull
    private final Set<UUID> participants = new HashSet<>();

    public void addParticipant(@NotNull UUID uuid) {
        this.participants.add(uuid);
    }

    public void removeParticipant(@NotNull UUID uuid) {
        this.participants.remove(uuid);
    }

    public boolean isParticipant(@NotNull UUID uuid) {
        return this.participants.contains(uuid);
    }

    public int getConnectedParticipantCount() {
        return (int) participants.stream()
                .filter(uuid -> Universe.get().getPlayer(uuid) != null)
                .count();
    }

    public @NotNull Optional<PlayerRef> getWinner() {
        return participants.stream()
                .map(uuid -> Universe.get().getPlayer(uuid))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
