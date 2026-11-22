package fr.bgoodes.uhc.util;

import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import org.jetbrains.annotations.NotNull;

public class UHCSoundUtil {

    public static void broadcast(@NotNull String soundKey) {
        Universe.get().getPlayers().forEach(playerRef -> {
            SoundUtil.playSoundEvent2dToPlayer(playerRef, soundKey, SoundCategory.UI);
        });
    }

    public static void send(@NotNull PlayerRef playerRef, @NotNull String soundKey) {
        SoundUtil.playSoundEvent2dToPlayer(playerRef, soundKey, SoundCategory.UI);
    }
}
