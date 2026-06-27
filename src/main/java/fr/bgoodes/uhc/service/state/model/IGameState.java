package fr.bgoodes.uhc.service.state.model;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import fr.bgoodes.uhc.context.GameContext;
import org.jetbrains.annotations.NotNull;

public interface IGameState {

    void start(@NotNull GameContext context);

    default void onClock() {}

    default void onPlayerJoin(@NotNull PlayerReadyEvent event, @NotNull GameContext context) {}
}
