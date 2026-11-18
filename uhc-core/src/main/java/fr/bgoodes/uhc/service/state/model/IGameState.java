package fr.bgoodes.uhc.service.state.model;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.service.hud.model.StateHud;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGameState {

    void start();

    default void onClock() {}

    default void end() {}

    default @Nullable StateHud createHud(@NotNull PlayerRef playerRef) {
        return null;
    }
}
