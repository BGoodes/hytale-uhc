package fr.bgoodes.uhc.service.hud.model;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public abstract class StateHud extends CustomUIHud {

    public static final String HUD_ID = "uhc:state";

    protected StateHud(@NotNull PlayerRef playerRef) {
        super(playerRef, HUD_ID);
    }

    public abstract void refresh();
}
