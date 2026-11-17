package fr.bgoodes.uhc.service.team;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Team {

    private final String id;
    private final String name;
    private final @Nullable Color color;
    private final Set<UUID> members = new HashSet<>();

    Team(String id, String name, @Nullable Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable Color getColor() {
        return color;
    }

    public synchronized @NotNull Set<UUID> getMembers() {
        return Set.copyOf(members);
    }

    synchronized void addMember(@NotNull UUID uuid) {
        members.add(uuid);
    }

    synchronized void removeMember(@NotNull UUID uuid) {
        members.remove(uuid);
    }
}
