package fr.bgoodes.uhc.service.hud.model;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public abstract class UHCHud extends CustomUIHud {

    protected UHCHud(@NotNull PlayerRef playerRef, @NotNull String key) {
        super(playerRef, key);
    }

    protected UHCHud(@NotNull PlayerRef playerRef, @NotNull String key, int zOrder) {
        super(playerRef, key, zOrder);
    }

    public void refresh() {}
}
