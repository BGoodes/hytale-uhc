package fr.bgoodes.uhc.service.state.model;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.context.GameContext;
import org.jetbrains.annotations.NotNull;

public interface IGameState {

    void start(@NotNull GameContext context);

    default void onClock(@NotNull World world, @NotNull Store<EntityStore> store) {}

    default void onPlayerJoin(@NotNull PlayerReadyEvent event, @NotNull GameContext context) {}
}
