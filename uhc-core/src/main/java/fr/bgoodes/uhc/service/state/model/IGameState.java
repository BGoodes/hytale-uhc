package fr.bgoodes.uhc.service.state.model;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGameState {

    String HUD_KEY = "uhc:state";

    void start();

    default void onClock() {}

    default void end() {}

    default @Nullable UHCHud createHud(@NotNull PlayerRef playerRef) {
        return null;
    }
}
