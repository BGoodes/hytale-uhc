package fr.bgoodes.uhc.service.team;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Team {

    @NotNull
    private final String name;

    @NotNull
    private final Set<UUID> members = new HashSet<>();

    public Team(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Set<UUID> getMembers() {
        return members;
    }
}
