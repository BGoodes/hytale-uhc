package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.hud.impl.TitleHud;
import fr.bgoodes.uhc.service.hud.model.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UHCTitleUtil {

    private UHCTitleUtil() {}

    public static void send(@NotNull PlayerRef playerRef, @NotNull Title title) {
        if (UHC.get().getHudService().get(playerRef.getUuid(), TitleHud.HUD_ID) instanceof TitleHud hud) {
            hud.show(title);
        }
    }

    public static void broadcast(@NotNull Title title) {
        Universe.get().getPlayers().forEach(playerRef -> send(playerRef, title));
    }

    public static void broadcast(@NotNull Message primary, @Nullable Message secondary, int duration) {
        broadcast(new Title(primary, secondary, duration));
    }
}
